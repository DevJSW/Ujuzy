package com.ujuzy.ujuzy.ujuzy.BottomSheetFragments;


import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ujuzy.ujuzy.ujuzy.Activities.MapsActivity;
import com.ujuzy.ujuzy.ujuzy.R;
import com.ujuzy.ujuzy.ujuzy.Realm.RealmHelper;
import com.ujuzy.ujuzy.ujuzy.Realm.RealmService;
import com.ujuzy.ujuzy.ujuzy.Realm.RealmServiceAdapter;
import com.ujuzy.ujuzy.ujuzy.Realm.RealmServiceImage;
import com.ujuzy.ujuzy.ujuzy.Realm.RealmUserService;
import com.ujuzy.ujuzy.ujuzy.Realm.RealmUserServiceAdapter;
import com.ujuzy.ujuzy.ujuzy.Realm.RealmUserServicesHelper;
import com.ujuzy.ujuzy.ujuzy.model.Constants;

import org.jboss.aerogear.android.authorization.AuthorizationManager;
import org.jboss.aerogear.android.authorization.AuthzModule;
import org.jboss.aerogear.android.authorization.oauth2.OAuth2AuthorizationConfiguration;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmList;

import static io.realm.internal.SyncObjectServerFacade.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyServicesFragments extends BottomSheetDialogFragment {

    private Realm realm;
    private RealmChangeListener realmChangeListener;
    private RealmServiceAdapter serviceReamAdapter;
    private RecyclerView serviceListRv;
    private RequestQueue requestQueue;
    private RealmUserServiceAdapter userServiceReamAdapter;
    RealmList<RealmServiceImage> serviceImages;
    String category_title = "";
    private TextView noService;
    View v;

    public MyServicesFragments() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_my_services, container, false);

        serviceListRv = (RecyclerView) v.findViewById(R.id.service_list);
        noService = (TextView) v.findViewById(R.id.noService22);

        initUserServicesFromApi();
        return v;
    }


    private void initUserServicesFromApi()
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


            authzModule.requestAccess(getActivity(), new org.jboss.aerogear.android.core.Callback<String>()
            {
                @Override
                public void onSuccess(final String data)
                {

                    JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                            Constants.HTTP.USER_SERVICES_JSON_URL, new JSONObject(),
                            new com.android.volley.Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response)
                                {

                                    try
                                    {

                                        JSONArray serviceData = response.getJSONArray("data");

                                        for (int i = 0 ; i < serviceData.length() ; i++)
                                        {

                                            JSONObject requestObj = serviceData.getJSONObject(i);
                                            JSONObject serviceUserObj = serviceData.getJSONObject(i).getJSONObject("created_by");
                                            JSONObject serviceLocationObj = serviceData.getJSONObject(i).getJSONObject("location");
                                            JSONObject serviceDurationObj = serviceData.getJSONObject(i).getJSONObject("duration");

                                            RealmUserService realmService = new RealmUserService();
                                            realmService.setId(requestObj.getString("id"));
                                            realmService.setServiceName(requestObj.getString("service_name"));
                                            realmService.setServiceDetails(requestObj.getString("service_details"));
                                            realmService.setCost(requestObj.getString("cost"));
                                            realmService.setCategory(requestObj.getString("category"));
                                            realmService.setTravel(requestObj.getBoolean("travel"));
                                            realmService.setCreated_at(requestObj.getString("created_at"));
                                            // realmService.setRating(requestObj.getString("rating"));

                                            String img = requestObj.getString("images");

                                            Object json = new JSONTokener(img).nextValue();

                                            if (json instanceof JSONObject)
                                            {

                                                JSONObject serviceImgObj = serviceData.getJSONObject(i).getJSONObject("images");
                                                realmService.setImage(serviceImgObj.getString("thumb"));

                                            } else if (json instanceof JSONArray)
                                            {

                                                JSONArray arrayImages = requestObj.getJSONArray("images");
                                                for (int j = 0; j < arrayImages.length(); j++)
                                                {
                                                    JSONObject imagesObj = arrayImages.getJSONObject(j);
                                                    realmService.setImage(imagesObj.getString("thumb"));
                                                }

                                            }


                                            //realmService.setUser_role(serviceUserObj.getString("user_role"));
                                            realmService.setUser_thumb(serviceUserObj.getString("thumb"));
                                            realmService.setFirst_name(serviceUserObj.getString("firstname"));
                                            realmService.setLast_name(serviceUserObj.getString("lastname"));
                                            realmService.setUser_id(serviceUserObj.getString("id"));

                                            realmService.setCity(serviceLocationObj.getString("city"));

                                            realmService.setService_duration_days(serviceDurationObj.getString("days"));
                                            realmService.setService_duration_hours(serviceDurationObj.getString("hours"));


                                            //SAVE
                                            realm = Realm.getDefaultInstance();
                                            RealmUserServicesHelper helper = new RealmUserServicesHelper(realm);
                                            helper.save(realmService);

                                            noService.setVisibility(View.GONE);
                                            //createBt.setVisibility(View.GONE);

                                            getUserServices();

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
            //e.printStackTrace();
        }

    }

    private void getUserServices() {
        realm = Realm.getDefaultInstance();
        final RealmUserServicesHelper helper = new RealmUserServicesHelper(realm);

        //RETRIEVE
        helper.retreiveFromDB();

        //CHECK IF DATABASE IS EMPTY
        if (helper.refreshDatabase().size() < 1 || helper.refreshDatabase().size() == 0) {

            /*noMyServices = (TextView) findViewById(R.id.noService2);
            noMyServices.setVisibility(View.VISIBLE);*/

        } else {

           /* noMyServices = (TextView) findViewById(R.id.noService2);
            noMyServices.setVisibility(View.GONE);*/
        }

        userServiceReamAdapter = new RealmUserServiceAdapter(getActivity(), helper.refreshDatabase());

        serviceListRv = (RecyclerView) v.findViewById(R.id.myservice_list);
        //RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        final LinearLayoutManager serviceLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        serviceListRv.setLayoutManager(serviceLayoutManager);
        serviceListRv.setAdapter(userServiceReamAdapter);

        //HANDLE DATA CHANGE FOR REFRESH
        realmChangeListener = new RealmChangeListener() {
            @Override
            public void onChange(Object o) {
                //REFRESH
                userServiceReamAdapter = new RealmUserServiceAdapter(getActivity(), helper.refreshDatabase());
                serviceListRv.setAdapter(userServiceReamAdapter);
            }
        };

        //ADD CHANGE LIST TO REALM
        realm = Realm.getDefaultInstance();
        realm.addChangeListener(realmChangeListener);
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
