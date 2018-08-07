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
import com.ujuzy.ujuzy.Realm.RealmHelper;
import com.ujuzy.ujuzy.Realm.RealmPhotoAdapter;
import com.ujuzy.ujuzy.Realm.RealmService;
import com.ujuzy.ujuzy.Realm.RealmSkillList;
import com.ujuzy.ujuzy.adapters.SkillsAdapter;
import com.ujuzy.ujuzy.model.Datum;
import com.ujuzy.ujuzy.model.RetrofitInstance;
import com.ujuzy.ujuzy.model.Service;
import com.ujuzy.ujuzy.model.SkillList;
import com.ujuzy.ujuzy.services.Api;
import com.ujuzy.ujuzy.services.ServiceInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class SkillsFragment extends Fragment
{

    private SkillsAdapter skillsAdapter;
    private RecyclerView skillListRv;
    private static String BASE_URL = "https://api.ujuzy.com/";
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

    private List<SkillList> skillItems;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_skills, container, false);

        skillListRv = (RecyclerView) v.findViewById(R.id.skills_list);
        progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
        noService = (TextView) v.findViewById(R.id.noService);

        skillItems = new ArrayList<>();

        Bundle bundle2 = this.getArguments();
        if (bundle2 != null) {
            service_id = bundle2.getString("serviceId", null);
            service_url = bundle2.getString("serviceUrl", null);
        }

        initProgessBar();

        getJsonResponse();

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
                    JSONArray skillList = jsonObject.getJSONArray("skill_list");


                    for (int i = 0 ; i < jsonArray.length() ; i++)
                    {

                        JSONObject serviceObj = jsonArray.getJSONObject(i);
                        JSONObject serviceImgObj = jsonArray.getJSONObject(i).getJSONObject("images");
                        JSONObject serviceUserObj = jsonArray.getJSONObject(i).getJSONObject("user");
                        JSONObject serviceLocationObj = jsonArray.getJSONObject(i).getJSONObject("location");
                        JSONObject serviceDurationObj = jsonArray.getJSONObject(i).getJSONObject("duration");
                        JSONObject serviceSkilllistnObj = jsonArray.getJSONObject(i).getJSONObject("skill_list");


                        for (int s = 0; s < skillList.length() ; s++) {

                            JSONObject skillObj = skillList.getJSONObject(i);

                            SkillList skill = new SkillList(
                                    skillObj.getString("id"),
                                    skillObj.getString("service_name"),
                                    skillObj.getString("cost"),
                                    skillObj.getString("offer_cost"),
                                    skillObj.getString("billing"),
                                    skillObj.getInt("no_of_personnel")
                            );

                            skillItems.add(skill);

                        }

                        skillsAdapter = new SkillsAdapter(skillItems, getActivity());
                        skillListRv.setAdapter(skillsAdapter);

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

    private void initProgessBar()
    {

        noService.setVisibility(View.VISIBLE);
        noService.setText("No reviews posted yet!");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
