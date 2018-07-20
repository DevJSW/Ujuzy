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
import com.ujuzy.ujuzy.adapters.CountryAdapter;
import com.ujuzy.ujuzy.adapters.SeeAllAdapter;
import com.ujuzy.ujuzy.model.Datum;
import com.ujuzy.ujuzy.model.RetrofitInstance;
import com.ujuzy.ujuzy.model.Service;
import com.ujuzy.ujuzy.services.Api;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ServicesFragment extends Fragment {

    String serviceId = "";
    String userId = "";
    private CountryAdapter countryAdapter;
    private RecyclerView serviceListRv;
    private static String BASE_URL = "https://api.ujuzy.com/";
    ArrayList<Datum> results;
    private SeeAllAdapter serviceAdapter;
    private ProgressBar progressBar;
    private TextView noService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_services, container, false);

        serviceListRv = (RecyclerView) v.findViewById(R.id.service_list);
        progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
        noService = (TextView) v.findViewById(R.id.noService);


        initProgessBar();

        Bundle bundle2 = this.getArguments();
        if (bundle2 != null) {
            serviceId = bundle2.getString("serviceId", null);
            userId = bundle2.getString("userId", null);
        }

        getServices();

        /**
         *  getting services by user id
         */

        /*Retrofit retrofit = new Retrofit
                .Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Api api = retrofit.create(Api.class);

        api.getServicesByCreatedBy(userId).enqueue(new Callback<Service>()
        {
            @Override
            public void onResponse(Call<Service> call, Response<Service> response)
            {

                Service service = response.body();

                results = (ArrayList<Datum>) service.getData();

                Log.d("ServicresRetrofit", service.getData().toString());


                viewData();

            }

            @Override
            public void onFailure(Call<Service> call, Throwable t) {

            }

        });*/

        return v;
    }

    private void initProgessBar()
    {

        progressBar.setVisibility(View.VISIBLE);
        noService.setVisibility(View.GONE);
    }


    public Object getServices()
    {

        Api api = RetrofitInstance.getService();
        Call<Service> callUserServices = api.getServicesById(serviceId);

        callUserServices.enqueue(new Callback<Service>()
        {
            @Override
            public void onResponse(Call<Service> callUserServices, Response<Service> response)
            {

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
    }

    private void viewData()
    {

        serviceAdapter = new SeeAllAdapter(getActivity(), results);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        serviceListRv.setLayoutManager(layoutManager);
        serviceListRv.setAdapter(serviceAdapter);

    }

}
