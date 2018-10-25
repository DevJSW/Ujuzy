package com.ujuzy.ujuzy.ujuzy.Activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.ujuzy.ujuzy.ujuzy.R;
import com.ujuzy.ujuzy.ujuzy.Realm.RealmFavourite;
import com.ujuzy.ujuzy.ujuzy.Realm.RealmFavouriteHelper;

import Tabs.AboutService;
import Tabs.PhotoFragment;
import Tabs.ReviewsFragment;
import Tabs.SkillsFragment;
import io.realm.Realm;

public class ServiceTabbedActivity extends AppCompatActivity {

    String serviceId = "";
    String userId = "";
    String user_role = "";
    String serviceUrl = "";
    String serviceName = "";
    String serviceDetails = "";
    String serviceLocation = "";
    String serviceCreatedBy = "";
    String serviceCreatedAt = "";
    String no_of_personnel = "";
    String ratings = "";

    String serviceCost = "";
    String serviceDays = "";
    String serviceHours = "";
    String serviceTravel = "";
    String serviceCategory = "";
    String serviceAddInfo = "";
    String serviceLatitude = "";
    String serviceLongitude = "";

    String first_name = "";
    String last_name = "";
    String profile_pic = "";

    private String webview_url = "https://ujuzy.com/#";

    private Realm realm;

    CollapsingToolbarLayout collapsingToolbarLayout;

    private TextView serviceNameTv, serviceLocationTv, serviceCreatedByTv, serviceCreatedAtTv;
    private ImageView share, backBtn, map, settings, serviceImage;

