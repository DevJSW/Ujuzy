package com.ujuzy.ujuzy.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ujuzy.ujuzy.R;
import com.ujuzy.ujuzy.Realm.RealmFavourite;
import com.ujuzy.ujuzy.Realm.RealmFavouriteHelper;
import com.ujuzy.ujuzy.Realm.RealmToken;
import com.ujuzy.ujuzy.Realm.RealmTokenHelper;
import com.ujuzy.ujuzy.map.MapsActivity;
import com.ujuzy.ujuzy.model.Constants;
import com.ujuzy.ujuzy.model.Request;
import com.ujuzy.ujuzy.model.SignUp;
import com.ujuzy.ujuzy.services.Api;

import org.jboss.aerogear.android.authorization.AuthorizationManager;
import org.jboss.aerogear.android.authorization.AuthzModule;
import org.jboss.aerogear.android.authorization.oauth2.OAuth2AuthorizationConfiguration;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Header;

import static io.realm.SyncCredentials.IdentityProvider.ACCESS_TOKEN;

public class RequestServiceActivity extends AppCompatActivity {

    String serviceCost = "";
    String serviceName = "";
    String serviceId = "";
    String first_name = "";
    String last_name = "";
    String profile_pic = "";
    String no_of_personnel = "";

    private TextView serviceNameTv, userFullName, noOfPersonnel, serviceCostTv;
    private ImageView userAvatar, backBtnIv;
    private EditText inputDateEt, inputRequestEt, inputPhone, inputName, inputTimeEt;
    private Button confirmBtn;

    private Realm realm;
    private Retrofit retrofit;


