package com.ujuzy.ujuzy.Tabs;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ujuzy.ujuzy.R;
import com.ujuzy.ujuzy.Realm.RealmAllServiceAdapter;
import com.ujuzy.ujuzy.Realm.RealmHelper;
import com.ujuzy.ujuzy.Realm.RealmRequestedServiceAdapter;
import com.ujuzy.ujuzy.Realm.RealmRequestedServicesHelper;
import com.ujuzy.ujuzy.Realm.RealmRequestedUserService;
import com.ujuzy.ujuzy.Realm.RealmServiceAdapter;
import com.ujuzy.ujuzy.Realm.RealmToken;
import com.ujuzy.ujuzy.Realm.RealmUser;
import com.ujuzy.ujuzy.Realm.RealmUserHelper;
import com.ujuzy.ujuzy.Realm.RealmUserService;
import com.ujuzy.ujuzy.Realm.RealmUserServiceAdapter;
import com.ujuzy.ujuzy.Realm.RealmUserServicesHelper;
import com.ujuzy.ujuzy.activities.UserProfileActivity;
import com.ujuzy.ujuzy.adapters.CountryAdapter;
import com.ujuzy.ujuzy.model.Constants;

import org.jboss.aerogear.android.authorization.AuthorizationManager;
import org.jboss.aerogear.android.authorization.AuthzModule;
import org.jboss.aerogear.android.authorization.oauth2.OAuth2AuthorizationConfiguration;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import retrofit2.Retrofit;

import static io.realm.internal.SyncObjectServerFacade.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserRequestedServiceFragment extends Fragment {

    String serviceId = "";
    String userId = "";
    String firstName = "";
    private CountryAdapter countryAdapter;
    private RecyclerView serviceListRv;
    private static String BASE_URL = "https://api.ujuzy.com/";
    ArrayList<RealmUserService> results;
    private RealmAllServiceAdapter serviceAdapter;
    private ProgressBar progressBar;
    private TextView noService, editTv;

    private Realm realm;
    private RealmChangeListener realmChangeListener;
    private RealmRequestedServiceAdapter serviceRealmAdapter;
    private RequestQueue requestQueue;

    private Retrofit retrofit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_requested_services, container, false);

        serviceListRv = (RecyclerView) v.findViewById(R.id.service_list);
        progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
        noService = (TextView) v.findViewById(R.id.noService);

        serviceListRv = (RecyclerView) v.findViewById(R.id.service_list);
        progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
        noService = (TextView) v.findViewById(R.id.noService);
        editTv = (TextView) v.findViewById(R.id.tvEdit);

        Bundle bundle2 = this.getArguments();
        if (bundle2 != null) {
            serviceId = bundle2.getString("serviceId", null);
            userId = bundle2.getString("userId", null);
            firstName = bundle2.getString("firstName", null);
        }

        //getServicesFromRealm();

        /**
         *  getting services by user id
         */


        noService.setVisibility(View.VISIBLE);

        initUserRequest();
        getUserReqServices();

        return v;
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

        serviceRealmAdapter = new RealmRequestedServiceAdapter(getActivity(), helper.refreshDatabase());

        //RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        final LinearLayoutManager serviceLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        serviceListRv.setLayoutManager(serviceLayoutManager);
        serviceListRv.setAdapter(serviceRealmAdapter);

        //HANDLE DATA CHANGE FOR REFRESH
        realmChangeListener = new RealmChangeListener()
        {
            @Override
            public void onChange(Object o) {
                //REFRESH
                serviceRealmAdapter = new RealmRequestedServiceAdapter(getActivity(), helper.refreshDatabase());
                serviceListRv.setAdapter(serviceRealmAdapter);
            }
        };

        //ADD CHANGE LIST TO REALM
        realm = Realm.getDefaultInstance();
        realm.addChangeListener(realmChangeListener);
    }

    private void initUserRequest()
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

            authzModule.requestAccess(getActivity(), new org.jboss.aerogear.android.core.Callback<String>() {
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
                    requestQueue = Volley.newRequestQueue(getActivity());
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

}
