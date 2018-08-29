package com.ujuzy.ujuzy.Tabs;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
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
import com.ujuzy.ujuzy.R;
import com.ujuzy.ujuzy.Realm.RealmAllServiceAdapter;
import com.ujuzy.ujuzy.Realm.RealmToken;
import com.ujuzy.ujuzy.adapters.ReviewsAdapter;
import com.ujuzy.ujuzy.model.Constants;
import com.ujuzy.ujuzy.model.Datum;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmChangeListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class JobsFragment extends Fragment
{

    private ReviewsAdapter reviewsAdapter;
    private RecyclerView reviewsListRv;
    private static String BASE_URL = "https://api.ujuzy.com/";
    private String USER_JOB_REQUESTS_SERVICES_JSON_URL = "https://api.ujuzy.com/requests/my-jobs ";
    ArrayList<Datum> results;
    String reviews;
    String service_id = "";
    private ProgressBar progressBar;
    private TextView noService;

    private Realm realm;
    private RealmChangeListener realmChangeListener;
    private RealmAllServiceAdapter serviceRealmAdapter;
    private RequestQueue requestQueue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_reviews, container, false);

        reviewsListRv = (RecyclerView) v.findViewById(R.id.reviews_list);
        progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
        noService = (TextView) v.findViewById(R.id.noService);

        Bundle bundle2 = this.getArguments();
        if (bundle2 != null) {
            service_id = bundle2.getString("serviceId", null);
        }

        initProgessBar();
        initJobRequestsVolley();

        return v;
    }

    private void initJobRequestsVolley() {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                Constants.HTTP.USER_JOB_REQUESTS_SERVICES_JSON_URL, new JSONObject(),
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

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
                headers.put("Authorization","Bearer "+ token.toString());
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("Accept","application/json");
                return headers;
            }

        };

        // Adding request to request queue
        requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(jsonObjReq);
    }

    private void initProgessBar()
    {

        noService.setVisibility(View.VISIBLE);
        noService.setText("Your job requests will be colleced here!");
    }

    private void viewData()
    {
        reviewsAdapter = new ReviewsAdapter(getActivity(), results);

        //RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        final LinearLayoutManager serviceLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        reviewsListRv.setLayoutManager(serviceLayoutManager);
        reviewsListRv.setAdapter(reviewsAdapter);
    }
}
