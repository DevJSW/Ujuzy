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

import com.ujuzy.ujuzy.R;
import com.ujuzy.ujuzy.Realm.RealmAllServiceAdapter;
import com.ujuzy.ujuzy.Realm.RealmHelper;
import com.ujuzy.ujuzy.adapters.CountryAdapter;
import com.ujuzy.ujuzy.adapters.SeeAllAdapter;
import com.ujuzy.ujuzy.model.Datum;
import com.ujuzy.ujuzy.model.RetrofitInstance;
import com.ujuzy.ujuzy.model.Service;
import com.ujuzy.ujuzy.services.Api;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ServicesFragment extends Fragment {

    String serviceId = "";
    String userId = "";
    String firstName = "";
    private CountryAdapter countryAdapter;
    private RecyclerView serviceListRv;
    private static String BASE_URL = "https://api.ujuzy.com/";
    ArrayList<Datum> results;
    private SeeAllAdapter serviceAdapter;
    private ProgressBar progressBar;
    private TextView noService;

    private Realm realm;
    private RealmChangeListener realmChangeListener;
    private RealmAllServiceAdapter serviceRealmAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_services, container, false);

        serviceListRv = (RecyclerView) v.findViewById(R.id.service_list);
        progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
        noService = (TextView) v.findViewById(R.id.noService);


        Bundle bundle2 = this.getArguments();
        if (bundle2 != null) {
            serviceId = bundle2.getString("serviceId", null);
            userId = bundle2.getString("userId", null);
            firstName = bundle2.getString("firstName", null);
        }

        //getServices();

        /**
         *  getting services by user id
         */


       initRealm();

        return v;
    }

    private void initRealm()
    {
        realm = Realm.getDefaultInstance();
        final RealmHelper helper = new RealmHelper(realm);

        //QUERY/FILTER REALM DATABASE
        helper.filterRealmDatabase("created_by", userId);

        //CHECK IF DATABASE IS EMPTY
        if (helper.refreshDatabase().size() < 1 || helper.refreshDatabase().size() == 0)
        {
            noService.setVisibility(View.VISIBLE);
            noService.setText(firstName + "has no services posted yet!");
        } else {
            noService.setVisibility(View.GONE);
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



    /*public Object getServices()
    {

        Api api = RetrofitInstance.getService();
        Call<Service> callUserServices = api.getServicesById(serviceId);

        callUserServices.enqueue(new Callback<Service>()
        {
            @Override
            public void onResponse(Call<Service> callUserServices, Response<Service> response)
            {
                progressBar.setVisibility(View.VISIBLE);
                Service service = response.body();

                if (service != null && service.getData() != null)
                {
                    results = (ArrayList<Datum>) service.getData();

                    if (results.size() < 1 || results.size() == 0)
                    {
                        progressBar.setVisibility(View.GONE);

                        noService.setVisibility(View.VISIBLE);

                    } else
                    {
                        viewData();
                    }
                }
            }

            @Override
            public void onFailure(Call<Service> call, Throwable t)
            {

            }

        });


        return results;
    }*/

    private void viewData()
    {

        serviceAdapter = new SeeAllAdapter(getActivity(), results);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        serviceListRv.setLayoutManager(layoutManager);
        serviceListRv.setAdapter(serviceAdapter);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.removeChangeListener(realmChangeListener);
        realm.close();
    }

}