    private ImageView backBtnIv;

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;
    private RatingBar serviceRatingBr;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_tabbed);

        initWindows();
        initTabs();
        initBackBtn();
        initServiceInfo();
        initRatings();

    }

    private void initRatings()
    {
        serviceRatingBr = (RatingBar) findViewById(R.id.serviceRating);
        serviceRatingBr.setRating(Float.parseFloat(ratings));
    }

    private void initWindows()
    {
        Window window = ServiceTabbedActivity.this.getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            window.setStatusBarColor(ContextCompat.getColor( ServiceTabbedActivity.this,R.color.colorPrimaryDark));
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        }

    }

    private void initTabs()
    {
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
    }

    private void initBackBtn()
    {
        backBtnIv = (ImageView) findViewById(R.id.backBtn);
        backBtnIv.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });
    }

    private void initServiceInfo()
    {

        serviceId = getIntent().getStringExtra("service_id");
        userId = getIntent().getStringExtra("user_id");
        serviceUrl = getIntent().getStringExtra("service_url");
        serviceName = getIntent().getStringExtra("service_name");
        serviceDetails = getIntent().getStringExtra("service_detail");
        serviceLocation = getIntent().getStringExtra("service_location");
        serviceCreatedBy = getIntent().getStringExtra("service_createdby");
        serviceCreatedAt = getIntent().getStringExtra("service_created_at");

        serviceCost = getIntent().getStringExtra("service_cost");
        no_of_personnel = getIntent().getStringExtra("no_of_personnel");
        serviceDays = getIntent().getStringExtra("service_duration_days");
        serviceHours = getIntent().getStringExtra("service_duration_hours");
        serviceTravel = getIntent().getStringExtra("service_travel");
        serviceCategory = getIntent().getStringExtra("service_category");
        serviceAddInfo = getIntent().getStringExtra("service_additional_info");
        ratings = getIntent().getStringExtra("service_ratings");

        serviceLatitude = getIntent().getStringExtra("service_latitude");
        serviceLongitude = getIntent().getStringExtra("service_longitude");

        first_name = getIntent().getStringExtra("first_name");
        last_name = getIntent().getStringExtra("last_name");
        user_role = getIntent().getStringExtra("user_role");
        profile_pic = getIntent().getStringExtra("profile_pic");

       /* serviceCreatedAtTv = (TextView) findViewById(R.id.tv_created_at);
        serviceCreatedByTv = (TextView) findViewById(R.id.tv_created_by);
        serviceLocationTv = (TextView) findViewById(R.id.tv_service_location);*/

        serviceNameTv = (TextView) findViewById(R.id.tv_service_name);
        serviceLocationTv = (TextView) findViewById(R.id.tv_service_location);
        //serviceCreatedAtTv = (TextView) findViewById(R.id.tv_created_by);

        serviceNameTv.setText("Category: " +serviceCategory);
        serviceLocationTv.setText( serviceLocation);

        serviceImage = (ImageView) findViewById(R.id.service_avatar);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.cardInfo_collapsing);

        collapsingToolbarLayout.setTitle(serviceName);
        final Typeface aller_font = Typeface.createFromAsset(getAssets(), "fonts/Aller_Rg.ttf");
        collapsingToolbarLayout.setExpandedTitleTypeface(aller_font);

        Glide.with(getApplicationContext())
                .load(serviceUrl)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .fitCenter()
                .into(new BitmapImageViewTarget(serviceImage) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(getApplicationContext().getResources(), resource);
                        circularBitmapDrawable.setCircular(false);
                        serviceImage.setImageDrawable(circularBitmapDrawable);
                    }
                });

        FloatingActionButton call = (FloatingActionButton) findViewById(R.id.fab);
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Context context = ServiceTabbedActivity.this;

                final Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.settings_dialog);
                dialog.setCancelable(true);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.show();

                TextView openShare = (TextView) dialog.findViewById(R.id.options_share);
                openShare.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view) {

                        Intent myIntent = new Intent(Intent.ACTION_SEND);
                        myIntent.setType("text/plain");
                        String shareBody ="Ujuzy App";
                        String shareSub = serviceDetails;
                        myIntent.putExtra(Intent.EXTRA_SUBJECT,shareBody);
                        myIntent.putExtra(Intent.EXTRA_TEXT,shareSub);
                        view.getContext().startActivity(Intent.createChooser(myIntent,"Share this Service"));

                        dialog.dismiss();
                    }
                });

                TextView openMap = (TextView) dialog.findViewById(R.id.options_map);
                openMap.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view) {

                        Intent openMaps = new Intent(ServiceTabbedActivity.this, MapsActivity.class);
                        openMaps.putExtra("service_latitude",serviceLatitude);
                        openMaps.putExtra("service_longitude",serviceLongitude);
                        startActivity(openMaps);

                        dialog.dismiss();
                    }
                });

                TextView favourite = (TextView) dialog.findViewById(R.id.options_favourite);
                favourite.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view) {

                        RealmFavourite realmService = new  RealmFavourite();

                        realmService.setId(serviceId);
                        realmService.setServiceName(serviceName);
                        realmService.setServiceDetails(serviceDetails);
                        realmService.setCost(serviceCost);
                        realmService.setCreatedBy(serviceCreatedBy);
                        realmService.setCategory(serviceCategory);
                        realmService.setTravel(serviceTravel);
                        realmService.setImage(serviceUrl);
                        realmService.setFirst_name(first_name);
                        realmService.setLast_name(last_name);
                        realmService.setUser_role(user_role);
                       /* realmService.setLongitude(serviceLongitude);
                        realmService.setLatitude((Double) serviceLatitude);*/
                        realmService.setService_duration_days(serviceDays);
                        realmService.setService_duration_hours(serviceHours);

                        //SAVE
                        realm = Realm.getDefaultInstance();
                        RealmFavouriteHelper helper = new RealmFavouriteHelper(realm);
                        helper.save(realmService);

                        dialog.dismiss();

                        Toast.makeText(ServiceTabbedActivity.this, "Services added to favourites", Toast.LENGTH_LONG).show();

                    }
                });

               /* TextView openReport = (TextView) dialog.findViewById(R.id.options_report);
                openReport.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view) {
                        Intent webView = new Intent(ServiceTabbedActivity.this, WebViewActivity.class);
                        webView.putExtra("webview_url", webview_url);
                        webView.putExtra("page_titile", "Forgot Password");
                        startActivity(webView);
                        dialog.dismiss();
                    }
                });
*/
                /*TextView openRequest = (TextView) dialog.findViewById(R.id.options_request);
                openRequest.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view) {

                        Intent requestActivity = new Intent(ServiceTabbedActivity.this, RequestServiceActivity.class);
                        requestActivity.putExtra("service_id", serviceId);
                        requestActivity.putExtra("first_name", first_name);
                        requestActivity.putExtra("last_name", last_name);
                        requestActivity.putExtra("service_cost", serviceCost);
                        requestActivity.putExtra("service_name", serviceName);
                        requestActivity.putExtra("no_of_personnel", no_of_personnel);
                        requestActivity.putExtra("profile_pic", profile_pic);
                        startActivity(requestActivity);
                        dialog.dismiss();
                    }
                });*/

                /*TextView openProfile = (TextView) dialog.findViewById(R.id.options_profile);
                openProfile.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view) {

                        Intent profileActivity = new Intent(view.getContext(), ProfileActivity.class);
                        profileActivity.putExtra("service_id", serviceId);
                        profileActivity.putExtra("user_id", userId);
                        profileActivity.putExtra("first_name", first_name);
                        profileActivity.putExtra("last_name", last_name);
                        profileActivity.putExtra("profile_pic", profile_pic);
                        profileActivity.putExtra("user_role", user_role);
                        startActivity(profileActivity);
                        dialog.dismiss();
                    }
                });*/

            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_service_tabbed, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_service_tabbed, container, false);
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position)
        {

            initServiceInfo();

            //returning the current tabs
            switch (position) {
                case 0:
                    AboutService tab1 = new AboutService();
                    Bundle bundle = new Bundle();
                    bundle.putString("serviceId", serviceId);
                    bundle.putString("serviceDetails", serviceDetails);
                    bundle.putString("serviceCreatedBy", serviceCreatedBy);
                    bundle.putString("serviceName", serviceName);
                    bundle.putString("serviceCreatedAt", serviceCreatedAt);
                    bundle.putString("serviceCost", serviceCost);
                    bundle.putString("serviceTravel", serviceTravel);
                    bundle.putString("serviceCategory", serviceCategory);
                    bundle.putString("serviceDays", serviceDays);
                    bundle.putString("serviceHours", serviceHours);
                    bundle.putString("serviceAddInfo", serviceAddInfo);
                    bundle.putString("no_of_personnel", no_of_personnel);

                    bundle.putString("first_name", first_name);
                    bundle.putString("last_name", last_name);
                    bundle.putString("profile_pic", profile_pic);
                    bundle.putString("user_role", user_role);
                    tab1.setArguments(bundle);
                    return tab1;

                case 1:
                    SkillsFragment tab3 = new SkillsFragment ();
                    Bundle bundle3 = new Bundle();
                    bundle3.putString("serviceId", serviceId);
                    tab3.setArguments(bundle3);
                    return tab3;

                case 2:
                    ReviewsFragment tab2 = new ReviewsFragment ();
                    Bundle bundle2 = new Bundle();
                    bundle2.putString("serviceId", serviceId);
                    tab2.setArguments(bundle2);
                    return tab2;


                case 3:
                    PhotoFragment tab4 = new PhotoFragment ();
                    Bundle bundle4 = new Bundle();
                    bundle4.putString("serviceId", serviceId);
                    bundle4.putString("serviceUrl", serviceUrl);
                    tab4.setArguments(bundle4);
                    return tab4;

            }
            return null;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Overview";
                case 1:
                    return "Skills";
                case 2:
                    return "Reviews";
                case 4:
                    return "Photos";

            }
            return null;
        }
    }
}
