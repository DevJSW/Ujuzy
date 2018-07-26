package com.ujuzy.ujuzy.activities;

import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.ujuzy.ujuzy.R;
import com.ujuzy.ujuzy.map.MapsActivity;
import com.ujuzy.ujuzy.model.Search;

import java.util.ArrayList;
import java.util.HashMap;

public class SearchActivity extends AppCompatActivity {

    // List view
    private ListView lv;

    // Listview Adapter
    ArrayAdapter<String> adapter;

    // Search EditText
    EditText inputSearch;
    private ImageView backBtnIv;

    // ArrayList for Listview
    ArrayList<HashMap<String, String>> searchList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // Listview Data
        String products[] = {"Plumbers", "Electricians", "Carpenter", "Masons", "Tailors",
                "Welders", "Professions",};



        lv = (ListView) findViewById(R.id.list_view);
        inputSearch = (EditText) findViewById(R.id.searchInputEt);

        // Adding items to listview
        adapter = new ArrayAdapter<String>(this, R.layout.search_item, R.id.product_name, products);
        lv.setAdapter(adapter);

        initWindows();
        initBackBtn();
    }

    private void initBackBtn()
    {
        backBtnIv = (ImageView) findViewById(R.id.backBtn);
        backBtnIv.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private ArrayList<Search> createSearchSuggestions()
    {
        ArrayList<Search> searchItems = new ArrayList<>();
        searchItems.add(new Search("Plumbers"));
        searchItems.add(new Search("Electricians"));
        searchItems.add(new Search("Carpenter"));
        searchItems.add(new Search("Masons"));
        searchItems.add(new Search("Tailors"));
        searchItems.add(new Search("Welders"));
        searchItems.add(new Search("Professions"));
        searchItems.add(new Search("Companies"));
        return searchItems;
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
}
