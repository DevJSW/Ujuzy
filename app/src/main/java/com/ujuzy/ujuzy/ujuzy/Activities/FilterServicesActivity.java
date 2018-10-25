package com.ujuzy.ujuzy.ujuzy.Activities;

import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.ujuzy.ujuzy.ujuzy.R;
import com.ujuzy.ujuzy.ujuzy.Realm.RealmAllServiceAdapter;
import com.ujuzy.ujuzy.ujuzy.Realm.RealmHelper;
import com.ujuzy.ujuzy.ujuzy.Realm.RealmService;
import com.ujuzy.ujuzy.ujuzy.adapters.SeeAllAdapter;
import com.ujuzy.ujuzy.ujuzy.model.Datum;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class FilterServicesActivity extends AppCompatActivity
{

    String category_type = "";
    String category_title = "";
    String selectedFilter = null;

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

    SwipeRefreshLayout mSwipeRefreshLayout;
    Spinner spinner_filter;
    List<String> serviceFilter;
    private TextView filterCount;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_services);

        initWindow();
        initCategories();
        //initProgessBar();
        //getServices();
        initTitle();

        initRealm();
        initRefreshPage();
        initFilter();
    }

    private void initFilter()
    {
        spinner_filter = (Spinner) findViewById(R.id.spinner_filter);

        String[] service_details_string = new String[]{
                "Filter",
                "Price: high-low",
                "Price: low-high",
                "Avg. Reviews",
                "Newest"
        };

        serviceFilter = new ArrayList<>(Arrays.asList(service_details_string));

        // Initializing an ArrayAdapter
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                getApplicationContext(), R.layout.spinner_filter_item, serviceFilter) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };


        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinner_filter.setAdapter(spinnerArrayAdapter);

        /*spinner_filter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedFilter = (String) parent.getItemAtPosition(position);
                // If user change the default selection
                // First item is disable and it is used for hint
                if (position > 0) {
                    // Notify the selected item text


                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });*/

    }

    private void initRealm()
    {
        realm = Realm.getDefaultInstance();
        final RealmHelper helper = new RealmHelper(realm);

        //QUERY/FILTER REALM DATABASE
        helper.filterRealmDatabase("category", category_title);

        filterCount = (TextView) findViewById(R.id.resultsCount);
        filterCount.setText("( " + helper.refreshDatabase().size() + " ) Results");

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

        companyServicesListRv = (RecyclerView) findViewById(R.id.service_list);
        serviceRealmAdapter = new RealmAllServiceAdapter(getApplicationContext(), helper.refreshDatabase());

        //RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        final LinearLayoutManager serviceLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        companyServicesListRv.setLayoutManager(serviceLayoutManager);
        companyServicesListRv.setAdapter(serviceRealmAdapter);

        //HANDLE DATA CHANGE FOR REFRESH
        realmChangeListener = new RealmChangeListener()
        {
            @Override
            public void onChange(Object o) {
                //REFRESH
                serviceRealmAdapter = new RealmAllServiceAdapter(getApplicationContext(), helper.refreshDatabase());
                companyServicesListRv.setAdapter(serviceRealmAdapter);
            }
        };

        //ADD CHANGE LIST TO REALM
        realm.addChangeListener(realmChangeListener);
    }

    private void initRefreshPage() {
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        initRealm();
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }, 3000);

            }
        });
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
