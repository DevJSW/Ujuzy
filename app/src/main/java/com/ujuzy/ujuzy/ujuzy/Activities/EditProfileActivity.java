package com.ujuzy.ujuzy.ujuzy.Activities;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.ujuzy.ujuzy.ujuzy.R;
import com.ujuzy.ujuzy.ujuzy.Realm.RealmToken;
import com.ujuzy.ujuzy.ujuzy.Realm.RealmTokenHelper;
import com.ujuzy.ujuzy.ujuzy.Realm.RealmUser;
import com.ujuzy.ujuzy.ujuzy.model.Constants;

import org.jboss.aerogear.android.authorization.AuthorizationManager;
import org.jboss.aerogear.android.authorization.AuthzModule;
import org.jboss.aerogear.android.authorization.oauth2.OAuth2AuthorizationConfiguration;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmChangeListener;

public class EditProfileActivity extends AppCompatActivity {

    String firstName = "";
    String lastName = "";
    String phone = "";
    String bio = "";
    private Realm realm;
    private RealmChangeListener realmChangeListener;
    private ImageView editPhoto, back;
    private Uri resultUri = null;
    private Uri mImageUri = null;
    private static int GALLERY_REQUEST = 1;
    private EditText phoneInput, firstNameInput, lastNameInput, bioInput;
    private Button saveBtn;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        initEdit();
        initEditInputs();
        initBack();
    }

    private void initBack()
    {
        back = (ImageView) findViewById(R.id.backBtn);
        back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });
    }

    private void initSaveEdit()
    {
        firstName = firstNameInput.getText().toString();
        lastName = lastNameInput.getText().toString();
        phone = phoneInput.getText().toString();
        bio = bioInput.getText().toString();

        if (TextUtils.isEmpty(firstName)) {
            firstNameInput.setError("First name must not be Empty");
        } else if (TextUtils.isEmpty(lastName)){
            lastNameInput.setError("Last name must not be Empty!");
        } else if (TextUtils.isEmpty(phone)){
            phoneInput.setError("Your phone must not be Empty!");
        } else {

            realm = Realm.getDefaultInstance();
            final RealmTokenHelper helper = new RealmTokenHelper(realm);

            try
            {

                final AuthzModule authzModule = AuthorizationManager
                        .config("KeyCloakAuthz", OAuth2AuthorizationConfiguration.class)
                        .setBaseURL(new URL(Constants.HTTP.AUTH_BASE_URL))
                        .setAuthzEndpoint("/auth/realms/ujuzy/protocol/openid-connect/auth")
                        .setAccessTokenEndpoint("/auth/realms/ujuzy/protocol/openid-connect/token")
                        .setAccountId("account")
                        .setClientId("account")
                        .setRedirectURL("https://ujuzy.com")
                        .setScopes(Arrays.asList("openid"))
                        .addAdditionalAuthorizationParam((Pair.create("grant_type", "password")))
                        .asModule();

                authzModule.requestAccess(EditProfileActivity.this, new org.jboss.aerogear.android.core.Callback<String>() {
                    @Override
                    public void onSuccess(final String data) {

                        //SAVE TOKEN TO REALM DATABASE
                        RealmToken token = new RealmToken();
                        token.setToken(data);

                        //initRetrofit();

                        Map<String, String> params= new HashMap<String, String>();
                        params.put("first_name",firstName);
                        params.put("last_name",lastName);
                        params.put("phone_number",phone);
                        params.put("bio",bio);

                        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                                Constants.HTTP.EDIT_ACCOUNT_JSON_URL, new JSONObject(params),
                                new com.android.volley.Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response)
                                    {

                                        Toast.makeText(EditProfileActivity.this, "Account updated successfully", Toast.LENGTH_LONG).show();

                                    }
                                }, new com.android.volley.Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error)
                            {
                                Toast.makeText(EditProfileActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                            }

                        }) {

                            /**
                             * Passing some request headers
                             * */
                            @Override
                            public Map<String, String> getHeaders() throws AuthFailureError {

                                HashMap<String, String> headers = new HashMap<String, String>();
                                headers.put("Authorization","Bearer "+ data);
                                headers.put("Content-Type", "application/json; charset=utf-8");
                                headers.put("Accept","application/json");
                                return headers;
                            }

                        };

                        // Adding request to request queue
                        requestQueue = Volley.newRequestQueue(EditProfileActivity.this);
                        requestQueue.add(jsonObjReq);

                        realm = Realm.getDefaultInstance();
                        RealmTokenHelper helper = new RealmTokenHelper(realm);
                        helper.save(token);

                    }

                    @Override
                    public void onFailure(Exception e) {
                        //Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        authzModule.deleteAccount();
                    }
                });


            } catch (MalformedURLException e) {
                // e.printStackTrace();
            }
        }
    }

    private void initEditInputs()
    {
        phoneInput = (EditText) findViewById(R.id.editPhone);
        firstNameInput = (EditText) findViewById(R.id.editFirstName);
        lastNameInput = (EditText) findViewById(R.id.editLastName);
        bioInput = (EditText) findViewById(R.id.editBio);

        realm = Realm.getDefaultInstance();
        RealmUser realmUser = realm.where(RealmUser.class).findFirst();
        if (realmUser != null)
        {
            if (realmUser.getFirstname() != null)
                firstNameInput.setText(realmUser.getFirstname());
            if (realmUser.getLastname() != null)
                lastNameInput.setText(realmUser.getLastname());
            if (realmUser.getPhone() != null)
                phoneInput.setText(realmUser.getPhone());
            if (realmUser.getBio() != null)
                bioInput.setText((Integer) realmUser.getBio());
        }

    }

    private void initEdit()
    {
        editPhoto = (ImageView) findViewById(R.id.editPhoto);
        editPhoto.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_REQUEST);
            }
        });

        saveBtn = (Button) findViewById(R.id.saveBtn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initSaveEdit();
            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_REQUEST && resultCode == RESULT_OK) {

            mImageUri = data.getData();

            CropImage.activity(mImageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setCropShape(CropImageView.CropShape.OVAL)
                    .setAspectRatio(1, 1)
                    .start(EditProfileActivity.this);

        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                resultUri = result.getUri();
                editPhoto.setImageURI(resultUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        realm = Realm.getDefaultInstance();
        if (realmChangeListener != null)
            realm.removeChangeListener(realmChangeListener);
    }

}
