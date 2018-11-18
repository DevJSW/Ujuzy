package com.ujuzy.ujuzy.ujuzy.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.View;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ujuzy.ujuzy.ujuzy.R;
import com.ujuzy.ujuzy.ujuzy.Realm.RealmRequestedServiceAdapter;
import com.ujuzy.ujuzy.ujuzy.Realm.RealmRequestedServicesHelper;
import com.ujuzy.ujuzy.ujuzy.Realm.RealmRequestedUserService;
import com.ujuzy.ujuzy.ujuzy.Realm.RealmServiceImage;
import com.ujuzy.ujuzy.ujuzy.Realm.RealmToken;
import com.ujuzy.ujuzy.ujuzy.model.Constants;

import org.jboss.aerogear.android.authorization.AuthorizationManager;
import org.jboss.aerogear.android.authorization.AuthzModule;
import org.jboss.aerogear.android.authorization.oauth2.OAuth2AuthorizationConfiguration;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmList;

public class UserRequestServiceActivity extends AppCompatActivity
{
    private Realm realm;
    private RealmChangeListener realmChangeListener;
    private RealmRequestedServiceAdapter serviceRealmAdapter;
    private RecyclerView serviceListRv;
    private RequestQueue requestQueue;
    RealmList<RealmServiceImage> serviceImages;
    String category_title = "";
    private TextView noService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_request_service);
        noService = (TextView) findViewById(R.id.noService);
        serviceListRv = (RecyclerView) findViewById(R.id.service_list);
        getUserReqServices();
    }

    private void getUserReqServices()
    {
        realm = Realm.getDefaultInstance();
        final RealmRequestedServicesHelper helper = new RealmRequestedServicesHelper(realm);

        //RETRIEVE
        helper.retreiveFromDB();

        //CHECK IF DATABASE IS EMPTY
        if (helper.refreshDatabase().size() < 1 || helper.refreshDatabase().size() == 0)
        {

            noService.setVisibility(View.VISIBLE);

        } else {

            noService.setVisibility(View.GONE);
        }

        serviceRealmAdapter = new RealmRequestedServiceAdapter(UserRequestServiceActivity.this, helper.refreshDatabase());

        //RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        final LinearLayoutManager serviceLayoutManager = new LinearLayoutManager(UserRequestServiceActivity.this, LinearLayoutManager.VERTICAL, false);
        serviceListRv.setLayoutManager(serviceLayoutManager);
        serviceListRv.setAdapter(serviceRealmAdapter);

        //HANDLE DATA CHANGE FOR REFRESH
        realmChangeListener = new RealmChangeListener()
        {
            @Override
            public void onChange(Object o) {
                //REFRESH
                serviceRealmAdapter = new RealmRequestedServiceAdapter(UserRequestServiceActivity.this, helper.refreshDatabase());
                serviceListRv.setAdapter(serviceRealmAdapter);
            }
        };

        //ADD CHANGE LIST TO REALM
        realm = Realm.getDefaultInstance();
        realm.addChangeListener(realmChangeListener);
    }

    private void initUserJobsFromApi()
    {
        final AuthzModule authzModule;
        try {
            authzModule = AuthorizationManager
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

            authzModule.requestAccess(UserRequestServiceActivity.this, new org.jboss.aerogear.android.core.Callback<String>() {
                @Override
                public void onSuccess(final String data) {

                    JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                            Constants.HTTP.USER_REQUESTS_SERVICES_JSON_URL, new JSONObject(),
                            new com.android.volley.Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {

                                    try {

                                        JSONArray serviceData = response.getJSONArray("data");

                                        for (int i = 0 ; i < serviceData.length() ; i++) {

                                            JSONObject requestObj = serviceData.getJSONObject(i);
                                            JSONObject serviceObj = serviceData.getJSONObject(i).getJSONObject("service");

                                            RealmRequestedUserService realmService = new RealmRequestedUserService();
                                            realmService.setId(requestObj.getString("id"));
                                            realmService.setName(requestObj.getString("name"));
                                            realmService.setPhone(requestObj.getString("phone_number"));
                                            realmService.setRequest_date(requestObj.getString("date"));
                                            realmService.setRequest_time(requestObj.getString("time"));
                                            realmService.setSeen_status(requestObj.getString("seen_status"));
                                            realmService.setCreated_at(requestObj.getString("created_at"));
                                            realmService.setCreated_by(requestObj.getString("created_by"));
                                            realmService.setUpdated_by(requestObj.getString("updated_by"));
                                            realmService.setSkill_request(requestObj.getString("skill_request"));
                                            realmService.setSpecal_request(requestObj.getString("special_request"));

                                            realmService.setImage(serviceObj.getString("thumb"));
                                            realmService.setServiceName(serviceObj.getString("service_name"));
                                            realmService.setServiceId(serviceObj.getString("service_id"));

                                            //SAVE
                                            realm = Realm.getDefaultInstance();
                                            RealmRequestedServicesHelper helper = new RealmRequestedServicesHelper(realm);
                                            helper.save(realmService);

                                            noService.setVisibility(View.GONE);

                                        }


                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            NetworkResponse response = error.networkResponse;
                            if (error instanceof ServerError && response != null) {
                                try {
                                    String res = new String(response.data,
                                            HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                                    // Now you can use any deserializer to make sense of data
                                    JSONObject obj = new JSONObject(res);
                                } catch (UnsupportedEncodingException e1) {
                                    // Couldn't properly decode data to string
                                    e1.printStackTrace();
                                } catch (JSONException e2) {
                                    // returned data is not JSONObject?
                                    e2.printStackTrace();
                                }
                            }
                        }

                    }) {

                        /**
                         * Passing some request headers
                         * */
                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {

                            realm = Realm.getDefaultInstance();
                            RealmToken token = realm.where(RealmToken.class).findFirst();

                            HashMap<String, String> headers = new HashMap<String, String>();
                            headers.put("Authorization","Bearer "+ data);
                            headers.put("Content-Type", "application/json; charset=utf-8");
                            headers.put("Accept","application/json");
                            return headers;
                        }

                    };

                    // Adding request to request queue
                    requestQueue = Volley.newRequestQueue(UserRequestServiceActivity.this);
                    requestQueue.add(jsonObjReq);

                }

                @Override
                public void onFailure(Exception e) {
                    // Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });


        } catch (MalformedURLException e) {
            e.printStackTrace();
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
