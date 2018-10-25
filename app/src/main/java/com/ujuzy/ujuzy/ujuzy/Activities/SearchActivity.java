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
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ujuzy.ujuzy.ujuzy.R;
import com.ujuzy.ujuzy.ujuzy.Realm.RealmHelper;
import com.ujuzy.ujuzy.ujuzy.Realm.RealmSearch;
import com.ujuzy.ujuzy.ujuzy.Realm.RealmSearchAdapter;
import com.ujuzy.ujuzy.ujuzy.Realm.RealmSearchHelper;
import com.ujuzy.ujuzy.ujuzy.Realm.RealmService;
import com.ujuzy.ujuzy.ujuzy.Realm.RealmServiceAdapter;
import com.ujuzy.ujuzy.ujuzy.Realm.RealmServiceImage;
import com.ujuzy.ujuzy.ujuzy.Services.Send_http_post_request;
import com.ujuzy.ujuzy.ujuzy.model.Constants;
import com.ujuzy.ujuzy.ujuzy.model.Service;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmList;

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
    private static final String USER_AGENT = "Mozilla/5.0";
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

    private static void sendGET() throws IOException {

        URL url = new URL(GET_URL + searchText);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        int responseCode = con.getResponseCode();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
       /* if (responseCode == HttpURLConnection.HTTP_OK) { // success
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // print result
            System.out.println(response.toString());
        } else {
            System.out.println("GET request not worked");
        }
*/
    }

   /* public void initSearchUrl() throws Exception
    {
        URL url = new URL("https://ujuzy.com/services/search?q="+searchText);

        StringBuilder postData = new StringBuilder();

        byte[] postDataBytes = postData.toString().getBytes("UTF-8");
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
        conn.setDoOutput(true);
//        conn.getOutputStream().write(postDataBytes);
        Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
        StringBuilder sb = new StringBuilder();
        for (int c; (c = in.read()) >= 0;)
            sb.append((char)c);
        String response = sb.toString();
        System.out.println(response);
        JSONObject myResponse = new JSONObject(response.toString());
        System.out.println("result after Reading JSON Response");
        System.out.println("origin- "+myResponse.getString("origin"));
        System.out.println("url- "+myResponse.getString("url"));
        JSONObject form_data = myResponse.getJSONObject("form");
        System.out.println("CODE- "+form_data.getString("CODE"));
        System.out.println("email- "+form_data.getString("email"));
        System.out.println("message- "+form_data.getString("message"));
        System.out.println("name"+form_data.getString("name"));
    }
*/
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
                if (searchText.length() > 0) {
                    //search();
                    try {
                        //sendGET();
                        sendingGetRequest();
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

    private String get(String getUrl) throws IOException {
        URL url = new URL(getUrl);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        return this.read(con.getInputStream());
    }

    private String read(InputStream is) throws IOException {
        BufferedReader in = null;
        String inputLine;
        StringBuilder body;
        try {
            in = new BufferedReader(new InputStreamReader(is));

            body = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                body.append(inputLine);
            }
            in.close();

            return body.toString();
        } catch(IOException ioe) {
            throw ioe;
        } finally {
            //this.closeQuietly(in);
        }
    }


    private void search()
    {
        final RealmSearchHelper helper = new RealmSearchHelper(realm);

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

   /* private void sendGet() throws Exception {

        try {
            // Create URL
            String url = "https://api.ujuzy.com/services/search?q=" + searchText;
            URL obj = new URL(url);

            // Next, we create a new JsonArrayRequest. This will use Volley to make a HTTP request
            // that expects a JSON Array Response.
            // To fully understand this, I'd recommend readng the office docs: https://developer.android.com/training/volley/index.html
            JsonArrayRequest arrReq = new JsonArrayRequest(Request.Method.GET, obj,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            // Check the length of our response
                            if (response.length() > 0) {
                                for (int i = 0; i < response.length(); i++) {
                                    try {
                                        JSONObject jsonObj = response.getJSONObject(i);
                                        String repoName = jsonObj.get("name").toString();
                                        String lastUpdated = jsonObj.get("updated_at").toString();
                                    } catch (JSONException e) {
                                        // If there is an error then output this to the logs.
                                        Log.e("Volley", "Invalid JSON Object.");


                                }
                            } else {
                            }

                        }
                    },

                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // If there a HTTP error then add a note to our repo list.
                            Log.e("Volley", error.toString());
                        }
                    }
            );
            // Add the request we just defined to our request queue.
            // The request queue will automatically handle the request as soon as it can.
            requestQueue.add(arrReq);
            // Simulate network access.
            Thread.sleep(2000);
        } catch (InterruptedException e) {

        }

    }
*/
    // HTTP GET request
    private void sendingGetRequest() throws Exception {

        String urlString = GET_URL + searchText;

        URL url = new URL(urlString);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        // By default it is GET request
        con.setRequestMethod("GET");

        //add request header
        con.setRequestProperty("User-Agent", USER_AGENT);

        int responseCode = con.getResponseCode();
        System.out.println("Sending get request : "+ url);
        System.out.println("Response code : "+ responseCode);

        // Reading response from input Stream
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String output;
        StringBuffer response = new StringBuffer();

        while ((output = in.readLine()) != null) {
            response.append(output);
        }
        in.close();

        //printing result from response
        System.out.println(response.toString());

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (realmChangeListener != null)
            realm.removeChangeListener(realmChangeListener);
        realm.close();
    }
}
