package com.ujuzy.ujuzy.ujuzy.Activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Bitmap;
import android.os.Build;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ujuzy.ujuzy.ujuzy.R;
import com.ujuzy.ujuzy.ujuzy.Realm.RealmToken;
import com.ujuzy.ujuzy.ujuzy.Realm.RealmTokenHelper;
import com.ujuzy.ujuzy.ujuzy.Services.Api;
import com.ujuzy.ujuzy.ujuzy.model.Constants;

import org.jboss.aerogear.android.authorization.AuthorizationManager;
import org.jboss.aerogear.android.authorization.AuthzModule;
import org.jboss.aerogear.android.authorization.oauth2.OAuth2AuthorizationConfiguration;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import io.realm.Realm;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RequestServiceActivity extends AppCompatActivity {

    String serviceCost = "";
    String serviceName = "";
    String serviceId = "";
    String first_name = "";
    String last_name = "";
    String profile_pic = "";
    String no_of_personnel = "";
    String date = "";
    String time = "";
    String request_input = "";
    String name = "";
    String info = "";
    String phone = "";

    private TextView serviceNameTv, userFullName, noOfPersonnel, serviceCostTv;
    private ImageView userAvatar, backBtnIv;
    private EditText inputDateEt, inputRequestEt, inputPhone, inputName, inputTimeEt;
    private Button confirmBtn;
    private RelativeLayout mDateRl, mTimeRl;

    private Realm realm;
    private Retrofit retrofit;

    Calendar myCalender;
    Calendar myTime;

    //json volley
    private JsonArrayRequest request;
    private RequestQueue requestQueue;

    private int mHour, mMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_service);

        confirmBtn = (Button) findViewById(R.id.btn_request);
        inputDateEt = (EditText) findViewById(R.id.inputDateTxt);
        inputTimeEt = (EditText) findViewById(R.id.inputTimeTxt);
        inputRequestEt = (EditText) findViewById(R.id.inputRequestTxt);
        inputPhone = (EditText) findViewById(R.id.inputPhoneTxt);
        inputName = (EditText) findViewById(R.id.inputNameTxt);
        mDateRl = (RelativeLayout) findViewById(R.id.date);
        mTimeRl = (RelativeLayout) findViewById(R.id.time);

        initWindows();
        initServiceInfo();
        initBackBtn();
        initConfirmBtn();
        initDatePickerDialog();
        intTimePickerDialog();
    }

    private void intTimePickerDialog()
    {
        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {

                        inputTimeEt.setText(hourOfDay + ":" + minute);
                    }
                }, mHour, mMinute, false);

        inputTimeEt.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // Get Current Time
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                timePickerDialog.show();

            }
        });
    }


    private void initDatePickerDialog()
    {
        inputDateEt.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                myCalender = Calendar.getInstance();
                DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        myCalender.set(Calendar.YEAR, year);
                        myCalender.set(Calendar.YEAR, year);
                        myCalender.set(Calendar.MONTH, month);
                        myCalender.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        updateLabel();
                    }
                };

                new DatePickerDialog(RequestServiceActivity.this, date, myCalender
                        .get(Calendar.YEAR), myCalender
                        .get(Calendar.MONTH), myCalender
                        .get(Calendar.DAY_OF_MONTH))
                        .show();

            }
        });
    }

    private void updateLabel()
    {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);
        inputDateEt.setText(sdf.format(myCalender.getTime()));
    }

    private void initConfirmBtn() {

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startRequest();

            }
        });
    }

    private void startRequest()
    {

        date = inputDateEt.getText().toString();
        request_input = inputRequestEt.getText().toString();
        time = inputTimeEt.getText().toString();
        phone = inputPhone.getText().toString();
        name = inputName.getText().toString();

        if (TextUtils.isEmpty(date)) {
            inputDateEt.setError("Enter time and date most suitable for you");
        } else if (TextUtils.isEmpty(request_input)){
            inputRequestEt.setError("Describe your request!");
        } else {

            realm = Realm.getDefaultInstance();
            final RealmTokenHelper helper = new RealmTokenHelper(realm);

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

                authzModule.requestAccess(RequestServiceActivity.this, new org.jboss.aerogear.android.core.Callback<String>() {
                    @Override
                    public void onSuccess(final String data) {

                        //SAVE TOKEN TO REALM DATABASE
                        RealmToken token = new RealmToken();
                        token.setToken(data);

                        //initRetrofit();

                        Map<String, String> params= new HashMap<String, String>();
                        params.put("contact_name",name);
                        params.put("phone_number",phone);
                        params.put("date", date);
                        params.put("service_id",serviceId);
                        params.put("time", time);
                        params.put("request_info", request_input);

                        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                                Constants.HTTP.REQUEST_SERVICE_JSON_URL, new JSONObject(params),
                                new com.android.volley.Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response)
                                    {

                                        Toast.makeText(RequestServiceActivity.this, "Request sent successfully", Toast.LENGTH_LONG).show();

                                    }
                                }, new com.android.volley.Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error)
                            {
                                Toast.makeText(RequestServiceActivity.this, error.toString(), Toast.LENGTH_LONG).show();
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
                        requestQueue = Volley.newRequestQueue(RequestServiceActivity.this);
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


    private void initServiceInfo()
    {

        userAvatar = (ImageView) findViewById(R.id.iv_avatar);
        serviceNameTv = (TextView) findViewById(R.id.tv_service_name);
        userFullName = (TextView) findViewById(R.id.tv_user_name);
        noOfPersonnel = (TextView) findViewById(R.id.tv_service_no_personnel);
        serviceCostTv = (TextView) findViewById(R.id.tv_service_cost);
        serviceNameTv = (TextView) findViewById(R.id.tv_service_name);

        serviceId = getIntent().getStringExtra("service_id");
        serviceName = getIntent().getStringExtra("service_name");

        serviceCost = getIntent().getStringExtra("service_cost");
        no_of_personnel = getIntent().getStringExtra("no_of_personnel");

        first_name = getIntent().getStringExtra("first_name");
        last_name = getIntent().getStringExtra("last_name");
        profile_pic = getIntent().getStringExtra("profile_pic");


        serviceNameTv.setText(serviceName);
        if (no_of_personnel != null)
            noOfPersonnel.setText( no_of_personnel);
        if (serviceCost != null)
        if (serviceCost == null || serviceCost.equals("null")) {
            serviceCostTv.setText( "Ksh: Ask");
        } else {
            serviceCostTv.setText( "Ksh: "+ serviceCost);
        }
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
