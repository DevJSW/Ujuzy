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
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ujuzy.ujuzy.R;
import com.ujuzy.ujuzy.Realm.RealmAllServiceAdapter;
import com.ujuzy.ujuzy.Realm.RealmHelper;
import com.ujuzy.ujuzy.Realm.RealmService;
import com.ujuzy.ujuzy.Realm.RealmServiceAdapter;
import com.ujuzy.ujuzy.Realm.RealmToken;
import com.ujuzy.ujuzy.Realm.RealmTokenHelper;
import com.ujuzy.ujuzy.Realm.RealmUser;
import com.ujuzy.ujuzy.Realm.RealmUserHelper;
import com.ujuzy.ujuzy.Realm.RealmUserService;
import com.ujuzy.ujuzy.Realm.RealmUserServiceAdapter;
import com.ujuzy.ujuzy.Realm.RealmUserServicesHelper;
import com.ujuzy.ujuzy.activities.EditProfileActivity;
import com.ujuzy.ujuzy.activities.MainActivity;
import com.ujuzy.ujuzy.activities.RequestServiceActivity;
import com.ujuzy.ujuzy.activities.UserProfileActivity;
import com.ujuzy.ujuzy.activities.WebViewActivity;
import com.ujuzy.ujuzy.adapters.CountryAdapter;
import com.ujuzy.ujuzy.adapters.SeeAllAdapter;
import com.ujuzy.ujuzy.adapters.ServiceAdapter;
import com.ujuzy.ujuzy.model.Constants;
import com.ujuzy.ujuzy.model.Datum;
import com.ujuzy.ujuzy.model.Service;
import com.ujuzy.ujuzy.services.Api;

