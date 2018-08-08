package com.ujuzy.ujuzy.activities;

import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ujuzy.ujuzy.R;
import com.ujuzy.ujuzy.Realm.RealmAllServiceAdapter;
import com.ujuzy.ujuzy.Realm.RealmHelper;
import com.ujuzy.ujuzy.Realm.RealmSearchAdapter;
import com.ujuzy.ujuzy.Utils.ElasticSearchApi;
import com.ujuzy.ujuzy.model.Constants;
import com.ujuzy.ujuzy.model.Datum;
import com.ujuzy.ujuzy.model.Service;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchActivity extends AppCompatActivity
{

    private String searchText = "";
    private ImageView backBtnIv;
    private EditText searchInput;
    private ArrayList<Service> mSearch;
    private RecyclerView searchListRv;
    private TextView noService;

    private Realm realm;
    private RealmChangeListener realmChangeListener;
    private RealmSearchAdapter serviceRealmAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        realm = Realm.getDefaultInstance();
        initWindows();
        initBackBtn();
        initSearch();
    }

    private void initSearch()
    {

        searchInput = (EditText) findViewById(R.id.searchInput);
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                searchText = searchInput.getText().toString();
                if (searchText.length() > 0) {
                    search();
                } else {
                    noService = (TextView) findViewById(R.id.noService);
                    noService.setVisibility(View.GONE);
                }
            }

        });


    }

    private void search()
    {
        final RealmHelper helper = new RealmHelper(realm);

        //QUERY/FILTER REALM DATABASE
        helper.searchRealmDatabase("serviceName", searchText);

        //CHECK IF DATABASE IS EMPTY
        if (helper.refreshDatabase().size() < 1 || helper.refreshDatabase().size() == 0)
        {
            noService = (TextView) findViewById(R.id.noService);
            noService.setVisibility(View.VISIBLE);
            noService.setText("Sorry, we cant find what you are looking for 😎!");
        } else {
            noService = (TextView) findViewById(R.id.noService);
            noService.setVisibility(View.GONE);
        }


        searchListRv = (RecyclerView) findViewById(R.id.service_list);
        serviceRealmAdapter = new RealmSearchAdapter(getApplicationContext(), helper.refreshDatabase());

        //RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        final LinearLayoutManager serviceLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        searchListRv.setLayoutManager(serviceLayoutManager);
        searchListRv.setAdapter(serviceRealmAdapter);

        //HANDLE DATA CHANGE FOR REFRESH
        realmChangeListener = new RealmChangeListener()
        {
            @Override
            public void onChange(Object o) {
                //REFRESH
                serviceRealmAdapter = new RealmSearchAdapter(getApplicationContext(), helper.refreshDatabase());
                searchListRv.setAdapter(serviceRealmAdapter);
            }
        };

        //ADD CHANGE LIST TO REALM
        realm.addChangeListener(realmChangeListener);

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

    private void initWindows()
    {
        Window window = SearchActivity.this.getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            window.setStatusBarColor(ContextCompat.getColor( SearchActivity.this,R.color.colorPrimaryDark));

            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (realmChangeListener != null)
        realm.removeChangeListener(realmChangeListener);
        realm.close();
    }
}
