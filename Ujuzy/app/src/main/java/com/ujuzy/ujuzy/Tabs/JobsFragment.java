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
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ujuzy.ujuzy.R;
import com.ujuzy.ujuzy.Realm.RealmAllServiceAdapter;
import com.ujuzy.ujuzy.Realm.RealmToken;
import com.ujuzy.ujuzy.adapters.ReviewsAdapter;
import com.ujuzy.ujuzy.model.Datum;

import org.json.JSONException;
import org.json.JSONObject;

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
        StringRequest stringRequest = new StringRequest(Request.Method.GET, USER_JOB_REQUESTS_SERVICES_JSON_URL, new com.android.volley.Response.Listener<String>() {
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

            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                realm = Realm.getDefaultInstance();
                RealmToken token = realm.where(RealmToken.class).findFirst();

                Map<String,String> params = new HashMap<String, String>();
                params.put("Authorization","Bearer "+ token);
                params.put("Content-Type","application/json");
                params.put("Accept","application/json");
                return params;
            }
        };

        requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
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
