package com.ujuzy.ujuzy.activities;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Pair;
import android.view.SubMenu;
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
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.ujuzy.ujuzy.CustomData.CustomTypefaceSpan;
import com.ujuzy.ujuzy.R;
import com.ujuzy.ujuzy.Realm.RealmHelper;
import com.ujuzy.ujuzy.Realm.RealmService;
import com.ujuzy.ujuzy.Realm.RealmServiceAdapter;
import com.ujuzy.ujuzy.Realm.RealmToken;
import com.ujuzy.ujuzy.Realm.RealmTokenHelper;
import com.ujuzy.ujuzy.Realm.RealmUser;
import com.ujuzy.ujuzy.Realm.RealmUserHelper;
import com.ujuzy.ujuzy.SqliteDatabase.ServicesDatabase;
import com.ujuzy.ujuzy.Utils.JWTUtilis;
import com.ujuzy.ujuzy.adapters.CountryAdapter;
import com.ujuzy.ujuzy.adapters.ServiceAdapter;
import com.ujuzy.ujuzy.map.MapsActivity;
import com.ujuzy.ujuzy.model.Constants;
import com.ujuzy.ujuzy.model.Datum;
import com.ujuzy.ujuzy.model.Login;
import com.ujuzy.ujuzy.model.RetrofitInstance;
import com.ujuzy.ujuzy.model.Service;
import com.ujuzy.ujuzy.model.User;
import com.ujuzy.ujuzy.services.Api;
import com.ujuzy.ujuzy.services.NetworkChecker;
import com.ujuzy.ujuzy.services.ServiceClient;
import com.ujuzy.ujuzy.services.ServiceInterface;

