package com.ujuzy.ujuzy.Tabs;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ujuzy.ujuzy.R;
import com.ujuzy.ujuzy.adapters.ReviewsAdapter;
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
public class ReviewsFragment extends Fragment
{

    private ReviewsAdapter reviewsAdapter;
    private RecyclerView reviewsListRv;
    private static String BASE_URL = "https://api.ujuzy.com/";
    ArrayList<Datum> results;
    String reviews;
    String service_id = "";
    private ProgressBar progressBar;
    private TextView noService;

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

        /*Retrofit retrofit = new Retrofit
                .Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Api api = retrofit.create(Api.class);

        api.getReviewsByServiceId(service_id).enqueue(new Callback<ResponseBody>()
        {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response)
            {


                //results = (ArrayList<Datum>) response.body().toString();

                viewData();

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
*/
        //getReviews();
        return v;
    }

    private void initProgessBar()
    {

        noService.setVisibility(View.VISIBLE);
        noService.setText("No reviews posted yet!");
    }

   /* public Object getReviews() {

        Api api = RetrofitInstance.getService();
        Call<Service> callReviews = api.getReviewsByServiceId(service_id);

        callReviews.enqueue(new Callback<Service>()
        {
            @Override
            public void onResponse(Call<Service> callReviews, Response<Service> response)
            {
                progressBar.setVisibility(View.VISIBLE);
                Service service = response.body();

                if (service != null && service.getData() != null)
                {
                    results = (ArrayList<Datum>) service.getData();

                    Log.d("RetrofitReviews", service.getData().toString());

                    // displaying results on a recyclerview

                    if (results.size() < 1 || results.size() == 0)
                    {
                        progressBar.setVisibility(View.GONE);

                        noService.setVisibility(View.VISIBLE);

                    } else
                    {
                        progressBar.setVisibility(View.GONE);
                        viewData();
                    }
                }
            }

            @Override
            public void onFailure(Call<Service> call, Throwable t) {

            }

        });
        return results;
    }*/

    private void viewData()
    {
        reviewsAdapter = new ReviewsAdapter(getActivity(), results);

        //RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        final LinearLayoutManager serviceLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        reviewsListRv.setLayoutManager(serviceLayoutManager);
        reviewsListRv.setAdapter(reviewsAdapter);
    }
}
