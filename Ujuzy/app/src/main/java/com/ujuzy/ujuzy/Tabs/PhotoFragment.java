package com.ujuzy.ujuzy.Tabs;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ujuzy.ujuzy.R;
import com.ujuzy.ujuzy.Realm.RealmAllServiceAdapter;
import com.ujuzy.ujuzy.Realm.RealmHelper;
import com.ujuzy.ujuzy.Realm.RealmPhotoAdapter;
import com.ujuzy.ujuzy.Realm.RealmService;
import com.ujuzy.ujuzy.Realm.RealmSkillList;
import com.ujuzy.ujuzy.activities.MainActivity;
import com.ujuzy.ujuzy.adapters.ReviewsAdapter;
import com.ujuzy.ujuzy.model.Datum;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmChangeListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class PhotoFragment extends Fragment
{

    private RealmPhotoAdapter realmPhotoAdapter;
    private RecyclerView photoListRv;
    private static String BASE_URL = "https://api.ujuzy.com/";
    ArrayList<Datum> results;
    String reviews;
    String service_id = "";
    String service_url = "";
    private ProgressBar progressBar;
    private TextView noService;

    private Realm realm;
    private RealmChangeListener realmChangeListener;

    //json volley
    private final String JSON_URL = "https://api.ujuzy.com/services";
    private JsonArrayRequest request;
    private RequestQueue requestQueue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_photo, container, false);

        photoListRv = (RecyclerView) v.findViewById(R.id.photo_list);
        progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
        noService = (TextView) v.findViewById(R.id.noService);

        Bundle bundle2 = this.getArguments();
        if (bundle2 != null) {
            service_id = bundle2.getString("serviceId", null);
            service_url = bundle2.getString("serviceUrl", null);
        }

        initProgessBar();

       // getJsonResponse();

        initRealm();
        return v;
    }

    private void getJsonResponse()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                JSON_URL, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    for (int i = 0 ; i < jsonArray.length() ; i++)
                    {

                        JSONObject serviceObj = jsonArray.getJSONObject(i);
                        JSONObject serviceImgObj = jsonArray.getJSONObject(i).getJSONObject("images");
                        JSONObject serviceUserObj = jsonArray.getJSONObject(i).getJSONObject("user");
                        JSONObject serviceLocationObj = jsonArray.getJSONObject(i).getJSONObject("location");
                        JSONObject serviceDurationObj = jsonArray.getJSONObject(i).getJSONObject("duration");

                        JSONArray skillList = serviceObj.getJSONArray("skill_list");

                        // ASSIGN DATA TO REALM DATABASE SERVICE

                        RealmService realmService = new  RealmService();
                        realmService.setId(serviceObj.getString("id"));
                        realmService.setServiceName(serviceObj.getString("service_name"));
                        realmService.setServiceDetails(serviceObj.getString("service_details"));
                        realmService.setCost(serviceObj.getString("cost"));
                        realmService.setCreatedBy(serviceObj.getString("created_by"));
                        realmService.setCategory(serviceObj.getString("category"));
                        realmService.setCreated_at(serviceObj.getString("created_at"));
                        realmService.setImage(serviceImgObj.getString("thumb"));

                        realmService.setUser_role(serviceUserObj.getString("user_role"));
                        realmService.setUser_thumb(serviceUserObj.getString("profile_pic"));
                        realmService.setFirst_name(serviceUserObj.getString("firstname"));
                        realmService.setLast_name(serviceUserObj.getString("lastname"));
                        realmService.setUser_id(serviceUserObj.getString("id"));

                        realmService.setCity(serviceLocationObj.getString("city"));

                        realmService.setService_duration_days(serviceDurationObj.getString("days"));
                        realmService.setService_duration_hours(serviceDurationObj.getString("hours"));

                        for (int s = 0; s < skillList.length() ; s++) {

                            JSONObject skillObj = skillList.getJSONObject(i);

                            RealmSkillList realmSkillList = new RealmSkillList();
                            realmSkillList.setServiceName(skillObj.getString("service_name"));
                            realmSkillList.setId(skillObj.getString("id"));
                            realmSkillList.setBilling(skillObj.getString("billing"));
                            realmSkillList.setCost(skillObj.getString("cost"));
                            realmSkillList.setNoOfPersonnel(skillObj.getInt("no_of_personnel"));

                        }

                        //SAVE
                        realm = Realm.getDefaultInstance();
                        RealmHelper helper = new RealmHelper(realm);
                        helper.save(realmService);


                        /**************************** END ******************************/

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }


    private void initRealm()
    {
        realm = Realm.getDefaultInstance();
        final RealmHelper helper = new RealmHelper(realm);

        //QUERY/FILTER REALM DATABASE
        helper.filterRealmDatabase("image",service_url);

        //CHECK IF DATABASE IS EMPTY
        if (helper.refreshDatabase().size() < 1 || helper.refreshDatabase().size() == 0)
        {
            noService.setVisibility(View.VISIBLE);
            noService.setText("This service has no photo's posted yet!");
        } else {

            noService.setVisibility(View.GONE);

        }

        realmPhotoAdapter = new RealmPhotoAdapter(getActivity(), helper.refreshDatabase());

        //RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        final LinearLayoutManager serviceLayoutManager = new GridLayoutManager(getActivity(), 3);
        photoListRv.setLayoutManager(serviceLayoutManager);
        photoListRv.setAdapter(realmPhotoAdapter);

        //HANDLE DATA CHANGE FOR REFRESH
        realmChangeListener = new RealmChangeListener()
        {
            @Override
            public void onChange(Object o) {
                //REFRESH
                realmPhotoAdapter = new RealmPhotoAdapter(getActivity(), helper.refreshDatabase());
                photoListRv.setAdapter(realmPhotoAdapter);
            }
        };

        //ADD CHANGE LIST TO REALM
        realm.addChangeListener(realmChangeListener);
    }


    private void initProgessBar()
    {

        noService.setVisibility(View.VISIBLE);
        noService.setText("No reviews posted yet!");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.removeChangeListener(realmChangeListener);
        realm.close();
    }

}
