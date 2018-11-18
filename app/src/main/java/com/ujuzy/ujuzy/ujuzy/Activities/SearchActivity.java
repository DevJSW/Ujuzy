package com.ujuzy.ujuzy.ujuzy.Activities;

import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ujuzy.ujuzy.ujuzy.R;
import com.ujuzy.ujuzy.ujuzy.Realm.RealmSearch;
import com.ujuzy.ujuzy.ujuzy.Realm.RealmSearchAdapter;
import com.ujuzy.ujuzy.ujuzy.Realm.RealmSearchHelper;
import com.ujuzy.ujuzy.ujuzy.Realm.RealmServiceImage;
import com.ujuzy.ujuzy.ujuzy.model.Constants;
import com.ujuzy.ujuzy.ujuzy.model.Service;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.net.HttpURLConnection;
import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmList;
import io.realm.RealmResults;

public class SearchActivity extends AppCompatActivity
{

    private static String searchText = "";
    private ImageView backBtnIv;
    private EditText searchInput;
    private ArrayList<Service> mSearch;
    private RecyclerView searchListRv;
    private TextView noService;
    private RequestQueue requestQueue;

    private Realm realm;
    private RealmChangeListener realmChangeListener;
    private RealmSearchAdapter serviceRealmAdapter;
    RealmList<RealmServiceImage> serviceImages;
    private static final String GET_URL = "http://ujuzy.com/services/search?q=";
    private static HttpURLConnection con;

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

    public void initSearchUrl()
    {

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                Constants.HTTP.SEARCH_ENDPOINT + searchText, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject serviceObj = jsonArray.getJSONObject(i);
                       // JSONObject serviceUserObj = jsonArray.getJSONObject(i).getJSONObject("user");
                       // JSONObject serviceLocationObj = jsonArray.getJSONObject(i).getJSONObject("location");
                        JSONObject serviceDurationObj = jsonArray.getJSONObject(i).getJSONObject("duration");

                       // JSONArray skillList = serviceObj.getJSONArray("skill_list");

                        // ASSIGN DATA TO REALM DATABASE SERVICE

                        RealmSearch realmService = new RealmSearch();
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

                       /* realmService.setUser_role(serviceUserObj.getString("user_role"));
                        realmService.setUser_thumb(serviceUserObj.getString("profile_pic"));
                        realmService.setFirst_name(serviceUserObj.getString("firstname"));
                        realmService.setLast_name(serviceUserObj.getString("lastname"));
                        realmService.setUser_id(serviceUserObj.getString("id"));*/

                        /*realmService.setCity(serviceLocationObj.getString("city"));
                        realmService.setLatitude(Double.parseDouble(serviceLocationObj.getString("lat")));
                        realmService.setLongitude(Double.parseDouble(serviceLocationObj.getString("lng")));*/

                        realmService.setService_duration_days(serviceDurationObj.getString("days"));
                        realmService.setService_duration_hours(serviceDurationObj.getString("hours"));


                        //SAVE
                        realm = Realm.getDefaultInstance();
                        RealmSearchHelper helper = new RealmSearchHelper(realm);
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

        requestQueue = Volley.newRequestQueue(SearchActivity.this);
        requestQueue.add(stringRequest);

    }

    private void initSearch()
    {

        searchInput = (EditText) findViewById(R.id.searchInput);
        searchInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES); // <----------- making the first letter in the sentence uppercase
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
                if (searchText.length() > 1) {
                    //search();
                    try {
                        deleteSearchFromRealm();
                        initSearchUrl();
                        search();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    noService = (TextView) findViewById(R.id.noService);
                    noService.setVisibility(View.GONE);
                }
            }

        });


    }

    private void deleteSearchFromRealm()
    {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<RealmSearch> result = realm.where(RealmSearch.class).findAll();
                result.deleteAllFromRealm();
            }
        });
    }

    private void search()
    {
        final RealmSearchHelper helper = new RealmSearchHelper(realm);

        helper.retreiveFromDB();

        //CHECK IF DATABASE IS EMPTY
        if (helper.refreshDatabase().size() < 1 || helper.refreshDatabase().size() == 0)
        {
            noService = (TextView) findViewById(R.id.noService);
        } else {
            noService = (TextView) findViewById(R.id.noService);
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
        deleteSearchFromRealm();
        realm.close();
    }
}
