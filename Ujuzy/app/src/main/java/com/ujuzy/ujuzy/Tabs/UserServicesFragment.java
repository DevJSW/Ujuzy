package com.ujuzy.ujuzy.Tabs;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ujuzy.ujuzy.R;
import com.ujuzy.ujuzy.Realm.RealmAllServiceAdapter;
import com.ujuzy.ujuzy.Realm.RealmHelper;
import com.ujuzy.ujuzy.Realm.RealmService;
import com.ujuzy.ujuzy.Realm.RealmServiceAdapter;
import com.ujuzy.ujuzy.Realm.RealmUserService;
import com.ujuzy.ujuzy.Realm.RealmUserServiceAdapter;
import com.ujuzy.ujuzy.Realm.RealmUserServicesHelper;
import com.ujuzy.ujuzy.activities.EditProfileActivity;
import com.ujuzy.ujuzy.adapters.CountryAdapter;
import com.ujuzy.ujuzy.adapters.SeeAllAdapter;
import com.ujuzy.ujuzy.adapters.ServiceAdapter;
import com.ujuzy.ujuzy.model.Constants;
import com.ujuzy.ujuzy.model.Datum;
import com.ujuzy.ujuzy.model.Service;
import com.ujuzy.ujuzy.services.Api;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmChangeListener;
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
    private CountryAdapter countryAdapter;
    private RecyclerView serviceListRv;
    private static String BASE_URL = "https://api.ujuzy.com/";
    ArrayList<RealmUserService> results;
    private RealmAllServiceAdapter serviceAdapter;
    private ProgressBar progressBar;
    private TextView noService, editTv;

    private Realm realm;
    private RealmChangeListener realmChangeListener;
    private RealmUserServiceAdapter serviceRealmAdapter;

    private Retrofit retrofit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_services, container, false);

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

       // getServicesFromRealm();

        /**
         *  getting services by user id
         */


        noService.setVisibility(View.VISIBLE);
        //initRealm();

        return v;
    }

    private void getServicesFromRealm()
    {
        realm = Realm.getDefaultInstance();
        final RealmUserServicesHelper helper = new RealmUserServicesHelper(realm);

        //RETRIEVE
        //helper.filterRealmDatabase("user_role", "Professional");
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


    @Override
    public void onDestroy() {
        super.onDestroy();
        /*realm.removeChangeListener(realmChangeListener);
        realm.close();*/
    }

}
