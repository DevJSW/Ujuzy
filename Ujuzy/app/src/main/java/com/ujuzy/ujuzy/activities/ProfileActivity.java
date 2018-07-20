package com.ujuzy.ujuzy.activities;

import android.graphics.Bitmap;
import android.os.Build;
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
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.ujuzy.ujuzy.R;
import com.ujuzy.ujuzy.Tabs.AboutService;
import com.ujuzy.ujuzy.Tabs.AboutUserFragment;
import com.ujuzy.ujuzy.Tabs.ReviewsFragment;
import com.ujuzy.ujuzy.Tabs.ServicesFragment;

public class ProfileActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    String service_id = "";
    String user_id = "";

    String first_name = "";
    String last_name = "";
    String profile_pic = "";
    String user_role = "";

    private TextView firstNameTv, lastNameTv, userRoleTv;
    private ImageView profilePicIv, backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initWindows();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
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

        initUserInfo();
        initBackBtn();

    }

    private void initBackBtn()
    {
        backBtn = (ImageView) findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });
    }

    private void initUserInfo()
    {

        service_id = getIntent().getStringExtra("service_id");
        user_id = getIntent().getStringExtra("user_id");

        first_name = getIntent().getStringExtra("first_name");
        last_name = getIntent().getStringExtra("last_name");
        user_role = getIntent().getStringExtra("user_role");
        profile_pic = getIntent().getStringExtra("profile_pic");

        firstNameTv = (TextView) findViewById(R.id.tv_first_name);
        lastNameTv = (TextView) findViewById(R.id.tv_last_name);
        userRoleTv = (TextView) findViewById(R.id.tv_user_role);
        profilePicIv = (ImageView) findViewById(R.id.user_avator);

        firstNameTv.setText(first_name);
        lastNameTv.setText(last_name);
        userRoleTv.setText(user_role);

        if (profile_pic != null)
        {

            Glide.with(getApplicationContext())
                    .load(profile_pic).asBitmap()
                    .placeholder(R.drawable.if_profile_white)
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .centerCrop()
                    .into(new BitmapImageViewTarget(profilePicIv) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable =
                                    RoundedBitmapDrawableFactory.create(getApplicationContext().getResources(), resource);
                            circularBitmapDrawable.setCircular(true);
                            profilePicIv.setImageDrawable(circularBitmapDrawable);
                        }
                    });
        }
    }

    private void initWindows()
    {
        Window window = ProfileActivity.this.getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor( ProfileActivity.this,R.color.colorPrimaryDark));

            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        switch (item.getItemId()) {

            case android.R.id.home:
                this.finish();
                return true;
            default:

        }

        return super.onOptionsItemSelected(item);
    }


    public static class PlaceholderFragment extends Fragment {

        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {

        }

        public static ServiceActivity.PlaceholderFragment newInstance(int sectionNumber) {
            ServiceActivity.PlaceholderFragment fragment = new ServiceActivity.PlaceholderFragment();
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
    public class SectionsPagerAdapter extends FragmentPagerAdapter
    {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position)
        {

            //returning the current tabs
            switch (position) {
                case 0:
                    ServicesFragment tab2 = new ServicesFragment ();
                    Bundle bundle2 = new Bundle();
                    //bundle2.putString("UserId", UserId);
                    bundle2.putString("userId", user_id);
                    bundle2.putString("serviceId", service_id);
                    tab2.setArguments(bundle2);
                    return tab2;
                case 1:
                    ReviewsFragment tab3 = new ReviewsFragment ();
                    Bundle bundle3 = new Bundle();
                    bundle3.putString("serviceId", service_id);
                    bundle3.putString("userId", user_id);
                    tab3.setArguments(bundle3);
                    return tab3;


            }
            return null;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }

    }
}