    //json volley
    private final String JSON_URL = "https://api.ujuzy.com/requests";
    private JsonArrayRequest request;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_service);
        initWindows();
        initServiceInfo();
        initBackBtn();
        initConfirmBtn();
    }

    private void initConfirmBtn() {

        confirmBtn = (Button) findViewById(R.id.btn_request);
        inputDateEt = (EditText) findViewById(R.id.inputDateTxt);
        inputTimeEt = (EditText) findViewById(R.id.inputTimeTxt);
        inputRequestEt = (EditText) findViewById(R.id.inputRequestTxt);
        inputPhone = (EditText) findViewById(R.id.inputPhoneTxt);
        inputName = (EditText) findViewById(R.id.inputNameTxt);

        final String date = inputDateEt.getText().toString();
        final String request = inputRequestEt.getText().toString();
        final String time = inputTimeEt.getText().toString();
        final String phone = inputPhone.getText().toString();
        final String name = inputName.getText().toString();

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*if (TextUtils.isEmpty(date)) {
                    inputDateEt.setError("Enter time and date most suitable for you");
                } else */if (!TextUtils.isEmpty(request)){
                    inputRequestEt.setError("Describe your request!");
                } else {

                    realm = Realm.getDefaultInstance();
                    final RealmTokenHelper helper = new RealmTokenHelper(realm);

                    //RETRIEVE
                    helper.retreiveFromDB();

                    //CHECK IF DATABASE IS EMPTY
                    if (helper.refreshDatabase().size() == 0)
                    {
                        try {

                            AuthzModule authzModule = AuthorizationManager
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

                            authzModule.requestAccess(RequestServiceActivity.this, new org.jboss.aerogear.android.core.Callback<String>() {
                                @Override
                                public void onSuccess(final String data) {

                                    //SAVE TOKEN TO REALM DATABASE
                                    RealmToken token = new RealmToken();
                                    token.setToken(data);

                                    realm = Realm.getDefaultInstance();
                                    RealmTokenHelper helper = new RealmTokenHelper(realm);
                                    helper.save(token);

                                    //requestService(data, name, phone, date, serviceId, time, request);
                                    StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.POST, JSON_URL, new com.android.volley.Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {


                                        }
                                    }, new com.android.volley.Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {

                                        }
                                    }) {
                                        @Override
                                        protected Map<String,String> getParams(){
                                            Map<String,String> params = new HashMap<String, String>();
                                            params.put("name",name);
                                            params.put("phone_number",phone);
                                            params.put("date", date);
                                            params.put("service_id",serviceId);
                                            params.put("time",time);
                                            params.put("request_info",request);

                                            return params;
                                        }

                                        @Override
                                        public Map<String, String> getHeaders() throws AuthFailureError {
                                            Map<String,String> params = new HashMap<String, String>();
                                            params.put("Authorization",data);
                                            params.put("Content-Type","application/x-www-form-urlencoded");
                                            return params;
                                        }
                                    };

                                    requestQueue = Volley.newRequestQueue(RequestServiceActivity.this);
                                    requestQueue.add(stringRequest);
                                }

                                @Override
                                public void onFailure(Exception e) {
                                    System.err.println("Error!!");
                                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });


                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }

                        //startActivity(new Intent(UserProfileActivity.this, LoginActivity.class));

                    } else {

                        try {

                            AuthzModule authzModule = AuthorizationManager
                                    .config("KeyCloakAuthz", OAuth2AuthorizationConfiguration.class)
                                    .setBaseURL(new URL(Constants.HTTP.AUTH_BASE_URL))
                                    .setAuthzEndpoint("/auth/realms/ujuzy/protocol/openid-connect/auth")
                                    .setAccessTokenEndpoint("/auth/realms/ujuzy/protocol/openid-connect/token")
                                    .setAccountId("account")
                                    .setClientId("account")
                                    .setRedirectURL("https://ujuzy.com")
                                    .setScopes(Arrays.asList("openid"))
                                    .addAdditionalAuthorizationParam((Pair.create("grant_type", "password")))
                                    .asModule();  // send token to backend database.

                            authzModule.requestAccess(RequestServiceActivity.this, new org.jboss.aerogear.android.core.Callback<String>() {
                                @Override
                                public void onSuccess(final String data) {

                                    //SAVE TOKEN TO REALM DATABASE
                                    RealmToken token = new RealmToken();
                                    token.setToken(data);

                                    realm = Realm.getDefaultInstance();
                                    RealmTokenHelper helper = new RealmTokenHelper(realm);
                                    helper.save(token);

                                    //requestService(data, name, phone, date, serviceId, time, request);
                                    StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.POST, JSON_URL, new com.android.volley.Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {


                                        }
                                    }, new com.android.volley.Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {

                                        }
                                    }) {
                                        @Override
                                        protected Map<String,String> getParams(){
                                            Map<String,String> params = new HashMap<String, String>();
                                            params.put("name",name);
                                            params.put("phone_number",phone);
                                            params.put("date", date);
                                            params.put("service_id",serviceId);
                                            params.put("time",time);
                                            params.put("request_info",request);

                                            return params;
                                        }

                                        @Override
                                        public Map<String, String> getHeaders() throws AuthFailureError {
                                            Map<String,String> params = new HashMap<String, String>();
                                            params.put("Authorization",data);
                                            params.put("Content-Type","application/x-www-form-urlencoded");
                                            return params;
                                        }
                                    };

                                    requestQueue = Volley.newRequestQueue(RequestServiceActivity.this);
                                    requestQueue.add(stringRequest);

                                    postUsingVolley();


                                }

                                @Override
                                public void onFailure(Exception e) {
                                    System.err.println("Error!!");
                                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });


                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }

                    }

                }

            }
        });
    }

    private void postUsingVolley()
    {

       /* StringRequest request = new StringRequest(Request.Method.POST, Constants.HTTP.BASE_URL,
                new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.equals(null)) {
                    Log.e("Your Array Response", response);
                } else {
                    Log.e("Your Array Response", "Data Null");
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error is ", "" + error);
            }
        }) {

            //This is for Headers If You Needed
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json; charset=UTF-8");
                params.put("token", ACCESS_TOKEN);
                return params;
            }

            //Pass Your Parameters here
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("User", UserName);
                params.put("Pass", PassWord);
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);*/
    }

    private void initServiceInfo()
    {

        userAvatar = (ImageView) findViewById(R.id.iv_avatar);
        serviceNameTv = (TextView) findViewById(R.id.tv_service_name);
        userFullName = (TextView) findViewById(R.id.tv_user_name);
        noOfPersonnel = (TextView) findViewById(R.id.tv_service_no_personnel);
        serviceCostTv = (TextView) findViewById(R.id.tv_service_cost);

        serviceId = getIntent().getStringExtra("service_id");
        serviceName = getIntent().getStringExtra("service_name");

        serviceCost = getIntent().getStringExtra("service_cost");
        no_of_personnel = getIntent().getStringExtra("no_of_personnel");

        first_name = getIntent().getStringExtra("first_name");
        last_name = getIntent().getStringExtra("last_name");
        profile_pic = getIntent().getStringExtra("profile_pic");

        serviceNameTv = (TextView) findViewById(R.id.tv_service_name);

        serviceNameTv.setText(serviceName);
        if (no_of_personnel != null)
        noOfPersonnel.setText( no_of_personnel);
        if (serviceCost != null)
        serviceCostTv.setText( serviceCost);
        if (first_name != null || last_name != null)
        userFullName.setText( first_name + " " + last_name);


        Glide.with(getApplicationContext())
                .load(profile_pic)
                .asBitmap()
                .placeholder(R.drawable.placeholder_image)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .fitCenter()
                .into(new BitmapImageViewTarget(userAvatar) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(getApplicationContext().getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        userAvatar.setImageDrawable(circularBitmapDrawable);
                    }
                });

    }

    private Retrofit getRetrofit()
    {
        if (this.retrofit == null)
        {
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            this.retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.HTTP.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return this.retrofit;
    }

    public void requestService(String token, String name, String phone, String date, String serviceId, String time, String info)
    {
        Api api = getRetrofit().create(Api.class);
        Call<Request> ServiceData =  api.requestSercive(token, name, phone,date ,serviceId, time, info);
        ServiceData.enqueue(new Callback<Request>() {
            @Override
            public void onResponse(Call<Request> call, Response<Request> response) {
                Request serviceResult = response.body();
                Toast.makeText(RequestServiceActivity.this, (CharSequence) serviceResult, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<Request> call, Throwable t) {

                Toast.makeText(RequestServiceActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }

    private void initWindows()
    {
        Window window = RequestServiceActivity.this.getWindow();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            window.setStatusBarColor(ContextCompat.getColor( RequestServiceActivity.this,R.color.colorPrimaryDark));

            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        }

    }

    private void initBackBtn()
    {
        backBtnIv = (ImageView) findViewById(R.id.backBtn);
        backBtnIv.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });
    }

}
