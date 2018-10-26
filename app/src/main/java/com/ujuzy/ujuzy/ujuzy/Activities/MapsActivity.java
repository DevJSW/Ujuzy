package com.ujuzy.ujuzy.ujuzy.Activities;

import android.Manifest;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.ujuzy.ujuzy.ujuzy.BottomSheetFragments.FavouritesFragments;
import com.ujuzy.ujuzy.ujuzy.BottomSheetFragments.MyJobsFragments;
import com.ujuzy.ujuzy.ujuzy.BottomSheetFragments.MyServicesFragments;
import com.ujuzy.ujuzy.ujuzy.BottomSheetFragments.ProfileFragments;
import com.ujuzy.ujuzy.ujuzy.BottomSheetFragments.ServiceFragments;
import com.ujuzy.ujuzy.ujuzy.CustomData.CustomTypefaceSpan;
import com.ujuzy.ujuzy.ujuzy.R;
import com.ujuzy.ujuzy.ujuzy.Realm.RealmFavouriteHelper;
import com.ujuzy.ujuzy.ujuzy.Realm.RealmFavouritesAdapter;
import com.ujuzy.ujuzy.ujuzy.Realm.RealmHelper;
import com.ujuzy.ujuzy.ujuzy.Realm.RealmLocationHelper;
import com.ujuzy.ujuzy.ujuzy.Realm.RealmSearchAdapter;
import com.ujuzy.ujuzy.ujuzy.Realm.RealmService;
import com.ujuzy.ujuzy.ujuzy.Realm.RealmServiceAdapter;
import com.ujuzy.ujuzy.ujuzy.Realm.RealmServiceImage;
import com.ujuzy.ujuzy.ujuzy.Realm.RealmToken;
import com.ujuzy.ujuzy.ujuzy.Realm.RealmTokenHelper;
import com.ujuzy.ujuzy.ujuzy.Realm.RealmUser;
import com.ujuzy.ujuzy.ujuzy.Realm.RealmUserHelper;
import com.ujuzy.ujuzy.ujuzy.Realm.RealmUserLocation;
import com.ujuzy.ujuzy.ujuzy.Realm.RealmUserService;
import com.ujuzy.ujuzy.ujuzy.Realm.RealmUserServiceAdapter;
import com.ujuzy.ujuzy.ujuzy.Realm.RealmUserServicesHelper;
import com.ujuzy.ujuzy.ujuzy.Services.CalculateDistanceTime;
import com.ujuzy.ujuzy.ujuzy.Services.DirectionsJSONParser;
import com.ujuzy.ujuzy.ujuzy.Services.GPSTracker;
import com.ujuzy.ujuzy.ujuzy.Services.PrefManager;
import com.ujuzy.ujuzy.ujuzy.adapters.ServiceMapMarkerAdapter;
import com.ujuzy.ujuzy.ujuzy.app_intro.SigninOrSkipActivity;
import com.ujuzy.ujuzy.ujuzy.model.Constants;
import com.ujuzy.ujuzy.ujuzy.model.ServiceMarker;

