package com.ujuzy.ujuzy.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.ujuzy.ujuzy.R;
import com.ujuzy.ujuzy.Tabs.AboutService;
import com.ujuzy.ujuzy.Tabs.ReviewsFragment;
import com.ujuzy.ujuzy.map.MapsActivity;

import java.util.HashMap;

public class ServiceActivity extends AppCompatActivity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener{

    String serviceId = "";
    String userId = "";
    String serviceUrl = "";
    String serviceName = "";
    String serviceDetails = "";
    String serviceLocation = "";
    String serviceCreatedBy = "";
    String serviceCreatedAt = "";

    String serviceCost = "";
    String serviceDays = "";
    String serviceHours = "";
    String serviceTravel = "";
    String serviceCategory = "";
    String serviceAddInfo = "";

    String first_name = "";
    String last_name = "";
    //String profile_pic = "";
    String user_role = "";

    private TextView serviceNameTv, serviceLocationTv, serviceCreatedByTv, serviceCreatedAtTv;
    private ImageView share, backBtn, map, settings;

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private SliderLayout mDemoSlider;

    private ViewPager mViewPager;

    private Boolean isFabOpen = false;
    private FloatingActionButton fabLocation,fabReview, fabMenu;
    private Animation fab_open,fab_close,rotate_forward,rotate_backward;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        initWindows();
        initServiceInfo();
        initSlider();
        initToolbarBtns();
        //initFab();

        setSupportActionBar(toolbar);
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

