package com.ujuzy.ujuzy.ujuzy.app_intro;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ujuzy.ujuzy.ujuzy.Activities.LoadLoggedInUserInfoToRealm;
import com.ujuzy.ujuzy.ujuzy.Activities.MapsActivity;
import com.ujuzy.ujuzy.ujuzy.R;
import com.ujuzy.ujuzy.ujuzy.Realm.RealmToken;
import com.ujuzy.ujuzy.ujuzy.Realm.RealmTokenHelper;
import com.ujuzy.ujuzy.ujuzy.Realm.RealmUser;
import com.ujuzy.ujuzy.ujuzy.Realm.RealmUserHelper;
import com.ujuzy.ujuzy.ujuzy.model.Constants;

import org.jboss.aerogear.android.authorization.AuthorizationManager;
import org.jboss.aerogear.android.authorization.AuthzModule;
import org.jboss.aerogear.android.authorization.oauth2.OAuth2AuthorizationConfiguration;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import io.realm.Realm;

public class SigninOrSkipActivity extends AppCompatActivity {

    private Button signInBt, skipBtn;
    private TextView termsTv;
    private Realm realm;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin_or_skip);

        initWindow();
        initSignIn();
        initSkip();
        initVideoBackground();
    }

    private void initVideoBackground()
    {
       /* VideoView videoview = (VideoView) findViewById(R.id.videoView);
        Uri uri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.ujuzy_video);
        videoview.setVideoURI(uri);
        videoview.start();*/
    }

    private void initSkip()
    {
        skipBtn = (Button) findViewById(R.id.btn_skip);
        skipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SigninOrSkipActivity.this , MapsActivity.class));
                finish();
            }
        });
    }

    private void initSignIn()
    {
        signInBt = (Button) findViewById(R.id.btn_signup);
        signInBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

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

                    authzModule.requestAccess(SigninOrSkipActivity.this, new org.jboss.aerogear.android.core.Callback<String>() {
                        @Override
                        public void onSuccess(final String data) {

                            //SAVE TOKEN TO REALM DATABASE
                            RealmToken token = new RealmToken();
                            token.setToken(data);

                            realm = Realm.getDefaultInstance();
                            RealmTokenHelper helper = new RealmTokenHelper(realm);
                            helper.save(token);


                            initUserProfileInfoToRealmFromApi();

                            Intent filterPlumbers = new Intent(SigninOrSkipActivity.this, MapsActivity.class);
                            startActivity(filterPlumbers);

                            finish();

                        }

                        @Override
                        public void onFailure(Exception e) {
                            //Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });


                } catch (MalformedURLException e) {
                    // e.printStackTrace();
                }

            }
        });
    }

    private void initUserProfileInfoToRealmFromApi()
    {
        try {

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

            authzModule.requestAccess(SigninOrSkipActivity.this, new org.jboss.aerogear.android.core.Callback<String>() {
                @Override
                public void onSuccess(final String data) {

                    //SAVE TOKEN TO REALM DATABASE
                    RealmToken token = new RealmToken();
                    token.setToken(data);

                    StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.HTTP.USER_PROFILE_JSON_URL,
                            new com.android.volley.Response.Listener<String>() {
                                @Override
                                public void onResponse(String response)
                                {

                                    try {

                                        JSONObject jsonObject = new JSONObject(response);

                                        RealmUser realmService = new  RealmUser();
                                        realmService.setId(jsonObject.getString("id"));
                                        realmService.setFirstname(jsonObject.getString("firstname"));
                                        realmService.setLastname(jsonObject.getString("lastname"));
                                        realmService.setGender(jsonObject.getString("gender"));
                                        realmService.setEmail(jsonObject.getString("email"));
                                        realmService.setCreated_at(jsonObject.getString("created_at"));
                                        realmService.setPhone(jsonObject.getString("phone_number"));
                                        //realmService.setVerified(jsonObject.getString("phone_number"));

                                        JSONObject jsonUserRole = new JSONObject(response).getJSONObject("user_role");
                                        realmService.setUserRole(jsonUserRole.getString("role_name"));
                                        JSONObject jsonUserProfilePic = new JSONObject(response).getJSONObject("profile_pic");
                                        realmService.setProfilePic(jsonUserProfilePic.getString("thumb"));

                                        //SAVE
                                        realm = Realm.getDefaultInstance();
                                        RealmUserHelper helper = new RealmUserHelper(realm);
                                        helper.save(realmService);


                                    } catch (JSONException e) {
                                        //e.printStackTrace();
                                    }

                                }
                            }, new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            authzModule.deleteAccount();

                        }
                    }) {

                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            Map<String,String> params = new HashMap<String, String>();
                            params.put("Authorization","Bearer "+ data);
                            params.put("Content-Type","application/json");
                            params.put("Accept","application/json");
                            return params;
                        }
                    };

                    requestQueue = Volley.newRequestQueue(SigninOrSkipActivity.this);
                    requestQueue.add(stringRequest);

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

    private void initWindow()
    {
        Window window = SigninOrSkipActivity.this.getWindow();
        // stop keyboard from show when activity is started.
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            window.setStatusBarColor(ContextCompat.getColor( SigninOrSkipActivity.this,R.color.grey_line));

            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        }
    }
}
