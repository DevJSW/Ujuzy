package com.ujuzy.ujuzy.ujuzy.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.ujuzy.ujuzy.ujuzy.R;
import com.ujuzy.ujuzy.ujuzy.Realm.RealmToken;
import com.ujuzy.ujuzy.ujuzy.RetrofitClasses.PostRequests;
import com.ujuzy.ujuzy.ujuzy.model.Constants;
import com.ujuzy.ujuzy.ujuzy.model.CreateService;
import com.ujuzy.ujuzy.ujuzy.model.Location;
import com.ujuzy.ujuzy.ujuzy.model.Service;

import org.jboss.aerogear.android.authorization.AuthorizationManager;
import org.jboss.aerogear.android.authorization.AuthzModule;
import org.jboss.aerogear.android.authorization.oauth2.OAuth2AuthorizationConfiguration;
import org.jboss.aerogear.android.core.Callback;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.realm.Realm;

public class CreateServiceActivity extends AppCompatActivity {

    private ImageView backBtn, addImgIv;
    private Uri resultUri = null;
    private Uri mImageUri = null;
    Bitmap bitmap;
    private static int GALLERY_REQUEST =1;
    private Button createBtn;

    Spinner spinner_category, spinner_travel;
    List<String> serviceCategory;
    Spinner spinner_billing;
    List<String> serviceBilling;
    List<String> serviceTravel;

    String service_name = "";
    String service_details = "";
    String service_cost = "";
    String selectedCategory = "";
    String selectedBilling = "";
    String selectedTravel = "";
    String service_image = "";
    String authorization = "";
    String contentType = "";
    String accept = "";

    private EditText inputServiceName, inputServiceDetails, inputServiceCost;
    //json volley
    private JsonArrayRequest request;
    private RequestQueue requestQueue;
    private Realm realm;