import org.jboss.aerogear.android.authorization.AuthorizationManager;
import org.jboss.aerogear.android.authorization.AuthzModule;
import org.jboss.aerogear.android.authorization.oauth2.OAuth2AuthorizationConfiguration;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmList;
import io.realm.RealmResults;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, NavigationView.OnNavigationItemSelectedListener {

    private static final String MY_API_KEY = "AIzaSyDLhtMsy7x0mDj8a3UWU6KmDZ7jD10-vic";
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private View navHeader;
    private GoogleMap mMap;
    private BottomSheetBehavior mBottomSheetBehavior;
    private Realm realm;
    private RealmChangeListener realmChangeListener;
    private RealmServiceAdapter serviceReamAdapter;
    private RealmSearchAdapter searchRealmAdapter;
    private RealmUserServiceAdapter userServiceReamAdapter;
    private RecyclerView servicesListRv, myServicesListRv, companyServicesListRv, searchListRv;
    private RequestQueue requestQueue;
    private ImageView pullSheetIv, pullDrawer;
    private EditText searchInputEt;

    // index to identify current nav menu item
    public static int navItemIndex = 0;
    private PrefManager prefManager;

    // tags used to attach the fragments
    String phoneInput = "";
    private static final String TAG_HOME = "home";
    private static final String TAG_SERVICES = "my services";
    private static final String TAG_JOBS = "my jobs";
    private static final String TAG_FAVOURITES = "favourites";
    private static final String TAG_SETTINGS = "settings";
    public static String CURRENT_TAG = TAG_HOME;

    // toolbar titles respected to selected nav menu item
    private String[] activityTitles;

    // flag to load home fragment when user presses back key
    private boolean shouldLoadHomeFragOnBackPress = true;
    private Handler mHandler;

    private TextView userName, userPhone, userEmail, createdAt, txtEmail, sheetTitleTv, noFavTv, noServTv, noChatsTv, noJobsTv, tvSeeAllComp, tvSeeAllProf, noService, noMyServices, sheetServiceDetails, sheetServiceAddress, sheetServiceCost, userRole, userFirstName, userLastName;
    private ImageView profileNav, jobNav, servNav, mapNav;
    private RelativeLayout servicesViewRl, profileViewRl, userServicesViewRl, favViewRl, myJobsViewRl, chatsViewRl, searchRely, bottomsheetTitle, searchView, myServicesView;

    private RealmFavouritesAdapter serviceFavRealmAdapter;
    private RecyclerView favouritesListRv, serviceListRv;

    private HashMap<Marker, ServiceMarker> mMarkersHashMap;
    private Map<Marker, ServiceMarker> mMarkersHashMap2 = new HashMap<>();
    private ArrayList<ServiceMarker> mMyMarkersArray = new ArrayList<ServiceMarker>();
    private Context ctx;
    String category_title = "";

    private static final int REQUEST_CODE_PERMISSION = 1;
    String mPermission = Manifest.permission.ACCESS_FINE_LOCATION;
    GPSTracker gps;
    Geocoder geocoder;
    double latitude;
    double longitude;
    List<Address> addresses;
    String city = "";
    String state = "";
    String country = "";
    String postalCode = "";
    private String searchText = "";
    String knownName = "";
    CalculateDistanceTime distance_task;
    Polyline lastPolyline;
    boolean isSecond = false;
    ArrayList<LatLng> listPoints;
    LinearLayout layoutBottomSheet;
    BottomSheetBehavior sheetBehavior;
    private Button requestBtn;
    EditText inputPhoneDialog;
    RealmList<RealmServiceImage> serviceImages;
    RelativeLayout mapRely, serviceRely, profileRely,jobsRely;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        listPoints = new ArrayList<>();
        serviceImages = new RealmList<>();

        initWindows();
        initSignIn();
        fetchServicesFromAPI();
        initNavDrawer();
        initNavigationCustomixation();
        initCalculateDistance();
        getUserLocation(); // <-------------------------------- GET USER LOCATION
        initSearch();
        initHideBottomSheet();
        initBottomNavBar();
        initBottomMapBtn();
        initPhoneNoCheck();

    }

    private void initPhoneNoCheck()
    {
        realm = Realm.getDefaultInstance();
        RealmUser realmUser = realm.where(RealmUser.class).findFirst();
        if (realmUser != null)
        {
            if (realmUser.getPhone() == null)
            {
                // show phone dialog
                // CHECK IF USER IS LOGGED IN FAST
                Context context = MapsActivity.this;

                final Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.phone_dialog);
                dialog.setCancelable(true);
                //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.show();

                inputPhoneDialog = (EditText) dialog.findViewById(R.id.editPhone);

                Button ok = (Button) dialog.findViewById(R.id.okBtn);
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {

                        phoneInput = inputPhoneDialog.getText().toString();
                        if (phoneInput.length() > 10 && phoneInput != null)
                        {
                           // initPhoneConfirmation();
                        }

                    }

                });

            }
        }
    }

    private void initBottomMapBtn()
    {
        mapNav.setImageResource(R.drawable.map_nav_color);
        servNav.setImageResource(R.drawable.briefcase_nav);
        profileNav.setImageResource(R.drawable.profile_nav);
        jobNav.setImageResource(R.drawable.job_nav);
    }

    private void initSearch()
    {
        searchRely = (RelativeLayout) findViewById(R.id.searchRely);
        searchRely.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MapsActivity.this, SearchActivity.class));

            }
        });
    }

    private void initBottomNavBar()
    {
        mapRely = (RelativeLayout) findViewById(R.id.mapRely);
        mapNav = (ImageView) findViewById(R.id.mapNav);
        mapRely.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mapNav.setImageResource(R.drawable.map_nav_color);
                servNav.setImageResource(R.drawable.briefcase_nav);
                profileNav.setImageResource(R.drawable.profile_nav);
                jobNav.setImageResource(R.drawable.job_nav);

            }
        });

        serviceRely = (RelativeLayout) findViewById(R.id.serviceRely);
        servNav = (ImageView) findViewById(R.id.serviceNav);
        serviceRely.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mapNav.setImageResource(R.drawable.map_nav2);
                servNav.setImageResource(R.drawable.briefcase_nav_color);
                profileNav.setImageResource(R.drawable.profile_nav);
                jobNav.setImageResource(R.drawable.job_nav);

                ServiceFragments servicesFragments = new ServiceFragments();
                servicesFragments.show(getSupportFragmentManager(), servicesFragments.getTag());

            }
        });

        profileRely = (RelativeLayout) findViewById(R.id.profileRely);
        profileNav = (ImageView) findViewById(R.id.profileNav);
        profileRely.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mapNav.setImageResource(R.drawable.map_nav2);
                servNav.setImageResource(R.drawable.briefcase_nav);
                profileNav.setImageResource(R.drawable.profile_nav_color);
                jobNav.setImageResource(R.drawable.job_nav);

                ProfileFragments profileFragments = new ProfileFragments();
                profileFragments.show(getSupportFragmentManager(), profileFragments.getTag());
            }
        });

        jobsRely = (RelativeLayout) findViewById(R.id.jobsRely);
        jobNav = (ImageView) findViewById(R.id.jobsNav);
        jobsRely.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mapNav.setImageResource(R.drawable.map_nav2);
                servNav.setImageResource(R.drawable.briefcase_nav);
                profileNav.setImageResource(R.drawable.profile_nav);
                jobNav.setImageResource(R.drawable.job_nav_color);

                MyJobsFragments myJobsFragments = new MyJobsFragments();
                myJobsFragments.show(getSupportFragmentManager(), myJobsFragments.getTag());
            }
        });

    }

    private void initHideBottomSheet()
    {
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserLocation();
    }

    private void initCalculateDistance() {
        distance_task = new CalculateDistanceTime(this);
    }

    private void getUserLocation()
    {

        if (Build.VERSION.SDK_INT >= 23) {

            if (checkSelfPermission(mPermission) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MapsActivity.this,
                        new String[]{mPermission,
                        },
                        REQUEST_CODE_PERMISSION);
                return;
            } else {
                geocoder = new Geocoder(this, Locale.getDefault());

                // create class object
                gps = new GPSTracker(MapsActivity.this);
                // check if GPS enabled
                if (gps.canGetLocation()) {
                    latitude = gps.getLatitude();
                    longitude = gps.getLongitude();
                    // \n is for new line
                    // Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();

                    try {
                        addresses = geocoder.getFromLocation(latitude, longitude, 1);

                        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                        city = addresses.get(0).getLocality();
                        state = addresses.get(0).getAdminArea();
                        country = addresses.get(0).getCountryName();
                        postalCode = addresses.get(0).getPostalCode();
                        knownName = addresses.get(0).getFeatureName();

                        // SAVE USER'S CURRENT LOCATION IN REALM DATABASE
                        RealmUserLocation realmUserLocation = new RealmUserLocation();
                        realmUserLocation.setCity(city);
                        realmUserLocation.setState(state);
                        realmUserLocation.setCountry(country);
                        realmUserLocation.setPostal_code(postalCode);
                        realmUserLocation.setKnown_name(knownName);

                        //SAVE
                        realm = Realm.getDefaultInstance();
                        RealmLocationHelper helper = new RealmLocationHelper(realm);
                        helper.save(realmUserLocation);


                    } catch (IOException e) {
                        e.printStackTrace();


                    }

                } else {
                    // can't get location
                    // GPS or Network is not enabled
                    // Ask user to enable GPS/network in settings
                    gps.showSettingsAlert();
                }
            }
        }

    }

    private void plotMarkers(ArrayList<ServiceMarker> markers) {
        if (markers.size() > 0) {

            for (ServiceMarker myMarker : markers) {

                MarkerOptions markerOption = new MarkerOptions().position(new LatLng(myMarker.getmLatitude(), myMarker.getmLongitude()));
                Bitmap bitmap = createUserBitmap();
                if (bitmap != null) {
                    //markerOption.title("Service");
                    LatLng serviceLoc = new LatLng(myMarker.getmLatitude(), myMarker.getmLongitude());
                    LatLng userLocation = new LatLng(latitude, longitude); // <--------------------- current user location
                    markerOption.icon(BitmapDescriptorFactory.fromBitmap(bitmap));
                    markerOption.anchor(0.5f, 0.907f);

                    mMap.setInfoWindowAdapter(new ServiceMapMarkerAdapter(getApplicationContext(), mMarkersHashMap));
                    //mMap.addMarker(markerOption);
                    mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(myMarker.getmLatitude(), myMarker.getmLongitude()))
                            .icon(BitmapDescriptorFactory.fromBitmap(bitmap))
                            .title(myMarker.getmLabel())
                            .snippet(myMarker.getService_category()));
                    mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                        @Override
                        public void onMapClick(LatLng latLng) {
                           /* layoutBottomSheet = (LinearLayout) findViewById(R.id.location_bottom_sheet);
                            sheetBehavior = BottomSheetBehavior.from(layoutBottomSheet);

                            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED); // <--------------- collapse sheet when map is clicked*/
                            listPoints.clear();

                          /*  if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {

                            } else {
                                sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED); // <--------------- collapse sheet when map is clicked
                            }
*/
                        }
                    });

                    mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                        @Override
                        public boolean onMarkerClick(Marker marker) {

                            listPoints.add(marker.getPosition()); // <-------------------- adding the location of the service marker which has been clicked
                            listPoints.add(userLocation); // <---------------------------- adding the current user location

                            String url = getDirectionsUrl(userLocation, marker.getPosition());
                            DownloadTask downloadTask = new DownloadTask();
                            downloadTask.execute(url);

                          /* if (listPoints.size() == 2 && listPoints != null)
                            {
                                lastPolyline.remove(); //<----------------------------------- REMOVE THE POLYLINE FROM MAP

                            }*/

                            return false;
                        }
                    });
                    // mMap.moveCamera(CameraUpdateFactory.newLatLng(serviceLoc));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(13), 2000, null);

                }
            }

        }
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }


    private void initServiceMarkers() {
        // Initialize the HashMap for Markers and MyMarker object
        mMarkersHashMap = new HashMap<Marker, ServiceMarker>();
        mMarkersHashMap2 = new HashMap<>();

        realm = Realm.getDefaultInstance();

        RealmResults<RealmService> services = realm
                .where(RealmService.class)
                .findAll();

        if (services != null && services.size() > 0)
        {
            for (int i = 0; i < services.size(); i++)
            {
                RealmService service = services.get(i);

                float mLat = (float) service.getLatitude();
                String latitude = Float.toString(mLat);
                float mLong = (float) service.getLongitude();
                String longitude = Float.toString(mLong);

                mMyMarkersArray.add((new ServiceMarker(service.getServiceName(), "", service.getCost(), service.getCity(), service.getFirst_name() + " " + service.getLast_name(), service.getId(), service.getCategory(), service.getServiceDetails(), Double.parseDouble(latitude), Double.parseDouble(longitude))));
            }
        }

        plotMarkers(mMyMarkersArray);
    }


    private void initSignIn() {
        prefManager = new PrefManager(this);
        if (prefManager.isFirstTimeLaunch()) {
            prefManager.setFirstTimeLaunch(false);
            startActivity(new Intent(MapsActivity.this, SigninOrSkipActivity.class));
            finish();
        }
    }

    private void initNavDrawer() {
        mHandler = new Handler();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        // Navigation view header
        navHeader = navigationView.getHeaderView(0);
        //txtName = (TextView) navHeader.findViewById(R.id.name);
        //txtEmail = (TextView) navHeader.findViewById(R.id.email);

        // load nav menu header data
        loadNavHeader();

        // initializing navigation menu
        //setUpNavigationView();

        pullDrawer = (ImageView) findViewById(R.id.pullDrawerIv); // <------------------------- pull nav drawer when clicked
        pullDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(GravityCompat.START);
            }
        });
    }

    private void loadNavHeader() {
        // name, website
        // txtName.setText("John Webi");
        //txtEmail.setText("johnwebi@gmail.com");

        //navigationView.getMenu().getItem(3).setActionView(R.layout.menu_dot);
    }

    /***
     * Returns respected fragment that user
     * selected from navigation menu
     */

    private void loadHomeFragment() {
        // selecting appropriate nav menu item
        //selectNavMenu();

        // set toolbar title
        //setToolbarTitle();

        // if user select the current navigation menu again, don't do anything
        // just close the navigation drawer
        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawer.closeDrawers();

            // show or hide the fab button
            return;
        }

        // Sometimes, when fragment has huge data, screen seems hanging
        // when switching between navigation menus
        // So using runnable, the fragment is loaded with cross fade effect
        // This effect can be seen in GMail app

        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                // update the main content by replacing fragments
                /*Fragment fragment = getHomeFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();*/
            }
        };
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
            return;
        }

        // This code loads home fragment when back key is pressed
        // when user is in other fragment than home
        if (shouldLoadHomeFragOnBackPress) {
            // checking if user is on other navigation menu
            // rather than home
            if (navItemIndex != 0) {
                navItemIndex = 0;
                CURRENT_TAG = TAG_HOME;
                loadHomeFragment();
                return;
            }
        }

        super.onBackPressed();
    }


    private void fetchServicesFromAPI() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                Constants.HTTP.SERVICES_ENDPOINT, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject serviceObj = jsonArray.getJSONObject(i);
                        JSONObject serviceUserObj = jsonArray.getJSONObject(i).getJSONObject("user");
                        JSONObject serviceLocationObj = jsonArray.getJSONObject(i).getJSONObject("location");
                        JSONObject serviceDurationObj = jsonArray.getJSONObject(i).getJSONObject("duration");

                        JSONArray skillList = serviceObj.getJSONArray("skill_list");

                        // ASSIGN DATA TO REALM DATABASE SERVICE

                        RealmService realmService = new RealmService();
                        realmService.setId(serviceObj.getString("id"));
                        realmService.setServiceName(serviceObj.getString("service_name"));
                        realmService.setServiceDetails(serviceObj.getString("service_details"));
                        realmService.setCost(serviceObj.getString("cost"));
                        realmService.setCreatedBy(serviceObj.getString("created_by"));
                        realmService.setCategory(serviceObj.getString("category"));
                        realmService.setCreated_at(serviceObj.getString("created_at"));
                        realmService.setRating(serviceObj.getString("rating"));

                        String img = serviceObj.getString("images");

                        Object json = new JSONTokener(img).nextValue();

                        if (json instanceof JSONObject)
                        {

                            JSONObject serviceImgObj = jsonArray.getJSONObject(i).getJSONObject("images");
                            realmService.setImage(serviceImgObj.getString("thumb"));

                        } else if (json instanceof JSONArray)
                        {

                            JSONArray arrayImages = serviceObj.getJSONArray("images");
                            for (int j = 0; j < arrayImages.length(); j++)
                            {
                                JSONObject imagesObj = arrayImages.getJSONObject(j);
                                realmService.setImage(imagesObj.getString("thumb"));
                                realmService.setImageArray(serviceImages);
                            }

                        }

                        realmService.setUser_role(serviceUserObj.getString("user_role"));
                        realmService.setUser_thumb(serviceUserObj.getString("profile_pic"));
                        realmService.setFirst_name(serviceUserObj.getString("firstname"));
                        realmService.setLast_name(serviceUserObj.getString("lastname"));
                        realmService.setUser_id(serviceUserObj.getString("id"));

                        realmService.setCity(serviceLocationObj.getString("city"));
                        realmService.setLatitude(Double.parseDouble(serviceLocationObj.getString("lat")));
                        realmService.setLongitude(Double.parseDouble(serviceLocationObj.getString("lng")));

                        realmService.setService_duration_days(serviceDurationObj.getString("days"));
                        realmService.setService_duration_hours(serviceDurationObj.getString("hours"));

                        for (int s = 0; s < skillList.length(); s++) {

                        }

                        //SAVE
                        realm = Realm.getDefaultInstance();
                        RealmHelper helper = new RealmHelper(realm);
                        helper.save(realmService);

                        /************************************************** END *************************************************************/

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

        requestQueue = Volley.newRequestQueue(MapsActivity.this);
        requestQueue.add(stringRequest);
    }

    private void getProfServicesFromDatabase() {
        realm = Realm.getDefaultInstance();
        final RealmHelper helper = new RealmHelper(realm);

        //RETRIEVE
        helper.filterRealmDatabase("user_role", "Professional");
        //helper.retreiveFromDB();


        // all services
        servicesListRv = (RecyclerView) findViewById(R.id.service_list);
        serviceReamAdapter = new RealmServiceAdapter(getApplicationContext(), helper.refreshDatabase());

        //RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        final LinearLayoutManager serviceLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        serviceLayoutManager.setReverseLayout(true);
        serviceLayoutManager.setStackFromEnd(true);
        servicesListRv.setLayoutManager(serviceLayoutManager); // <------------------- change layout to list in recyclerview
        servicesListRv.setAdapter(serviceReamAdapter);

        //HANDLE DATA CHANGE FOR REFRESH
        realmChangeListener = new RealmChangeListener() {
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

   /* private void initBottomSheet() {
        View bottomSheet = findViewById(R.id.bottom_sheet);
        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        mBottomSheetBehavior.setPeekHeight(110);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        pullSheetIv = (ImageView) findViewById(R.id.pullSheet);
        pullSheetIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else {
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        });

        mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    pullSheetIv.setImageResource(R.drawable.pull_down);
                } else if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    pullSheetIv.setImageResource(R.drawable.pull_up);
                } else if (newState == BottomSheetBehavior.STATE_HIDDEN) {

                }
            }

            @Override
            public void onSlide(View bottomSheet, float slideOffset) {
            }
        });

    }*/

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    private void initWindows() {

        Window window = MapsActivity.this.getWindow();
        // stop keyboard from show when activity is started.
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(MapsActivity.this, R.color.grey));

            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        getUserLocation(); // <-------------------------------- GET USER LOCATION
        initServiceMarkers(); // <-------------------------- SET UP SERVICE MARKERS ON GOOGLE MAP

        mMap.getUiSettings().setZoomControlsEnabled(true);
        /*if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat)*/

        /***
         *
         * SHOWING CURRENT USER LOCATION ON A MAP POINTER
         *
         */

        LatLng userLocation = new LatLng(latitude, longitude);
        MarkerOptions options2 = new MarkerOptions().position(userLocation);
        Bitmap bitmap2 = UserBitmap();
        if (bitmap2 != null) {
            options2.title("You are here");
            options2.icon(BitmapDescriptorFactory.fromBitmap(bitmap2));
            options2.anchor(0.5f, 0.907f);

            mMap.addMarker(options2);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(userLocation));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(12), 1000, null);
        }

    }

    private Bitmap createUserBitmap() {
        Bitmap result = null;
        try {
            result = Bitmap.createBitmap(dp(62), dp(76), Bitmap.Config.ARGB_8888);
            result.eraseColor(Color.TRANSPARENT);
            Canvas canvas = new Canvas(result);
            Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.livepin_briefcase, null);
            drawable.setBounds(0, 0, dp(42), dp(53));
            drawable.draw(canvas);

            Paint roundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            RectF bitmapRect = new RectF();
            canvas.save();

            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.backbtn_black);
            //Bitmap bitmap = BitmapFactory.decodeFile(path.toString()); /*generate bitmap here if your image comes from any url*/
           /* if (bitmap != null)
            {
                BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
                Matrix matrix = new Matrix();
                float scale = dp(52) / (float) bitmap.getWidth();
                matrix.postTranslate(dp(5), dp(5));
                matrix.postScale(scale, scale);
                roundPaint.setShader(shader);
                shader.setLocalMatrix(matrix);
                bitmapRect.set(dp(5), dp(5), dp(52 + 5), dp(52 + 5));
                canvas.drawRoundRect(bitmapRect, dp(26), dp(26), roundPaint);
            }
            canvas.restore();
            try {
                canvas.setBitmap(null);
            } catch (Exception e) {}*/
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return result;
    }

    private Bitmap UserBitmap() {
        Bitmap result = null;
        try {
            result = Bitmap.createBitmap(dp(62), dp(78), Bitmap.Config.ARGB_8888);
            result.eraseColor(Color.TRANSPARENT);
            Canvas canvas = new Canvas(result);
            Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.livepin_profile, null);
            drawable.setBounds(0, 0, dp(42), dp(53));
            drawable.draw(canvas);

            Paint roundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            RectF bitmapRect = new RectF();
            canvas.save();

            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.backbtn_black);
            //Bitmap bitmap = BitmapFactory.decodeFile(path.toString()); /*generate bitmap here if your image comes from any url*/
           /* if (bitmap != null)
            {
                BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
                Matrix matrix = new Matrix();
                float scale = dp(52) / (float) bitmap.getWidth();
                matrix.postTranslate(dp(5), dp(5));
                matrix.postScale(scale, scale);
                roundPaint.setShader(shader);
                shader.setLocalMatrix(matrix);
                bitmapRect.set(dp(5), dp(5), dp(52 + 5), dp(52 + 5));
                canvas.drawRoundRect(bitmapRect, dp(26), dp(26), roundPaint);
            }
            canvas.restore();
            try {
                canvas.setBitmap(null);
            } catch (Exception e) {}*/
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return result;
    }

    public int dp(float value) {
        if (value == 0) {
            return 0;
        }
        return (int) Math.ceil(getResources().getDisplayMetrics().density * value);
    }

    private void toggleDownBottomSheet() {
        layoutBottomSheet = (LinearLayout) findViewById(R.id.location_bottom_sheet);
        sheetBehavior = BottomSheetBehavior.from(layoutBottomSheet);
        sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    private void initAuthUser() {

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

                    // CHECK IF USER IS LOGGED IN FAST
                    Context context = MapsActivity.this;

                    final Dialog dialog = new Dialog(context);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.sign_in_dialog);
                    dialog.setCancelable(true);
                    //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    dialog.show();

                    TextView sign_in = (TextView) dialog.findViewById(R.id.btn_signin);
                    sign_in.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // initUserProfileSheet(); // <------------------- open user profile bottom sheet
                            initAeroGearSignIn();
                            dialog.dismiss();
                        }
                    });

                    TextView cancel = (TextView) dialog.findViewById(R.id.cancel);
                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });

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

    }

    private void initAeroGearSignIn() {
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

                    StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.HTTP.USER_PROFILE_JSON_URL,
                            new com.android.volley.Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                    try {

                                        JSONObject jsonObject = new JSONObject(response);

                                        RealmUser realmService = new RealmUser();
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
                                        //JSONObject jsonUserProfilePic = new JSONObject(response).getJSONObject("profile_pic");
                                        //realmService.setProfilePic(jsonUserProfilePic.getString("thumb"));

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

                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("Authorization", "Bearer " + data);
                            params.put("Content-Type", "application/json");
                            params.put("Accept", "application/json");
                            return params;
                        }
                    };

                    requestQueue = Volley.newRequestQueue(MapsActivity.this);
                    requestQueue.add(stringRequest);

                    realm = Realm.getDefaultInstance();
                    RealmTokenHelper helper = new RealmTokenHelper(realm);
                    helper.save(token);


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
        // startActivity(new Intent(UserProfileActivity.this, LoginActivity.class));

    }


    // create method to get full url to request direction from google map api.
    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        String str_origin = "origin=" + origin.latitude + "," + origin.longitude; // <--------------------- value of the origin


        String str_dest = "destination=" + dest.latitude + "," + dest.longitude; // <------------------------ value of destination


        String sensor = "sensor=false";

        String mode = "mode=driving"; // <---------------------------- set mode for finding direction


        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + mode;   // <--------------------------- set the full param

        String output = "json";


        return "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + MY_API_KEY;
    }

    //  creating method requestDirections to get directions, using httpurlConnection
    public static String downloadUrl(String strUrl) throws IOException {
        String data = "";
        HttpURLConnection urlConnection;
        URL url = new URL(strUrl);

        // Creating an http connection to communicate with url
        urlConnection = (HttpURLConnection) url.openConnection();

        // Connecting to url
        urlConnection.connect();

        // Reading data from url
        try (InputStream iStream = urlConnection.getInputStream()) {
            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuilder sb = new StringBuilder();

            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exception while down", e.toString());
        } finally {
            urlConnection.disconnect();
        }
        return data;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_fav)
        {
            FavouritesFragments favouritesFragments = new FavouritesFragments();
            favouritesFragments.show(getSupportFragmentManager(), favouritesFragments.getTag());

        } else if (id == R.id.nav_services)
        {

            MyServicesFragments myServicesFragments = new MyServicesFragments();
            myServicesFragments.show(getSupportFragmentManager(), myServicesFragments.getTag());

        } else if (id == R.id.nav_create_services)
        {

            toggleDownBottomSheet();
            initCreateService();

        } else if (id == R.id.nav_share)
        {

            toggleDownBottomSheet();
            Intent myIntent = new Intent(Intent.ACTION_SEND);
            myIntent.setType("text/plain");
            String shareBody ="Ujuzy App";
            String shareSub = "Hi, this is an invitation to download Ujuzy app  https://play.google.com/store/apps/details?id=com.ujuzy.ujuzy";
            myIntent.putExtra(Intent.EXTRA_SUBJECT,shareBody);
            myIntent.putExtra(Intent.EXTRA_TEXT,shareSub);
            startActivity(Intent.createChooser(myIntent,"Share this app"));


        } else if (id == R.id.nav_rate)
        {
            toggleDownBottomSheet();
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.ujuzy.ujuzy"));
            startActivity(browserIntent);

        }

        toggleDownBottomSheet();
        drawer.closeDrawer(GravityCompat.START);
        return false;
    }

    private void initCreateService()
    {
        startActivity(new Intent(MapsActivity.this, CreateServiceActivity.class));
    }


    // creating asyncTask to call request direction
    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }

    // asyncTask to pass json response
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result)
        {
            ArrayList<LatLng> points = new ArrayList<>();
            PolylineOptions lineOptions = new PolylineOptions();
            for (int i = 0; i < result.size(); i++) {
                List<HashMap<String, String>> path = result.get(i);
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);
                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);
                    points.add(position);
                }

                lineOptions.addAll(points);
                lineOptions.width(5);
                lineOptions.color(R.color.colorPrimary);
                if (isSecond) {
                    lastPolyline.remove();
                    lastPolyline = mMap.addPolyline(lineOptions);
                }
                else {
                    lastPolyline = mMap.addPolyline(lineOptions);
                    isSecond = true;
                }
            }
        }
    }

    private void initNavigationCustomixation()
    {

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        View hView = navigationView.getHeaderView(0);

        userName = hView.findViewById(R.id.tv_user_name);
        userEmail = hView.findViewById(R.id.tv_user_email);

        realm = Realm.getDefaultInstance();
        RealmUser user = realm.where(RealmUser.class).findFirst();
        if (user != null) {
            userName.setText(user.getFirstname() + " " + user.getLastname());
            userEmail.setText(user.getEmail());
        }

        navigationView.setNavigationItemSelectedListener(this);

        Menu m = navigationView.getMenu();
        for (int i=0;i<m.size();i++)
        {
            MenuItem mi = m.getItem(i);
            applyFontToMenuItem(mi);

            //for applying a font to subMenu ...
            SubMenu subMenu = mi.getSubMenu();
            if (subMenu != null && subMenu.size() > 0)
            {
                for (int j = 0; j < subMenu.size(); j++)
                {
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

    /*private void initUserInfo()
    {

        userName = (TextView) findViewById(R.id.tv_user_name);
        userEmail = (TextView) findViewById(R.id.tv_user_email);
        userPhone = (TextView) findViewById(R.id.tv_user_phone);
        createdAt = (TextView) findViewById(R.id.tv_created_at);
        userRole = (TextView) findViewById(R.id.tv_user_role);
        userFirstName = (TextView) findViewById(R.id.tv_user_firstname);
        userLastName = (TextView) findViewById(R.id.tv_user_lastname);

        realm = Realm.getDefaultInstance();
        RealmUser realmUser = realm.where(RealmUser.class).findFirst();
        if (realmUser != null) {
            if (realmUser.getFirstname() != null)
                userName.setText(realmUser.getFirstname() + " " + realmUser.getLastname());
            if (realmUser.getEmail() != null)
                userEmail.setText(realmUser.getEmail());
            if (realmUser.getPhone() != null)
                userPhone.setText(realmUser.getPhone());
            if (realmUser.getCreated_at() != null)
                createdAt.setText(realmUser.getCreated_at());
            if (realmUser.getUserRole() != null)
                userRole.setText(realmUser.getUserRole());
            if (realmUser.getFirstname() != null)
                userFirstName.setText(realmUser.getFirstname());
            if (realmUser.getLastname() != null)
                userLastName.setText(realmUser.getLastname());

        }

    }*/

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        realm = Realm.getDefaultInstance();
        if (realmChangeListener != null)
            realm.removeChangeListener(realmChangeListener);
    }

}