import org.jboss.aerogear.android.authorization.AuthorizationManager;
import org.jboss.aerogear.android.authorization.AuthzModule;
import org.jboss.aerogear.android.authorization.oauth2.OAuth2AuthorizationConfiguration;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmConfiguration;
import io.realm.RealmList;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
{

    private String token = "";
    private static String TAG = "MainActivity.this";
    private String webview_url = "https://ujuzy.com/services/create";
    private Toolbar toolbar;
    private ServiceAdapter serviceAdapter;
    private RealmServiceAdapter serviceReamAdapter;
    private RecyclerView servicesListRv;
    private ProgressBar progressBar1, progressBar2;

    private RelativeLayout searchRely;

    NavigationView navigationView;

    //json volley
    private final String JSON_URL = "https://api.ujuzy.com/services";
    private final String USER_PROFILE_JSON_URL = "https://api.ujuzy.com/users/profile";
    private JsonArrayRequest request;
    private RequestQueue requestQueue;
    private List<Datum> serviceList;

    private Retrofit retrofit;

    private Realm realm;
    private RealmChangeListener realmChangeListener;// it will come up with the most of it to the inner of it all, make sure you come up with all of the things

    private RecyclerView companyServicesListRv;
    ArrayList<Datum> results;
    ArrayList<RealmService> reamResults;
    private TextView tvSeeAllProf, tvSeeAllComp, noService, noServiceCo, terms, usernameTv, useremailTv;

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
        if (NetworkChecker.isNetworkAvailable(getApplicationContext()))  // it will take more than ti
        {
            noService.setVisibility(View.GONE);
           // getServicesFromApi();
            //volleyJsonRequest();
            getServices();

        } else {
            //FETCH SERVICES FROM DATABASE
            getServicesFromDatabase();
        }


        initSeeAll();
        initSearch();
        initHorizScrollMenu();
        initNavigationCustomixation();
        initTerms();

    }

    private void initTerms() {
        terms = (TextView) findViewById(R.id.terms);
        terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://ujuzy.com/terms"));
                startActivity(browserIntent);
            }
        });
    }

    private void initNavigationCustomixation() {

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        View hView = navigationView.getHeaderView(0);

        usernameTv = hView.findViewById(R.id.tv_user_name);
        useremailTv = hView.findViewById(R.id.tv_user_email);

        realm = Realm.getDefaultInstance();
        RealmUser user = realm.where(RealmUser.class).findFirst();
        if (user != null) {
            usernameTv.setText(user.getFirstname() + " " + user.getLastname());
            useremailTv.setText(user.getEmail());
        }

        navigationView.setNavigationItemSelectedListener(this);

        Menu m = navigationView.getMenu();
        for (int i=0;i<m.size();i++) {
            MenuItem mi = m.getItem(i);
            applyFontToMenuItem(mi);

            //for applying a font to subMenu ...
            SubMenu subMenu = mi.getSubMenu();
            if (subMenu != null && subMenu.size() > 0) {
                for (int j = 0; j < subMenu.size(); j++) {
                    MenuItem subMenuItem = subMenu.getItem(j);
                    applyFontToMenuItem(subMenuItem);
                }
            }
        }
    }

    private void applyFontToMenuItem(MenuItem item) {
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/Aller_Rg.ttf");
        SpannableString mNewTitle = new SpannableString(item.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("" , font), 0 , mNewTitle.length(),  Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        item.setTitle(mNewTitle);
    }

    private Retrofit getRetrofit()
    {
        if (this.retrofit == null)
        {

            this.retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.HTTP.SERVICES_ENDPOINT)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return this.retrofit;
    }

    public void getServices()
    {
        Api api = getRetrofit().create(Api.class);
        Call<Service> ServiceData =  api.getServices();
        ServiceData.enqueue(new Callback<Service>() {
            @Override
            public void onResponse(Call<Service> call, Response<Service> response) {

                Service service = response.body();

                if (service != null && service.getData() != null)
                {
                    results = (ArrayList<Datum>) service.getData();
                    /**
                     * displaying results on a recyclerview
                     */
                    viewData();
                }
            }

            @Override
            public void onFailure(Call<Service> call, Throwable t) {

                volleyJsonRequest();

                //Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }

    private void volleyJsonRequest()
    {

        serviceList = new ArrayList<>();

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                JSON_URL, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    for (int i = 0 ; i < jsonArray.length() ; i++)
                    {

                        JSONObject serviceObj = jsonArray.getJSONObject(i);
                        JSONObject serviceImgObj = jsonArray.getJSONObject(i).getJSONObject("images");
                        JSONObject serviceUserObj = jsonArray.getJSONObject(i).getJSONObject("user");
                        JSONObject serviceLocationObj = jsonArray.getJSONObject(i).getJSONObject("location");
                        JSONObject serviceDurationObj = jsonArray.getJSONObject(i).getJSONObject("duration");

                        JSONArray skillList = serviceObj.getJSONArray("skill_list");

                      /*  Datum datum = new Datum();
                        datum.setId(serviceObj.getString("id"));
                        datum.setServiceName(serviceObj.getString("service_name"));
                        datum.setServiceDetails(serviceObj.getString("service_details"));
                        datum.setCost(serviceObj.getString("cost"));
                        *//*datum.setCreatedBy(serviceObj.getString("created_by"));
                        datum.setCreatedBy(serviceUserObj.getString());
                        datum.setCategory(serviceObj.getString("category"));
                        datum.setCreated_at(serviceObj.getString("created_at"));
                        datum.setImage(serviceImgObj.getString("thumb"));

                        datum.setUser_role(serviceUserObj.getString("user_role"));
                        datum.setUser_thumb(serviceUserObj.getString("profile_pic"));
                        datum.setFirst_name(serviceUserObj.getString("firstname"));
                        datum.setLast_name(serviceUserObj.getString("lastname"));
                        datum.setUser_id(serviceUserObj.getString("id"));

                        datum.setCity(serviceLocationObj.getString("city"));

                        datum.setService_duration_days(serviceDurationObj.getString("days"));
                        datum.setService_duration_hours(serviceDurationObj.getString("hours"));*//*

                        serviceList.add(datum);

                        servicesListRv = (RecyclerView) findViewById(R.id.service_list);
                        serviceAdapter = new ServiceAdapter(getApplicationContext(), results);

                        //RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
                        final LinearLayoutManager serviceLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
                        servicesListRv.setLayoutManager(serviceLayoutManager);
                        servicesListRv.setAdapter(serviceAdapter);
*/
                        // ASSIGN DATA TO REALM DATABASE SERVICE

                        RealmService realmService = new  RealmService();
                        realmService.setId(serviceObj.getString("id"));
                        realmService.setServiceName(serviceObj.getString("service_name"));
                        realmService.setServiceDetails(serviceObj.getString("service_details"));
                        realmService.setCost(serviceObj.getString("cost"));
                        realmService.setCreatedBy(serviceObj.getString("created_by"));
                        realmService.setCategory(serviceObj.getString("category"));
                        realmService.setCreated_at(serviceObj.getString("created_at"));
                        realmService.setImage(serviceImgObj.getString("thumb"));

                        realmService.setUser_role(serviceUserObj.getString("user_role"));
                        realmService.setUser_thumb(serviceUserObj.getString("profile_pic"));
                        realmService.setFirst_name(serviceUserObj.getString("firstname"));
                        realmService.setLast_name(serviceUserObj.getString("lastname"));
                        realmService.setUser_id(serviceUserObj.getString("id"));

                        realmService.setCity(serviceLocationObj.getString("city"));

                        realmService.setService_duration_days(serviceDurationObj.getString("days"));
                        realmService.setService_duration_hours(serviceDurationObj.getString("hours"));

                        for (int s = 0; s < skillList.length() ; s++) {

                        }

                        //SAVE
                        realm = Realm.getDefaultInstance();
                        RealmHelper helper = new RealmHelper(realm);
                        helper.save(realmService);


                        /**************************** END ******************************/

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(stringRequest);


        /*request = new JsonArrayRequest(JSON_URL, new com.android.volley.Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                JSONObject jsonObject = null;

                for (int i = 0 ; i < response.length() ; i++)
                {
                    try {

                        jsonObject = response.getJSONObject(i);

                        Service service = new Service();
                        Datum datum = new Datum();
                        results = (ArrayList<Datum>) service.getData();
                        datum.setServiceName(jsonObject.getString("service_name"));
                        datum.setServiceDetails(jsonObject.getString("service_details"));
                        datum.setCost(jsonObject.getString("cost"));
                        datum.setCategory(jsonObject.getString("category"));
                        serviceList.add(datum);


                    } catch (JSONException e) {

                        e.printStackTrace();

                    }
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(request);*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        getServicesFromDatabase();
        getServices();

    }

    private void getProfServices()
    {
        realm = Realm.getDefaultInstance();
        final RealmHelper helper = new RealmHelper(realm);

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

    public void getServicesFromApi()
    {

        //volley json request
       // volleyJsonRequest();
        getServices();

    }

    private void viewData()
    {
        //ADD RESPONSE TO ADAPTER
        serviceAdapter = new ServiceAdapter(getApplicationContext(), results);
        servicesListRv = (RecyclerView) findViewById(R.id.service_list);
        final LinearLayoutManager serviceLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        servicesListRv.setLayoutManager(serviceLayoutManager);
        servicesListRv.setAdapter(serviceAdapter);

        companyServicesListRv = (RecyclerView) findViewById(R.id.company_service_list);

        serviceAdapter = new ServiceAdapter(getApplicationContext(), results);
        final LinearLayoutManager compServiceLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        companyServicesListRv.setLayoutManager(compServiceLayoutManager);
        companyServicesListRv.setAdapter(serviceAdapter);

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

            realm = Realm.getDefaultInstance();
            final RealmTokenHelper helper = new RealmTokenHelper(realm);

            //RETRIEVE
            helper.retreiveFromDB();

            //CHECK IF DATABASE IS EMPTY
            if (helper.refreshDatabase().size() == 0)
            {
                try {

                    final AuthzModule authzModule = AuthorizationManager
                            .config("KeyCloakAuthz", OAuth2AuthorizationConfiguration.class)
                            .setBaseURL(new URL(Constants.HTTP.AUTH_BASE_URL))
                            .setAuthzEndpoint("/auth/realms/ujuzy/protocol/openid-connect/auth")
                            .setAccessTokenEndpoint("/auth/realms/ujuzy/protocol/openid-connect/token")
                            .setAccountId("account")
                            .setClientId("account")
                            .setRedirectURL("https://ujuzy.com")
                            .setScopes(Arrays.asList("openid"))
                            .addAdditionalAuthorizationParam((Pair.create("grant_type", "password")))
                            .asModule();

                    authzModule.requestAccess(this, new org.jboss.aerogear.android.core.Callback<String>() {
                        @Override
                        public void onSuccess(final String data) {

                            //SAVE TOKEN TO REALM DATABASE
                            RealmToken token = new RealmToken();
                            token.setToken(data);

                            StringRequest stringRequest = new StringRequest(Request.Method.GET, USER_PROFILE_JSON_URL, new com.android.volley.Response.Listener<String>() {
                                @Override
                                public void onResponse(String response)
                                {

                                    try {
                                        JSONObject jsonObject = new JSONObject(response);

                                        RealmUser realmService = new  RealmUser();
                                        realmService.setId(jsonObject.getString("id"));
                                        realmService.setFirstname(jsonObject.getString("firstname"));
                                        realmService.setLastname(jsonObject.getString("lastname"));
                                        realmService.setGender(jsonObject.getString("gender"));
                                        realmService.setEmail(jsonObject.getString("email"));
                                        realmService.setCreated_at(jsonObject.getString("created_at"));
                                        realmService.setPhone(jsonObject.getString("phone_number"));
                                        //realmService.setVerified(jsonObject.getString("phone_number"));

                                        JSONObject jsonUserRole = new JSONObject(response).getJSONObject("user_role");
                                        realmService.setUserRole(jsonUserRole.getString("role_name"));

                                        JSONObject jsonUserProfilePic = new JSONObject(response).getJSONObject("profile_pic");
                                        realmService.setProfilePic(jsonUserProfilePic.getString("thumb"));

                                        //SAVE
                                        realm = Realm.getDefaultInstance();
                                        RealmUserHelper helper = new RealmUserHelper(realm);
                                        helper.save(realmService);


                                    } catch (JSONException e) {
                                        //e.printStackTrace();
                                    }

                                }
                            }, new com.android.volley.Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                    authzModule.deleteAccount();

                                }
                            }) {
                                /*@Override
                                protected Map<String,String> getParams(){
                                    Map<String,String> params = new HashMap<String, String>();
                                    params.put("name",userAccount.getUsername());
                                    params.put("phone_number",userAccount.getPassword());
                                    params.put("date", Uri.encode(comment));
                                    params.put("service_id",String.valueOf(postId));
                                    params.put("time",String.valueOf(blogId));
                                    params.put("request_info",String.valueOf(blogId));

                                    return params;
                                }*/

                                @Override
                                public Map<String, String> getHeaders() throws AuthFailureError {
                                    Map<String,String> params = new HashMap<String, String>();
                                    params.put("Authorization","Bearer "+ data);
                                    params.put("Content-Type","application/json");
                                    params.put("Accept","application/json");
                                    return params;
                                }
                            };

                            requestQueue = Volley.newRequestQueue(MainActivity.this);
                            requestQueue.add(stringRequest);

                            realm = Realm.getDefaultInstance();
                            RealmTokenHelper helper = new RealmTokenHelper(realm);
                            helper.save(token);

                            Intent filterPlumbers = new Intent(MainActivity.this, UserProfileActivity.class);
                            filterPlumbers.putExtra("auth_token", data);
                            startActivity(filterPlumbers);

                        }

                        @Override
                        public void onFailure(Exception e) {
                            //Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                            authzModule.deleteAccount();
                        }
                    });


                } catch (MalformedURLException e) {
                   // e.printStackTrace();
                }
                //startActivity(new Intent(UserProfileActivity.this, LoginActivity.class));

            } else {

                try {

                    final AuthzModule authzModule = AuthorizationManager
                            .config("KeyCloakAuthz", OAuth2AuthorizationConfiguration.class)
                            .setBaseURL(new URL(Constants.HTTP.AUTH_BASE_URL))
                            .setAuthzEndpoint("/auth/realms/ujuzy/protocol/openid-connect/auth")
                            .setAccessTokenEndpoint("/auth/realms/ujuzy/protocol/openid-connect/token")
                            .setAccountId("account")
                            .setClientId("account")
                            .setRedirectURL("https://ujuzy.com")
                            .setScopes(Arrays.asList("openid"))
                            .addAdditionalAuthorizationParam((Pair.create("grant_type", "password")))
                            .asModule();


                    authzModule.requestAccess(this, new org.jboss.aerogear.android.core.Callback<String>() {
                        @Override
                        public void onSuccess(final String data) {

                            //SAVE TOKEN TO REALM DATABASE
                            RealmToken token = new RealmToken();
                            token.setToken(data);

                            StringRequest stringRequest = new StringRequest(Request.Method.GET, USER_PROFILE_JSON_URL, new com.android.volley.Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                    try {
                                        JSONObject jsonObject = new JSONObject(response);

                                        RealmUser realmService = new  RealmUser();
                                        realmService.setId(jsonObject.getString("id"));
                                        realmService.setFirstname(jsonObject.getString("firstname"));
                                        realmService.setLastname(jsonObject.getString("lastname"));
                                        realmService.setGender(jsonObject.getString("gender"));
                                        realmService.setEmail(jsonObject.getString("email"));
                                        realmService.setCreated_at(jsonObject.getString("created_at"));
                                        realmService.setPhone(jsonObject.getString("phone_number"));
                                        //realmService.setVerified(jsonObject.getString("phone_number"));

                                        JSONObject jsonUserRole = new JSONObject(response).getJSONObject("user_role");
                                        realmService.setUserRole(jsonUserRole.getString("role_name"));

                                        JSONObject jsonUserProfilePic = new JSONObject(response).getJSONObject("profile_pic");
                                        realmService.setProfilePic(jsonUserProfilePic.getString("thumb"));

                                        //SAVE
                                        realm = Realm.getDefaultInstance();
                                        RealmUserHelper helper = new RealmUserHelper(realm);
                                        helper.save(realmService);


                                    } catch (JSONException e) {
                                       // e.printStackTrace();
                                    }

                                }
                            }, new com.android.volley.Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                    authzModule.deleteAccount();
                                    /*if (error.toString().equals("com.android.volley.AuthFailureError"))
                                    {
                                        authzModule.deleteAccount();
                                    } else {
                                        Toast.makeText(MainActivity.this, error.toString(),Toast.LENGTH_LONG).show();
                                    }*/

                                }
                            }) {
                                /*@Override
                                protected Map<String,String> getParams(){
                                    Map<String,String> params = new HashMap<String, String>();
                                    params.put("Authorization","Bearer "+ data);

                                    return params;
                                }*/

                                @Override
                                public Map<String, String> getHeaders() throws AuthFailureError {
                                    Map<String,String> params = new HashMap<String, String>();
                                    params.put("Authorization","Bearer "+ data);
                                    params.put("Content-Type","application/json");
                                    params.put("Accept","application/json");
                                    return params;
                                }
                            };

                            requestQueue = Volley.newRequestQueue(MainActivity.this);
                            requestQueue.add(stringRequest);

                            realm = Realm.getDefaultInstance();
                            RealmTokenHelper helper = new RealmTokenHelper(realm);
                            helper.save(token);

                            Intent filterPlumbers = new Intent(MainActivity.this, UserProfileActivity.class);
                            filterPlumbers.putExtra("auth_token", data);
                            startActivity(filterPlumbers);

                        }

                        @Override
                        public void onFailure(Exception e) {
                           // Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });


                } catch (MalformedURLException e) {
                    //e.printStackTrace();
                }


            }

            // Handle the camera action   //
        } else if (id == R.id.nav_favourite)
        {
            startActivity(new Intent(MainActivity.this, FavouriteActivity.class));

        } else if (id == R.id.nav_about)
        {

            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://ujuzy.com/professional"));
            startActivity(browserIntent);

        } else if (id == R.id.nav_help)
        {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://ujuzy.com/help"));
            startActivity(browserIntent);

        }  else if (id == R.id.nav_rate)
        {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.ujuzy.ujuzy"));
            startActivity(browserIntent);

        } else if (id == R.id.nav_share)
        {
            Intent myIntent = new Intent(Intent.ACTION_SEND);
            myIntent.setType("text/plain");
            String shareBody ="Ujuzy App";
            String shareSub = "Hi, this is an invitation to download Ujuzy app  https://play.google.com/store/apps/details?id=com.ujuzy.ujuzy";
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