    private void initToolbarBtns()
    {
        share = (ImageView) findViewById(R.id.iv_share);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(Intent.ACTION_SEND);
                myIntent.setType("text/plain");
                String shareBody ="Ujuzy App";
                String shareSub = serviceDetails;
                myIntent.putExtra(Intent.EXTRA_SUBJECT,shareBody);
                myIntent.putExtra(Intent.EXTRA_TEXT,shareSub);
                view.getContext().startActivity(Intent.createChooser(myIntent,"Share this Service"));
            }
        });

        backBtn = (ImageView) findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        map = (ImageView) findViewById(R.id.iv_location);
        map.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ServiceActivity.this, MapsActivity.class));
            }
        });

        settings = (ImageView) findViewById(R.id.iv_settings);
        settings.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Context context = ServiceActivity.this;

                final Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.settings_dialog);
                dialog.setCancelable(true);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.show();

                TextView openReviews = (TextView) dialog.findViewById(R.id.options_reviews);
                openReviews.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view) {

                        startActivity(new Intent(ServiceActivity.this, PostReviewsActivity.class));
                        dialog.dismiss();
                    }
                });

                TextView openProfile = (TextView) dialog.findViewById(R.id.options_profile);
                openProfile.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view) {

                        Intent profileActivity = new Intent(view.getContext(), ProfileActivity.class);
                        profileActivity.putExtra("service_id", serviceId);
                        profileActivity.putExtra("user_id", userId);
                        profileActivity.putExtra("first_name", first_name);
                        profileActivity.putExtra("last_name", last_name);
                        //   profileActivity.putExtra("profile_pic", profile_pic);
                        profileActivity.putExtra("user_role", user_role);
                        startActivity(profileActivity);
                        dialog.dismiss();
                    }
                });

            }
        });

    }

    private void initFab()
    {
        fabMenu = (FloatingActionButton)findViewById(R.id.fabMenu);
        fabMenu.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                animateFAB();
            }
        });
        fabLocation = (FloatingActionButton)findViewById(R.id.fabLocation);
        fabLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ServiceActivity.this, MapsActivity.class));
            }
        });
        fabReview = (FloatingActionButton)findViewById(R.id.fabReview);
        fabReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(ServiceActivity.this, AddGroupActivity.class));
            }
        });
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_backward);
        /*fab.setOnClickListener(MainActivity.this);
        fab1.setOnClickListener(this);
        fab2.setOnClickListener(this);*/

    }

    @Override
    protected void onResume() {
        super.onResume();
        //closeAnimFab();
    }

    public void closeAnimFab() {
        if(isFabOpen){
            fabMenu.startAnimation(rotate_backward);
            fabLocation.startAnimation(fab_close);
            fabLocation.setClickable(false);
            isFabOpen = false;
            Log.d("John", "close");
        }
    }


    public void animateFAB(){

        if(isFabOpen){

            fabMenu.startAnimation(rotate_backward);
            fabLocation.startAnimation(fab_close);
            fabLocation.setClickable(false);
            isFabOpen = false;
            Log.d("John", "close");

        } else {

            fabMenu.startAnimation(rotate_forward);
            fabLocation.startAnimation(fab_open);
            fabLocation.setClickable(true);
            isFabOpen = true;
            Log.d("John","open");

        }
    }

    private void initServiceInfo()
    {

        serviceId = getIntent().getStringExtra("service_id");
        userId = getIntent().getStringExtra("user_id");
        serviceUrl = getIntent().getStringExtra("service_url");
        serviceName = getIntent().getStringExtra("service_name");
        serviceDetails = getIntent().getStringExtra("service_details");
        serviceLocation = getIntent().getStringExtra("service_location");
        serviceCreatedBy = getIntent().getStringExtra("service_createdby");
        serviceCreatedAt = getIntent().getStringExtra("service_created_at");

        serviceCost = getIntent().getStringExtra("service_cost");
        serviceDays = getIntent().getStringExtra("service_duration_days");
        serviceHours = getIntent().getStringExtra("service_duration_hours");
        serviceTravel = getIntent().getStringExtra("service_travel");
        serviceCategory = getIntent().getStringExtra("service_category");
        serviceAddInfo = getIntent().getStringExtra("service_additional_info");

        first_name = getIntent().getStringExtra("first_name");
        last_name = getIntent().getStringExtra("last_name");
        user_role = getIntent().getStringExtra("user_role");
      //  profile_pic = getIntent().getStringExtra("profile_pic");

       /* serviceCreatedAtTv = (TextView) findViewById(R.id.tv_created_at);
        serviceCreatedByTv = (TextView) findViewById(R.id.tv_created_by);
        serviceLocationTv = (TextView) findViewById(R.id.tv_service_location);*/
        serviceNameTv = (TextView) findViewById(R.id.tv_service_name);
        serviceLocationTv = (TextView) findViewById(R.id.tv_service_location);
        //serviceCreatedAtTv = (TextView) findViewById(R.id.tv_created_by);

        serviceNameTv.setText(serviceName);
        serviceLocationTv.setText(serviceLocation);


    }

    private void initSlider()
    {
        mDemoSlider = (SliderLayout)findViewById(R.id.slider);

        HashMap<String,String> url_maps = new HashMap<String, String>();
        url_maps.put("", serviceUrl);
       /* url_maps.put("", "http://tvfiles.alphacoders.com/100/hdclearart-10.png");
        url_maps.put("", "http://cdn3.nflximg.net/images/3093/2043093.jpg");
        url_maps.put("", "http://images.boomsbeat.com/data/images/full/19640/game-of-thrones-season-4-jpg.jpg");*/


        for(String name : url_maps.keySet()){
            DefaultSliderView textSliderView = new DefaultSliderView(ServiceActivity.this);
            // initialize a SliderLayout
            textSliderView
                    .description(name)
                    .image(url_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit);
                    //.setOnSliderClickListener(this);


            textSliderView.bundle(new Bundle());
            // textSliderView.getBundle().putString("extra",name);

            mDemoSlider.addSlider(textSliderView);
        }

        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        //mDemoSlider.setDuration(4000);
        mDemoSlider.addOnPageChangeListener(this);

        /*HashMap<String,Integer> file_maps = new HashMap<String, Integer>();
        file_maps.put("", R.drawable.plumbing_1);
        file_maps.put("", R.drawable.plumbing_2);
        //file_maps.put("", R.drawable.plumbing_3);
       // file_maps.put("", R.drawable.plumbing_3);


        for(String name : file_maps.keySet())
        {
            DefaultSliderView textSliderView = new DefaultSliderView(ServiceActivity.this);
            // initialize a SliderLayout
            textSliderView
                    .description(name)
                    .image(file_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit);
                    *//*.setOnSliderClickListener(this);*//*


            textSliderView.bundle(new Bundle());
            // textSliderView.getBundle().putString("extra",name);

            mDemoSlider.addSlider(textSliderView);
        }
        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(4000);
        mDemoSlider.addOnPageChangeListener(this);*/

    }


    private void initWindows()
    {
        Window window = ServiceActivity.this.getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            window.setStatusBarColor(ContextCompat.getColor( ServiceActivity.this,R.color.colorPrimaryDark));

            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        }

    }

    @Override
    protected void onStop() {
        // To prevent a memory leak on rotation, make sure to call stopAutoCycle() on the slider before activity or fragment is destroyed
        mDemoSlider.stopAutoCycle();
        super.onStop();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        switch (item.getItemId())
        {

            case android.R.id.home:
                this.finish();
                return true;
            default:

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
    {
       /* switch (position) {
            case 0:
                fabMenu.show();
                fabReview.hide();
                closeAnimFab();
                break;
            case 1:
                fabMenu.hide();
                fabReview.show();
                closeAnimFab();
                break;

            default:
                fabMenu.show();
                break;
        }*/
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    public static class PlaceholderFragment extends Fragment {

        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {

        }

        public static PlaceholderFragment newInstance(int sectionNumber)
        {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.activity_service, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
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
                    bundle.putString("serviceCreatedAt", serviceCreatedAt);
                    bundle.putString("serviceCost", serviceCost);
                    bundle.putString("serviceTravel", serviceTravel);
                    bundle.putString("serviceCategory", serviceCategory);
                    bundle.putString("serviceDays", serviceDays);
                    bundle.putString("serviceHours", serviceHours);
                    bundle.putString("serviceAddInfo", serviceAddInfo);

                    bundle.putString("first_name", first_name);
                    bundle.putString("last_name", last_name);
                   // bundle.putString("profile_pic", profile_pic);
                    bundle.putString("user_role", user_role);
                    tab1.setArguments(bundle);
                    return tab1;
                case 1:
                    ReviewsFragment tab2 = new ReviewsFragment ();
                    Bundle bundle2 = new Bundle();
                    bundle2.putString("serviceId", serviceId);
                    tab2.setArguments(bundle2);
                    return tab2;

            }
            return null;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Overview";
                case 1:
                    return "Reviews";

            }
            return null;
        }
    }
}
