package com.ujuzy.ujuzy.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.ujuzy.ujuzy.R;
import com.ujuzy.ujuzy.Realm.RealmHelper;
import com.ujuzy.ujuzy.Realm.RealmToken;
import com.ujuzy.ujuzy.Realm.RealmTokenHelper;
import com.ujuzy.ujuzy.Realm.RealmUserServiceAdapter;
import com.ujuzy.ujuzy.Tabs.AboutUserFragment;
import com.ujuzy.ujuzy.Tabs.ReviewsFragment;
import com.ujuzy.ujuzy.Tabs.ServicesFragment;
import com.ujuzy.ujuzy.Tabs.UserRequestedServiceFragment;
import com.ujuzy.ujuzy.Tabs.UserServicesFragment;
import com.ujuzy.ujuzy.adapters.Service2Adapter;
import com.ujuzy.ujuzy.adapters.ServiceAdapter;
import com.ujuzy.ujuzy.model.Constants;
import com.ujuzy.ujuzy.model.Datum;
import com.ujuzy.ujuzy.model.Service;
import com.ujuzy.ujuzy.services.Api;

import org.jboss.aerogear.android.authorization.AuthorizationManager;
import org.jboss.aerogear.android.authorization.AuthzModule;
import org.jboss.aerogear.android.authorization.oauth2.OAuth2AuthorizationConfiguration;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserProfileActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    String service_id = "";
    String user_id = "";

    String first_name = "";
    String last_name = "";
    String profile_pic = "";
    String user_role = "";

    private TextView firstNameTv, lastNameTv, userRoleTv, noService;
    private ImageView profilePicIv, backBtn;

    private Service2Adapter userServiceAdapter;
    private Realm realm;
    private Retrofit retrofit;
    ArrayList<Datum> results;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

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
        initRetrofit();
       // initRealm();

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

    private void initRetrofit()
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

            }
        });
    }

    private void viewData()
    {
        //ADD RESPONSE TO ADAPTER
        userServiceAdapter = new Service2Adapter(getApplicationContext(), results);

    }

    @Override
    protected void onResume() {
        super.onResume();
       // initRealm();
    }

    private void initRealm() {
        realm = Realm.getDefaultInstance();
        final RealmTokenHelper helper = new RealmTokenHelper(realm);

        //RETRIEVE
        helper.retreiveFromDB();

        //CHECK IF DATABASE IS EMPTY
        if (helper.refreshDatabase().size() == 0)
        {
            try {

                AuthzModule authzModule = AuthorizationManager
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
                    public void onSuccess(String data) {

                        //SAVE TOKEN TO REALM DATABASE
                        RealmToken token = new RealmToken();
                        token.setToken(data);

                        realm = Realm.getDefaultInstance();
                        RealmTokenHelper helper = new RealmTokenHelper(realm);
                        helper.save(token);

                    }

                    @Override
                    public void onFailure(Exception e) {
                        System.err.println("Error!!");
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        finish();
                    }
                });


            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            //startActivity(new Intent(UserProfileActivity.this, LoginActivity.class));

        } else {

        }
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
        Window window = UserProfileActivity.this.getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor( UserProfileActivity.this,R.color.colorPrimaryDark));

            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        }

    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.profile_menu, menu);
        return true;
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

       /* //noinspection SimplifiableIfStatement
        if (id == R.id.action_update)
        {
            return true;
        }*/

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
                    AboutUserFragment tab1 = new AboutUserFragment ();
                    return tab1;
                case 1:
                    UserServicesFragment tab2 = new UserServicesFragment ();
                    Bundle bundle2 = new Bundle();
                    //bundle2.putString("UserId", UserId);
                    bundle2.putString("userId", user_id);
                    bundle2.putString("serviceId", service_id);
                    bundle2.putString("firstName", first_name);
                    tab2.setArguments(bundle2);
                    return tab2;

                case 2:
                    UserRequestedServiceFragment tab4 = new UserRequestedServiceFragment ();
                    return tab4;
                case 3:
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
            // Show 4 total pages.
            return 4;
        }

    }



}