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
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ujuzy.ujuzy.R;
import com.ujuzy.ujuzy.Realm.RealmAllServiceAdapter;
import com.ujuzy.ujuzy.Realm.RealmHelper;
import com.ujuzy.ujuzy.Realm.RealmPhotoAdapter;
import com.ujuzy.ujuzy.Realm.RealmService;
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


        initRealm();
        return v;
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
