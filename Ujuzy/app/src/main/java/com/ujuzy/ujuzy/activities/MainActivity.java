package com.ujuzy.ujuzy.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ujuzy.ujuzy.R;
import com.ujuzy.ujuzy.Realm.RealmHelper;
import com.ujuzy.ujuzy.Realm.RealmService;
import com.ujuzy.ujuzy.Realm.RealmServiceAdapter;
import com.ujuzy.ujuzy.SqliteDatabase.ServicesDatabase;
import com.ujuzy.ujuzy.adapters.CountryAdapter;
import com.ujuzy.ujuzy.adapters.ServiceAdapter;
import com.ujuzy.ujuzy.map.MapsActivity;
import com.ujuzy.ujuzy.model.Constants;
import com.ujuzy.ujuzy.model.Datum;
import com.ujuzy.ujuzy.model.RetrofitInstance;
import com.ujuzy.ujuzy.model.Service;
import com.ujuzy.ujuzy.services.Api;
import com.ujuzy.ujuzy.services.NetworkChecker;
import com.ujuzy.ujuzy.services.ServiceInterface;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmConfiguration;
import io.realm.RealmList;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
{
    private static String TAG = "MainActivity.this";
    private String webview_url = "https://ujuzy.com/services/create";
    private Toolbar toolbar;
    private ServiceAdapter serviceAdapter;
    private RealmServiceAdapter serviceReamAdapter;
    private RecyclerView servicesListRv;
    private ProgressBar progressBar1, progressBar2;

    private RelativeLayout searchRely;

    private Realm realm;
    private RealmChangeListener realmChangeListener;

    private RecyclerView companyServicesListRv;
    ArrayList<Datum> results;
    ArrayList<RealmService> reamResults;
    private TextView tvSeeAllProf, tvSeeAllComp, noService, noServiceCo;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        initView();
        initWindow();
        initFab();
        initDrawer();
        initProgessBar();

        //CHECK IF APP IS CONNECTED TO INTERNET
        if (NetworkChecker.isNetworkAvailable(getApplicationContext()))
        {
            getServicesFromApi();
            getServicesFromRetrofitApi();
        } else {

        }

        getServicesFromDatabase();

        initSeeAll();
        //initProgessBar();
        initSearch();
        initHorizScrollMenu();

    }

    @Override
    protected void onResume() {
        super.onResume();
        // stop keyboard from show when activity is started.
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    private void getProfServices()
    {
        realm = Realm.getDefaultInstance();
        final RealmHelper helper = new RealmHelper(realm);

        //RETRIEVE
        helper.filterRealmDatabase("user_role", "Professional");

        //CHECK IF DATABASE IS EMPTY
        if (helper.refreshDatabase().size() < 1 || helper.refreshDatabase().size() == 0)
        {
            noService.setVisibility(View.VISIBLE);

        } else {
            noService.setVisibility(View.GONE);
        }

        //CHECK IF DATABASE IS EMPTY
//        if (helper.refreshDatabase().size() < 1 || helper.refreshDatabase().size() == 0)
//        {
//            noService = (TextView) findViewById(R.id.noService);
//            noService.setVisibility(View.VISIBLE);
//            noService.setText("Oh sorry 😌😞 this is embarrassing but no Professional services posted yet!");
//        } else {
//            noService = (TextView) findViewById(R.id.noService);
//            noService.setVisibility(View.GONE);
//        }

        servicesListRv = (RecyclerView) findViewById(R.id.service_list);
        serviceReamAdapter = new RealmServiceAdapter(getApplicationContext(), helper.refreshDatabase());

        //RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        final LinearLayoutManager serviceLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        servicesListRv.setLayoutManager(serviceLayoutManager);
        servicesListRv.setAdapter(serviceReamAdapter);

        //HANDLE DATA CHANGE FOR REFRESH
        realmChangeListener = new RealmChangeListener()
        {
            @Override
            public void onChange(Object o) {
                //REFRESH
                serviceReamAdapter = new RealmServiceAdapter(getApplicationContext(), helper.refreshDatabase());
                servicesListRv.setAdapter(serviceReamAdapter);
            }
        };

        //ADD CHANGE LIST TO REALM
        realm.addChangeListener(realmChangeListener);
    }

    private void getCompServices()
    {
        realm = Realm.getDefaultInstance();
        final RealmHelper helper = new RealmHelper(realm);

        //RETRIEVE
        helper.filterRealmDatabase("user_role", "company");

        //CHECK IF DATABASE IS EMPTY
        if (helper.refreshDatabase().size() < 1 || helper.refreshDatabase().size() == 0)
        {

            noServiceCo.setVisibility(View.VISIBLE);

        } else {

            noServiceCo.setVisibility(View.GONE);
        }

        companyServicesListRv = (RecyclerView) findViewById(R.id.company_service_list);
        serviceReamAdapter = new RealmServiceAdapter(getApplicationContext(), helper.refreshDatabase());

        //RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        final LinearLayoutManager compServiceLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        companyServicesListRv.setLayoutManager(compServiceLayoutManager);
        companyServicesListRv.setAdapter(serviceReamAdapter);

        //HANDLE DATA CHANGE FOR REFRESH
        realmChangeListener = new RealmChangeListener()
        {
            @Override
            public void onChange(Object o) {
                //REFRESH
                serviceReamAdapter = new RealmServiceAdapter(getApplicationContext(), helper.refreshDatabase());
                companyServicesListRv.setAdapter(serviceReamAdapter);
            }
        };

        //ADD CHANGE LIST TO REALM
        realm.addChangeListener(realmChangeListener);
    }


    private void initHorizScrollMenu()
    {
        LinearLayout plumberScrollLl = (LinearLayout) findViewById(R.id.plumberScrollLl);
        plumberScrollLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String category_type = "";
                String category_title = "Plumbing";

                Intent filterPlumbers = new Intent(MainActivity.this, FilterServicesActivity.class);
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

                Intent filterPlumbers = new Intent(MainActivity.this, FilterServicesActivity.class);
                filterPlumbers.putExtra("category_type", category_type);
                filterPlumbers.putExtra("category_title", category_title);
                startActivity(filterPlumbers);

            }
        });

        LinearLayout paintingScrollLl = (LinearLayout) findViewById(R.id.paintingScrollLl);
        paintingScrollLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String category_type = "";
                String category_title = "Painting";

                Intent filterPlumbers = new Intent(MainActivity.this, FilterServicesActivity.class);
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

                Intent filterPlumbers = new Intent(MainActivity.this, FilterServicesActivity.class);
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

                Intent filterPlumbers = new Intent(MainActivity.this, FilterServicesActivity.class);
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

                Intent filterPlumbers = new Intent(MainActivity.this, FilterServicesActivity.class);
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

                Intent filterPlumbers = new Intent(MainActivity.this, FilterServicesActivity.class);
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

                Intent filterPlumbers = new Intent(MainActivity.this, FilterServicesActivity.class);
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

                Intent filterPlumbers = new Intent(MainActivity.this, FilterServicesActivity.class);
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

                Intent filterPlumbers = new Intent(MainActivity.this, FilterServicesActivity.class);
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

                Intent filterPlumbers = new Intent(MainActivity.this, FilterServicesActivity.class);
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

                Intent filterPlumbers = new Intent(MainActivity.this, FilterServicesActivity.class);
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

                Intent filterPlumbers = new Intent(MainActivity.this, FilterServicesActivity.class);
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

                Intent filterPlumbers = new Intent(MainActivity.this, SeeAllActivity.class);
                filterPlumbers.putExtra("category_type", electricitan_type);
                startActivity(filterPlumbers);

            }
        });
    }

    private void getServicesFromDatabase()
    {

        getProfServices();
        getCompServices();

    }

    private void initSearch()
    {

        searchRely = (RelativeLayout) findViewById(R.id.searchRely);
        searchRely.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, SearchActivity.class));
            }

        });

    }

    private void initProgessBar()
    {
        /*progressBar1 = (ProgressBar) findViewById(R.id.progressBar1);
        progressBar2 = (ProgressBar) findViewById(R.id.progressBar2);

        progressBar1.setVisibility(View.VISIBLE);
        progressBar2.setVisibility(View.VISIBLE);*/

        noService = (TextView) findViewById(R.id.noService);
        noServiceCo = (TextView) findViewById(R.id.noService2);

        noService.setVisibility(View.GONE);
        noServiceCo.setVisibility(View.GONE);
    }

    private void initSeeAll()
    {
        tvSeeAllComp = (TextView) findViewById(R.id.seeAllComp);
        tvSeeAllProf = (TextView) findViewById(R.id.seeAllProf);

        tvSeeAllProf.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(MainActivity.this, SeeAllActivity.class));
            }
        });

        tvSeeAllComp.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(MainActivity.this, SeeAllCoServicesActivity.class));
            }
        });
    }

    public void getServicesFromRetrofitApi() {
        ServiceInterface apiInterface = RetrofitInstance.getService();
        Call<Service> call = apiInterface.getServices();
        call.enqueue(new Callback<Service>() {
            @Override
            public void onResponse(Call<Service> call, Response<Service> response) {
                if(response.body() != null){
                    Service myResponse = response.body();
                    /*List<Detail> details = myResponse.getDetails();
                    for(Detail d : details){
                        if(d.getId().equals(id)){
                            reqDetail = d;

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {//update your views here
                                    tvName.setText(reqDetail.getName());
                                }
                            });
                            *//*reqDetail will be having everything that you need and you can get it using the following code.
                            reqDetail.getName();
                            reqDetail.getAge();
                            reqDetail.getEmail();
                            reqDetail.getMobile();*//*
                        }
                    }*/
                }
            }

            @Override
            public void onFailure(Call<Service> call, Throwable t) {

            }
        });
    }

    public void getServicesFromApi()
    {

//        Api api = RetrofitInstance.getService();
//        Call<Service> call = api.getServices();

        ServiceInterface serviceInterface = RetrofitInstance.getService();
        Call<Service> callService = serviceInterface.getServices();


        callService.enqueue(new Callback<Service>()
        {
            @Override
            public void onResponse(Call<Service> call, Response<Service> response)
            {

                if (response.isSuccessful())
                {

                    Service serviceList = response.body();
                    results = (ArrayList<Datum>) serviceList.getData();


                    /**
                     * displaying results to adapter
                     */

                    if (results.size() < 1 || results.size() == 0)
                    {

                        /*progressBar1.setVisibility(View.GONE);
                        progressBar2.setVisibility(View.GONE);*/

                        noService.setVisibility(View.VISIBLE);
                        noServiceCo.setVisibility(View.VISIBLE);

                    } else
                    {
                        progressBar1.setVisibility(View.GONE);
                        progressBar2.setVisibility(View.GONE);
                        viewData();
                    }

                } else {

                    /*progressBar1.setVisibility(View.GONE);
                    progressBar2.setVisibility(View.GONE);*/

                    noService.setVisibility(View.VISIBLE);
                    noServiceCo.setVisibility(View.VISIBLE);

                }

            }

            @Override
            public void onFailure(Call<Service> call, Throwable t)
            {
                noService.setVisibility(View.VISIBLE);
                noServiceCo.setVisibility(View.VISIBLE);
            }

        });


       // return results;
    }

    private void viewData()
    {

       // servicesListRv = (RecyclerView) findViewById(R.id.service_list);
        serviceAdapter = new ServiceAdapter(getApplicationContext(), results);

       /* //RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        final LinearLayoutManager serviceLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        servicesListRv.setLayoutManager(serviceLayoutManager);
        servicesListRv.setAdapter(serviceAdapter);*/

        //companyServicesListRv = (RecyclerView) findViewById(R.id.company_service_list);
        serviceAdapter = new ServiceAdapter(getApplicationContext(), results);

        //RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        /*final LinearLayoutManager compServiceLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        companyServicesListRv.setLayoutManager(compServiceLayoutManager);
        companyServicesListRv.setAdapter(serviceAdapter);*/


    }


    private void initView()
    {
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.mCustomToolbar);
        setSupportActionBar(toolbar);
    }

    private void initWindow()
    {
        Window window = MainActivity.this.getWindow();
        // stop keyboard from show when activity is started.
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            window.setStatusBarColor(ContextCompat.getColor( MainActivity.this,R.color.colorPrimaryDark));

            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        }
    }

    private void initDrawer()
    {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void initFab()
    {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent webView = new Intent(MainActivity.this, WebViewActivity.class);
                webView.putExtra("webview_url", webview_url);
                startActivity(webView);
            }
        });
    }

    @Override
    public void onBackPressed()
    {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        } else
        {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera)
        {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            // Handle the camera action
        } else if (id == R.id.nav_gallery)
        {
            startActivity(new Intent(MainActivity.this, FavouriteActivity.class));

        } else if (id == R.id.nav_slideshow)
        {

            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://ujuzy.com"));
            startActivity(browserIntent);

        } else if (id == R.id.nav_manage)
        {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));

        } else if (id == R.id.nav_share)
        {
            Intent myIntent = new Intent(Intent.ACTION_SEND);
            myIntent.setType("text/plain");
            String shareBody ="Ujuzy App";
            String shareSub = "Download Ujuzy app TODAY!";
            myIntent.putExtra(Intent.EXTRA_SUBJECT,shareBody);
            myIntent.putExtra(Intent.EXTRA_TEXT,shareSub);
            startActivity(Intent.createChooser(myIntent,"Share this app"));

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (realmChangeListener != null)
        realm.removeChangeListener(realmChangeListener);
        if (realm != null)
        realm.close();
    }

}
