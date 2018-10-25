package com.ujuzy.ujuzy.ujuzy.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.ujuzy.ujuzy.ujuzy.R;
import com.ujuzy.ujuzy.ujuzy.Realm.RealmAllServiceAdapter;
import com.ujuzy.ujuzy.ujuzy.Realm.RealmHelper;
import com.ujuzy.ujuzy.ujuzy.adapters.SeeAllAdapter;
import com.ujuzy.ujuzy.ujuzy.model.Datum;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmChangeListener;

public class SeeAllCoServicesActivity extends AppCompatActivity
{

    private Toolbar toolbar;
    private String BASE_URL = "https://api.ujuzy.com/";
    private SeeAllAdapter serviceAdapter;
    private RealmAllServiceAdapter serviceRealmAdapter;
    private RecyclerView servicesListRv;
    SwipeRefreshLayout mSwipeRefreshLayout;
    private ProgressBar progressBar1, progressBar2;
    private TextView noService, title;

    private Realm realm;
    private RealmChangeListener realmChangeListener;

    private RecyclerView countriesListRv, companyServicesListRv;
    ArrayList<Datum> results;
    private ImageView backBtn;

    Spinner spinner_filter;
    List<String> serviceFilter;
    private TextView filterCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_all_prof);

        initWindow();
        //getServices();
        initTitle();
        initProgessBar();
        initHorizScrollMenu();
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
        title = (TextView) findViewById(R.id.toolbarTv);
        title.setText("Company services");
    }

    private void initRealm()
    {
        realm = Realm.getDefaultInstance();
        final RealmHelper helper = new RealmHelper(realm);

        //RETRIEVE
        helper.filterRealmDatabase("user_role", "Company");
        filterCount = (TextView) findViewById(R.id.resultsCount);
        filterCount.setText("( " + helper.refreshDatabase().size() + " ) Results");

        //CHECK IF DATABASE IS EMPTY
        if (helper.refreshDatabase().size() < 1 || helper.refreshDatabase().size() == 0)
        {
            noService.setVisibility(View.VISIBLE);
            noService.setText("Oh sorry 😌😞 this is embarrassing but no company services posted yet!");

        } else {
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

    private void initProgessBar()
    {
        progressBar1 = (ProgressBar) findViewById(R.id.progressBar);

        noService = (TextView) findViewById(R.id.noService);
        noService.setVisibility(View.VISIBLE);
        noService.setText("Oh sorry 😌😞 this is embarrassing but no company services posted yet!");
    }


    /*public Object getServices()
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
                    *//**
 * displaying results on a recyclerview
 *//*
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
    }*/

private void viewData()
{

    countriesListRv = (RecyclerView) findViewById(R.id.service_list);
    serviceAdapter = new SeeAllAdapter(getApplicationContext(), results);

    //RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
    final LinearLayoutManager serviceLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
    countriesListRv.setLayoutManager(serviceLayoutManager);
    countriesListRv.setAdapter(serviceAdapter);

}

    private void initHorizScrollMenu()
    {
        LinearLayout plumberScrollLl = (LinearLayout) findViewById(R.id.plumberScrollLl);
        plumberScrollLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String category_type = "";
                String category_title = "Plumbing";

                Intent filterPlumbers = new Intent(SeeAllCoServicesActivity.this, FilterServicesActivity.class);
                filterPlumbers.putExtra("category_title", category_title);
                filterPlumbers.putExtra("category_type", category_type);
                startActivity(filterPlumbers);

            }
        });

        LinearLayout elecScrollLl = (LinearLayout) findViewById(R.id.elecScrollLl);
        elecScrollLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String category_type = "";
                String category_title = "Electrical";

                Intent filterPlumbers = new Intent(SeeAllCoServicesActivity.this, FilterServicesActivity.class);
                filterPlumbers.putExtra("category_type", category_type);
                filterPlumbers.putExtra("category_title", category_title);
                startActivity(filterPlumbers);

            }
        });

        LinearLayout masonScrollLl = (LinearLayout) findViewById(R.id.masonScrollLl);
        masonScrollLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String category_type = "";
                String category_title = "Masonry";

                Intent filterPlumbers = new Intent(SeeAllCoServicesActivity.this, FilterServicesActivity.class);
                filterPlumbers.putExtra("category_type", category_type);
                filterPlumbers.putExtra("category_title", category_title);
                startActivity(filterPlumbers);

            }
        });

        LinearLayout meidaScrollLl = (LinearLayout) findViewById(R.id.mediaScrollLl);
        meidaScrollLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String category_type = "";
                String category_title = "Broadcast Media";

                Intent filterPlumbers = new Intent(SeeAllCoServicesActivity.this, FilterServicesActivity.class);
                filterPlumbers.putExtra("category_type", category_type);
                filterPlumbers.putExtra("category_title", category_title);
                startActivity(filterPlumbers);

            }
        });

        LinearLayout legalScrollLl = (LinearLayout) findViewById(R.id.legalScrollLl);
        legalScrollLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String category_type = "";
                String category_title = "Legal";

                Intent filterPlumbers = new Intent(SeeAllCoServicesActivity.this, FilterServicesActivity.class);
                filterPlumbers.putExtra("category_type", category_type);
                filterPlumbers.putExtra("category_title", category_title);
                startActivity(filterPlumbers);

            }
        });

        LinearLayout accountingScrollLl = (LinearLayout) findViewById(R.id.accountingScrollLl);
        accountingScrollLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String category_type = "";
                String category_title = "Accounting";

                Intent filterPlumbers = new Intent(SeeAllCoServicesActivity.this, FilterServicesActivity.class);
                filterPlumbers.putExtra("category_type", category_type);
                filterPlumbers.putExtra("category_title", category_title);
                startActivity(filterPlumbers);

            }
        });

        LinearLayout securityScrollLl = (LinearLayout) findViewById(R.id.securityScrollLl);
        securityScrollLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String category_type = "";
                String category_title = "Security";

                Intent filterPlumbers = new Intent(SeeAllCoServicesActivity.this, FilterServicesActivity.class);
                filterPlumbers.putExtra("category_type", category_type);
                filterPlumbers.putExtra("category_title", category_title);
                startActivity(filterPlumbers);

            }
        });

        LinearLayout cosmeticsScrollLl = (LinearLayout) findViewById(R.id.cosmeticsScrollLl);
        cosmeticsScrollLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String category_type = "";
                String category_title = "Cosmetics";

                Intent filterPlumbers = new Intent(SeeAllCoServicesActivity.this, FilterServicesActivity.class);
                filterPlumbers.putExtra("category_type", category_type);
                filterPlumbers.putExtra("category_title", category_title);
                startActivity(filterPlumbers);

            }
        });

        LinearLayout landscapingScrollLl = (LinearLayout) findViewById(R.id.landscapingScrollLl);
        landscapingScrollLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String category_type = "";
                String category_title = "Landscaping";

                Intent filterPlumbers = new Intent(SeeAllCoServicesActivity.this, FilterServicesActivity.class);
                filterPlumbers.putExtra("category_type", category_type);
                filterPlumbers.putExtra("category_title", category_title);
                startActivity(filterPlumbers);

            }
        });


        LinearLayout weldersScrollLl = (LinearLayout) findViewById(R.id.weldersScrollLl);
        weldersScrollLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String category_type = "";
                String category_title = "Welders";

                Intent filterPlumbers = new Intent(SeeAllCoServicesActivity.this, FilterServicesActivity.class);
                filterPlumbers.putExtra("category_type", category_type);
                filterPlumbers.putExtra("category_title", category_title);
                startActivity(filterPlumbers);

            }
        });

        LinearLayout capentrScrollLl = (LinearLayout) findViewById(R.id.capentrScrollLl);
        capentrScrollLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String category_type = "";
                String category_title = "Carpentry";

                Intent filterPlumbers = new Intent(SeeAllCoServicesActivity.this, FilterServicesActivity.class);
                filterPlumbers.putExtra("category_type", category_type);
                filterPlumbers.putExtra("category_title", category_title);
                startActivity(filterPlumbers);

            }
        });

        LinearLayout tailorScrollLl = (LinearLayout) findViewById(R.id.tailorScrollLl);
        tailorScrollLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String category_type = "";
                String category_title = "Tailors";

                Intent filterPlumbers = new Intent(SeeAllCoServicesActivity.this, FilterServicesActivity.class);
                filterPlumbers.putExtra("category_type", category_type);
                filterPlumbers.putExtra("category_title", category_title);
                startActivity(filterPlumbers);

            }
        });

        LinearLayout seeallScrollLl = (LinearLayout) findViewById(R.id.seeallScrollLl);
        seeallScrollLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String electricitan_type = "";

                Intent filterPlumbers = new Intent(SeeAllCoServicesActivity.this, SeeAllCoServicesActivity.class);
                filterPlumbers.putExtra("category_type", electricitan_type);
                startActivity(filterPlumbers);

            }
        });
    }


    private void initWindow()
    {
        Window window = SeeAllCoServicesActivity.this.getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            window.setStatusBarColor(ContextCompat.getColor( SeeAllCoServicesActivity.this,R.color.colorPrimaryDark));

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
