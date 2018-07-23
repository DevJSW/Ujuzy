package com.ujuzy.ujuzy.activities;

import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ujuzy.ujuzy.R;
import com.ujuzy.ujuzy.adapters.SeeAllAdapter;
import com.ujuzy.ujuzy.model.Datum;
import com.ujuzy.ujuzy.model.RetrofitInstance;
import com.ujuzy.ujuzy.model.Service;
import com.ujuzy.ujuzy.services.Api;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FilterServicesActivity extends AppCompatActivity
{

    String category_type = "";
    String category_title = "";

    private SeeAllAdapter serviceAdapter;
    private RecyclerView servicesListRv;
    //   ArrayList<Service> results;
    private ProgressBar progressBar1, progressBar2;
    private TextView noService, noServiceCo;

    private RecyclerView countriesListRv, companyServicesListRv;
    ArrayList<Datum> results;
    private ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        initWindow();
        getServices();
        initProgessBar();
        initCategories();
        initTitle();
    }

    private void initTitle()
    {
        TextView title = (TextView) findViewById(R.id.toolbarTv);
        title.setText(category_title);
    }

    private void initCategories()
    {
        category_type = getIntent().getStringExtra("category_type");
        category_title = getIntent().getStringExtra("category_title");
    }

    private void initProgessBar()
    {
        progressBar1 = (ProgressBar) findViewById(R.id.progressBar);

        progressBar1.setVisibility(View.VISIBLE);
        noService = (TextView) findViewById(R.id.noService);
    }


    public Object getServices()
    {

        Api api = RetrofitInstance.getService();
        Call<Service> call = api.getServices();

        call.enqueue(new Callback<Service>()
        {
            @Override
            public void onResponse(Call<Service> call, Response<Service> response)
            {

                Service service = response.body();

                if (service != null && service.getData() != null)
                {
                    results = (ArrayList<Datum>) service.getData();
                    /**
                     * displaying results on a recyclerview
                     */
                    if (results.size() < 1 || results.size() == 0)
                    {
                        progressBar1.setVisibility(View.GONE);

                        noService.setVisibility(View.VISIBLE);

                    } else
                    {
                        progressBar1.setVisibility(View.GONE);
                        viewData();
                    }
                }
            }

            @Override
            public void onFailure(Call<Service> call, Throwable t) {

            }

        });


        return results;
    }

    private void viewData()
    {

        countriesListRv = (RecyclerView) findViewById(R.id.service_list);
        serviceAdapter = new SeeAllAdapter(getApplicationContext(), results);

        //RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        final LinearLayoutManager serviceLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        countriesListRv.setLayoutManager(serviceLayoutManager);
        countriesListRv.setAdapter(serviceAdapter);

    }


    private void initView()
    {
        setContentView(R.layout.activity_filter_services);

        backBtn = (ImageView) findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });

    }

    private void initWindow()
    {
        Window window = FilterServicesActivity.this.getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            window.setStatusBarColor(ContextCompat.getColor( FilterServicesActivity.this,R.color.colorPrimaryDark));

            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        }
    }
}
