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
import com.ujuzy.ujuzy.Realm.RealmAllServiceAdapter;
import com.ujuzy.ujuzy.Realm.RealmHelper;
import com.ujuzy.ujuzy.Realm.RealmService;
import com.ujuzy.ujuzy.adapters.SeeAllAdapter;
import com.ujuzy.ujuzy.model.Datum;
import com.ujuzy.ujuzy.model.RetrofitInstance;
import com.ujuzy.ujuzy.model.Service;
import com.ujuzy.ujuzy.services.Api;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
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

    private Realm realm;
    private RealmChangeListener realmChangeListener;
    private RealmAllServiceAdapter serviceRealmAdapter;
    RealmResults<RealmService> services;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        initWindow();
        initCategories();
        //initProgessBar();
        //getServices();
        initTitle();

        initRealm();
    }

    private void initRealm()
    {
        realm = Realm.getDefaultInstance();
        final RealmHelper helper = new RealmHelper(realm);

        //QUERY/FILTER REALM DATABASE
        helper.filterRealmDatabase("category", category_title);

        //CHECK IF DATABASE IS EMPTY
        if (helper.refreshDatabase().size() < 1 || helper.refreshDatabase().size() == 0)
        {
            noService = (TextView) findViewById(R.id.noService);
            noService.setVisibility(View.VISIBLE);
            noService.setText("Oh sorry 😌😞 this is embarrassing but no " + category_title + " services posted yet!");
        } else {
            noService = (TextView) findViewById(R.id.noService);
            noService.setVisibility(View.GONE);
        }


        countriesListRv = (RecyclerView) findViewById(R.id.service_list);
        serviceRealmAdapter = new RealmAllServiceAdapter(getApplicationContext(), helper.refreshDatabase());

        //RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        final LinearLayoutManager serviceLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        countriesListRv.setLayoutManager(serviceLayoutManager);
        countriesListRv.setAdapter(serviceRealmAdapter);

        //HANDLE DATA CHANGE FOR REFRESH
        realmChangeListener = new RealmChangeListener()
        {
            @Override
            public void onChange(Object o) {
                //REFRESH
                serviceRealmAdapter = new RealmAllServiceAdapter(getApplicationContext(), helper.refreshDatabase());
                companyServicesListRv.setAdapter(serviceRealmAdapter);
                servicesListRv.setAdapter(serviceRealmAdapter);
            }
        };

        //ADD CHANGE LIST TO REALM
        realm.addChangeListener(realmChangeListener);
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

        noService = (TextView) findViewById(R.id.noService);
        noService.setVisibility(View.VISIBLE);
        noService.setText("Sorry, no " + category_title + " services posted yet!");
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
                progressBar1.setVisibility(View.VISIBLE);
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
                        noService.setText("Sorry, no " + category_title + " services posted yet!");

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.removeChangeListener(realmChangeListener);
        realm.close();
    }
}
