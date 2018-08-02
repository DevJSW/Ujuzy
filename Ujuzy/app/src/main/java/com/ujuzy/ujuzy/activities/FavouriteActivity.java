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
import android.widget.TextView;

import com.ujuzy.ujuzy.R;
import com.ujuzy.ujuzy.Realm.RealmAllServiceAdapter;
import com.ujuzy.ujuzy.Realm.RealmFavouriteHelper;
import com.ujuzy.ujuzy.Realm.RealmFavouritesAdapter;
import com.ujuzy.ujuzy.Realm.RealmHelper;

import io.realm.Realm;
import io.realm.RealmChangeListener;

public class FavouriteActivity extends AppCompatActivity
{

    private TextView noService, title;
    private ImageView backBtn;

    private Realm realm;
    private RealmChangeListener realmChangeListener;
    private RealmFavouritesAdapter serviceRealmAdapter;
    private RecyclerView favouritesListRv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);

        initWindow();
        initBack();
        initTitle();
        initRealm();

    }

    private void initRealm()
    {
        realm = Realm.getDefaultInstance();
        final RealmFavouriteHelper helper = new RealmFavouriteHelper(realm);

        //QUERY/FILTER REALM DATABASE
        helper.retreiveFromDB();

        //CHECK IF DATABASE IS EMPTY
        if (helper.refreshDatabase().size() < 1 || helper.refreshDatabase().size() == 0)
        {
            noService = (TextView) findViewById(R.id.noService);
            noService.setVisibility(View.VISIBLE);
            noService.setText("All your favourite services will be collected here");
        } else {
            noService = (TextView) findViewById(R.id.noService);
            noService.setVisibility(View.GONE);
        }


        favouritesListRv = (RecyclerView) findViewById(R.id.service_list);
        serviceRealmAdapter = new RealmFavouritesAdapter(getApplicationContext(), helper.refreshDatabase());

        //RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        final LinearLayoutManager serviceLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        favouritesListRv.setLayoutManager(serviceLayoutManager);
        favouritesListRv.setAdapter(serviceRealmAdapter);

        //HANDLE DATA CHANGE FOR REFRESH
        realmChangeListener = new RealmChangeListener()
        {
            @Override
            public void onChange(Object o) {
                //REFRESH
                serviceRealmAdapter = new RealmFavouritesAdapter(getApplicationContext(), helper.refreshDatabase());
                favouritesListRv.setAdapter(serviceRealmAdapter);
            }
        };

        //ADD CHANGE LIST TO REALM
        realm.addChangeListener(realmChangeListener);
    }


    private void initBack() {
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

    private void initTitle()
    {
        title = (TextView) findViewById(R.id.toolbarTv);
        title.setText("Favourites");
    }

    private void initWindow()
    {
        Window window = FavouriteActivity.this.getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            window.setStatusBarColor(ContextCompat.getColor( FavouriteActivity.this,R.color.colorPrimaryDark));

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