    private PostRequests postRequests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_service);

        initWindows();
        initBactBtn();
        initSpinners();
        initAddImage();
        initCreateService();
    }

    private void initCreateService()
    {
        createBtn = (Button) findViewById(R.id.btn_create);
        createBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {

                inputServiceName = (EditText) findViewById(R.id.inputServiceNameEt);
                inputServiceDetails = (EditText) findViewById(R.id.inputDescServiceEt);
                inputServiceCost = (EditText) findViewById(R.id.inputCostServiceEt);

                service_name = inputServiceName.getText().toString();
                service_details = inputServiceDetails.getText().toString();
                service_cost = inputServiceCost.getText().toString();

                Realm realm = Realm.getDefaultInstance();
                RealmToken realmToken = realm.where(RealmToken.class).findFirst();

                postRequests = new PostRequests();
                authorization = "Bearer "+ realmToken.getToken();
                contentType = Constants.HEADERS.CONTENT_TYPE;
                accept = Constants.HEADERS.ACCEPT;

                postRequests.CreateServiceReq(authorization, accept, contentType ,service_name, service_details, service_cost, selectedCategory, selectedBilling,true);

//                Map<String, Integer> myMap = new HashMap<String, Integer>();
//                Map<String, String> map2 = (Map<String, String>)myMap;
//                map2.put("maping", "foo");
//
//                HashMap<String,String> location = new HashMap<String,String>();
//                HashMap<String, Boolean> travelInt = new HashMap<String, Boolean>();
//                HashMap<String, HashMap<String,String>> locationParam = new HashMap<String,HashMap<String,String>>(); // <--------------------------- sending location object
//                location.put("city", "Koinange Street, Nairobi, Kenya");
//                location.put("lat", String.valueOf(-1.2838881));
//                location.put("lng", String.valueOf(36.818703700000015));
//                locationParam.put("location", location);
//
//                HashMap<String, String> params= new HashMap<String, String>();
//                params.put("service_name", service_name);
//                params.put("service_details", service_details);
//                params.put("offer_cost", service_cost);
//                params.put("category", selectedCategory);
//                params.put("billing", selectedBilling);
//                params.put("location", String.valueOf(locationParam));
//                params.put("travel", String.valueOf(false));
//
//
//                HashMap data = new HashMap();
//                data.put("status_id", new Integer(1));
//                data.put("service_name", service_name);
//                data.put("service_details", service_details);
//                data.put("offer_cost", service_cost);
//                data.put("category", selectedCategory);
//                data.put("billing", selectedBilling);
//                data.put("travel", false);
//
//
//                if (TextUtils.isEmpty(service_name))
//                {
//                    inputServiceName.setError("Enter name of your service");
//                } else if (TextUtils.isEmpty(service_details)){
//                    inputServiceDetails.setError("Enter detail of service");
//                } else if (TextUtils.isEmpty(service_cost)){
//                    inputServiceCost.setError("Enter cost of your service");
//                } else {
//                    try
//                    {
//                        final AuthzModule authzModule = AuthorizationManager
//                                .config("KeyCloakAuthz", OAuth2AuthorizationConfiguration.class)
//                                .setBaseURL(new URL(Constants.HTTP.AUTH_BASE_URL))
//                                .setAuthzEndpoint("/auth/realms/ujuzy/protocol/openid-connect/auth")
//                                .setAccessTokenEndpoint("/auth/realms/ujuzy/protocol/openid-connect/token")
//                                .setAccountId("account")
//                                .setClientId("account")
//                                .setRedirectURL("https://ujuzy.com")
//                                .setScopes(Arrays.asList("openid"))
//                                .addAdditionalAuthorizationParam((Pair.create("grant_type", "password")))
//                                .asModule();
//
//                        authzModule.requestAccess(CreateServiceActivity.this, new Callback<String>()
//                        {
//                            @Override
//                            public void onSuccess(final String data)
//                            {
//
//                                JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
//                                        Constants.HTTP.CREATE_SERVICE_JSON_URL, new JSONObject(params),
//                                        new Response.Listener<JSONObject>()
//                                        {
//                                            @Override
//                                            public void onResponse(JSONObject response)
//                                            {
//
//                                                Toast.makeText(CreateServiceActivity.this, "Service created successfully", Toast.LENGTH_LONG).show();
//
//                                            }
//                                        }, new Response.ErrorListener() {
//                                    @Override
//                                    public void onErrorResponse(VolleyError error)
//                                    {
//
//                                        NetworkResponse response = error.networkResponse;
//                                        if (error instanceof ServerError && response != null)
//                                        {
//                                            try
//                                            {
//                                                String res = new String(response.data,
//                                                        HttpHeaderParser.parseCharset(response.headers, "utf-8"));
//                                                // Now you can use any deserializer to make sense of data
//                                                JSONObject obj = new JSONObject(res);
//                                            } catch (UnsupportedEncodingException e1)
//                                            {
//                                                // Couldn't properly decode data to string
//                                                e1.printStackTrace();
//                                            } catch (JSONException e2)
//                                            {
//                                                // returned data is not JSONObject?
//                                                e2.printStackTrace();
//                                            }
//                                        }
//                                    }
//
//                                }) {
//
//                                    /**
//                                     * Passing some request headers
//                                     * */
//                                    @Override
//                                    public Map<String, String> getHeaders() throws AuthFailureError {
//
//                                        HashMap<String, String> headers = new HashMap<String, String>();
//                                        headers.put("Authorization","Bearer "+ data);
//                                        headers.put("Content-Type", "application/json; charset=utf-8");
//                                        headers.put("Accept","application/json");
//                                        return headers;
//                                    }
//
//                                };
//
//                                // Adding request to request queue
//                                requestQueue = Volley.newRequestQueue(CreateServiceActivity.this);
//                                requestQueue.add(jsonObjReq);
//
//                            }
//
//                            @Override
//                            public void onFailure(Exception e) {
//                                // Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
//                            }
//                        });
//
//
//                    } catch (MalformedURLException e) {
//                        //e.printStackTrace();
//                    }
//
//
//                }

            }
        });

    }

    private void initAddImage() {
        addImgIv = (ImageView) findViewById(R.id.addServiceImg);
        addImgIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_REQUEST);
            }
        });
    }

    private void initSpinners()
    {
        spinner_category = (Spinner) findViewById(R.id.spinner_category);

        String[] service_category_string = new String[]{
                "Choose a category",
                "Accounting",
                "Broadcast Media",
                "Beauty & Grooming",
                "Cosmetics",
                "Carpentry",
                "Electrical",
                "Masonry",
                "Painting",
                "Plumbing",
                "Security",
                "Landscaping",
                "Legal",
                "Tailoring",
                "Welders",
                "Others"
        };

        serviceCategory = new ArrayList<>(Arrays.asList(service_category_string));

        // Initializing an ArrayAdapter
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                getApplicationContext(), R.layout.spinner_filter_item, serviceCategory) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };


        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinner_category.setAdapter(spinnerArrayAdapter);

        spinner_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCategory = (String) parent.getItemAtPosition(position);
                // If user change the default selection
                // First item is disable and it is used for hint
                // Notify the selected item text

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        spinner_billing = (Spinner) findViewById(R.id.spinner_billing);

        String[] service_billing_string = new String[]{
                "Billing Cycle",
                "One off payment",
                "Hourly",
                "Daily",
                "Monthly"
        };

        serviceBilling = new ArrayList<>(Arrays.asList(service_billing_string));

        // Initializing an ArrayAdapter
        final ArrayAdapter<String> spinnerArrayAdapter2 = new ArrayAdapter<String>(
                getApplicationContext(), R.layout.spinner_filter_item, serviceBilling) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };


        spinnerArrayAdapter2.setDropDownViewResource(R.layout.spinner_item);
        spinner_billing.setAdapter(spinnerArrayAdapter2);

        spinner_billing.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedBilling = (String) parent.getItemAtPosition(position);
                // If user change the default selection
                // First item is disable and it is used for hint
                // Notify the selected item text

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_travel = (Spinner) findViewById(R.id.spinner_travel);

        String[] service_travel_string = new String[]{
                "Willing to travel",
                "true",
                "false"
        };

        serviceTravel = new ArrayList<>(Arrays.asList(service_travel_string));

        // Initializing an ArrayAdapter
        final ArrayAdapter<String> spinnerArrayAdapter3 = new ArrayAdapter<String>(
                getApplicationContext(), R.layout.spinner_filter_item, serviceTravel) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };


        spinnerArrayAdapter3.setDropDownViewResource(R.layout.spinner_item);
        spinner_travel.setAdapter(spinnerArrayAdapter3);

        spinner_travel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedTravel = (String) parent.getItemAtPosition(position);
                // If user change the default selection
                // First item is disable and it is used for hint
                // Notify the selected item text

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void initWindows()
    {
        Window window = CreateServiceActivity.this.getWindow();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            window.setStatusBarColor(ContextCompat.getColor( CreateServiceActivity.this,R.color.colorPrimaryDark));

            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        }

    }

    private void initBactBtn()
    {
        backBtn = (ImageView) findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_REQUEST && resultCode == RESULT_OK)
        {

            mImageUri = data.getData();

            CropImage.activity(mImageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setCropShape(CropImageView.CropShape.RECTANGLE)
                    .setAspectRatio(4, 3)
                    .start(CreateServiceActivity.this);

        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
        {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK)
            {
                resultUri = result.getUri();
                try
                {
                    //getting image from gallery
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), resultUri);

                    //Setting image to ImageView
                    addImgIv.setImageBitmap(bitmap);
                    addImgIv.setImageURI(resultUri);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

}