import org.jboss.aerogear.android.authorization.AuthorizationManager;
import org.jboss.aerogear.android.authorization.AuthzModule;
import org.jboss.aerogear.android.authorization.oauth2.OAuth2AuthorizationConfiguration;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserServicesFragment extends Fragment {

    String serviceId = "";
    String userId = "";
    String firstName = "";
    private String webview_url = "https://ujuzy.com/services/create";
    private CountryAdapter countryAdapter;
    private RecyclerView serviceListRv;
    private static String BASE_URL = "https://api.ujuzy.com/";
    private String USER_SERVICES_JSON_URL = "https://api.ujuzy.com/users/my-services";
    ArrayList<RealmUserService> results;
    private RealmAllServiceAdapter serviceAdapter;
    private ProgressBar progressBar;
    private TextView noService, editTv;
    private Button createBt;

    private Realm realm;
    private RealmChangeListener realmChangeListener;
    private RealmAllServiceAdapter serviceRealmAdapter;

    private Retrofit retrofit;
    private RequestQueue requestQueue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_services, container, false);

        serviceListRv = (RecyclerView) v.findViewById(R.id.service_list);
        progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
        noService = (TextView) v.findViewById(R.id.noService);
        editTv = (TextView) v.findViewById(R.id.tvEdit);
        createBt = (Button) v.findViewById(R.id.createServiceBt);

        Bundle bundle2 = this.getArguments();
        if (bundle2 != null) {
            serviceId = bundle2.getString("serviceId", null);
            userId = bundle2.getString("userId", null);
            firstName = bundle2.getString("firstName", null);
        }

       // getServicesFromRealm();

        /**
         *  getting services by user id
         */


        noService.setVisibility(View.VISIBLE);
        //initRealm();
        initUserServices();
        //getServicesFromRealm();

        initRealm();
        initCreateBt();
        initRealm();

        return v;
    }

    private Retrofit getRetrofit()
    {

        //GET TOKEN FROM REALM
        realm = Realm.getDefaultInstance();
        RealmToken realmToken = realm.where(RealmToken.class).findFirst();
        String token = realmToken.getToken().toString();

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        httpClient.addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                okhttp3.Request request = chain.request()
                        .newBuilder()
                        .addHeader("Authorization","Bearer "+ token)
                        .addHeader("Content-Type","application/json")
                        .addHeader("Accept","application/json")
                        .build();
                return chain.proceed(request);
            }
        });

        if (this.retrofit == null)
        {
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            this.retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.HTTP.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(httpClient.build())
                    .build();
        }
        return this.retrofit;
    }

    private void initRetrofit() {

        Api api = getRetrofit().create(Api.class);
        Call<Service> ServiceData =  api.getUserServices();
        ServiceData.enqueue(new Callback<Service>() {
            @Override
            public void onResponse(Call<Service> call, retrofit2.Response<Service> response) {

                Service resp = response.body();

            }

            @Override
            public void onFailure(Call<Service> call, Throwable t) {


                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }


    private void initCreateBt() {
        createBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent webView = new Intent(getActivity(), WebViewActivity.class);
                webView.putExtra("webview_url", webview_url);
                startActivity(webView);
            }
        });
    }

    private void initRealm()
    {
        realm = Realm.getDefaultInstance();
        final RealmHelper helper = new RealmHelper(realm);

        RealmUser realmUser = realm.where(RealmUser.class).findFirst();

        //QUERY/FILTER REALM DATABASE
        helper.filterRealmDatabase("user_id", realmUser.getId());

        //CHECK IF DATABASE IS EMPTY
        if (helper.refreshDatabase().size() < 1 || helper.refreshDatabase().size() == 0)
        {
            noService.setVisibility(View.VISIBLE);
            createBt.setVisibility(View.VISIBLE);
            noService.setText("You have no services posted yet!");
        } else {
            noService.setVisibility(View.GONE);
            createBt.setVisibility(View.GONE);
        }


        serviceRealmAdapter = new RealmAllServiceAdapter(getActivity(), helper.refreshDatabase());

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
                serviceRealmAdapter = new RealmAllServiceAdapter(getActivity(), helper.refreshDatabase());
                serviceListRv.setAdapter(serviceRealmAdapter);
            }
        };

        //ADD CHANGE LIST TO REALM
        realm.addChangeListener(realmChangeListener);
    }


    private void initUserServices()
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

            authzModule.requestAccess(getActivity(), new org.jboss.aerogear.android.core.Callback<String>() {
                @Override
                public void onSuccess(final String data) {

                    Map<String, String> params= new HashMap<String, String>();
                    params.put("new_role","company");

                    JsonObjectRequest jsonObjReq = new JsonObjectRequest(com.android.volley.Request.Method.POST,
                            Constants.HTTP.UPGRADE_PROFILE_JSON_URL, new JSONObject(params),
                            new com.android.volley.Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {


                                }
                            }, new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
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


                    StringRequest stringRequest = new StringRequest(Request.Method.GET, USER_SERVICES_JSON_URL, new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response)
                        {

                            try {

                                JSONObject jsonObject = new JSONObject(response);


                            } catch (JSONException e) {
                                //e.printStackTrace();
                            }

                        }
                    }, new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            initRetrofit();
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

                    requestQueue = Volley.newRequestQueue(getActivity());
                    requestQueue.add(stringRequest);
                }

                @Override
                public void onFailure(Exception e) {
                }
            });


        } catch (MalformedURLException e) {
            // e.printStackTrace();
        }


    }

   /* private void getServicesFromRealm()
    {
        realm = Realm.getDefaultInstance();
        final RealmUserServicesHelper helper = new RealmUserServicesHelper(realm);

        //RETRIEVE
        helper.retreiveFromDB();

        //CHECK IF DATABASE IS EMPTY
        if (helper.refreshDatabase().size() < 1 || helper.refreshDatabase().size() == 0)
        {
            noService.setVisibility(View.VISIBLE);

        } else {
            noService.setVisibility(View.GONE);
        }


        serviceRealmAdapter = new RealmUserServiceAdapter(getActivity(), results);
        final LinearLayoutManager serviceLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        serviceListRv.setLayoutManager(serviceLayoutManager);
        serviceListRv.setAdapter(serviceRealmAdapter);

        //HANDLE DATA CHANGE FOR REFRESH
        realmChangeListener = new RealmChangeListener()
        {
            @Override
            public void onChange(Object o) {
                //REFRESH
                serviceRealmAdapter = new RealmUserServiceAdapter(getActivity(), helper.refreshDatabase());
                serviceListRv.setAdapter(serviceAdapter);
            }
        };

        //ADD CHANGE LIST TO REALM
        realm.addChangeListener(realmChangeListener);
    }
*/

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (realmChangeListener != null)
        realm.removeChangeListener(realmChangeListener);
        //if (realm != null)
        //realm.close();
    }

}
