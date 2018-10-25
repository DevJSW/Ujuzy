package com.ujuzy.ujuzy.ujuzy.BottomSheetFragments;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.ujuzy.ujuzy.ujuzy.Activities.MapsActivity;
import com.ujuzy.ujuzy.ujuzy.Activities.RequestServiceActivity;
import com.ujuzy.ujuzy.ujuzy.R;
import com.ujuzy.ujuzy.ujuzy.Realm.RealmHelper;
import com.ujuzy.ujuzy.ujuzy.Realm.RealmService;
import com.ujuzy.ujuzy.ujuzy.Realm.RealmServiceAdapter;
import com.ujuzy.ujuzy.ujuzy.Realm.RealmServiceImage;
import com.ujuzy.ujuzy.ujuzy.model.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmList;

import static io.realm.internal.SyncObjectServerFacade.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 */
public class ServiceFragments extends BottomSheetDialogFragment {

    private Realm realm;
    private RealmChangeListener realmChangeListener;
    private RealmServiceAdapter serviceReamAdapter;
    private RecyclerView servicesListRv, myServicesListRv, companyServicesListRv, searchListRv;
    private RequestQueue requestQueue;
    RealmList<RealmServiceImage> serviceImages;
    String category_title = "";
    private TextView noService;
    View v;

    public ServiceFragments() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_service, container, false);

        servicesListRv = (RecyclerView) v.findViewById(R.id.service_list);
        
        initHorizScrollMenu();
        getProfServicesFromDatabase();
        return v;
    }


    private void initHorizScrollMenu() {
        LinearLayout plumberScrollLl = (LinearLayout) v.findViewById(R.id.plumberScrollLl);
        plumberScrollLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String category_type = "";
                category_title = "Plumbing";

                initFilterServices();

            }
        });

        LinearLayout elecScrollLl = (LinearLayout) v.findViewById(R.id.elecScrollLl);
        elecScrollLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String category_type = "";
                category_title = "Electrical";

                initFilterServices();

            }
        });

        LinearLayout paintingScrollLl = (LinearLayout) v.findViewById(R.id.paintingScrollLl);
        paintingScrollLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String category_type = "";
                category_title = "Painting";

                initFilterServices();

            }
        });

        LinearLayout masonScrollLl = (LinearLayout) v.findViewById(R.id.masonScrollLl);
        masonScrollLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String category_type = "";
                category_title = "Masonry";

                initFilterServices();

            }
        });

        LinearLayout meidaScrollLl = (LinearLayout) v.findViewById(R.id.mediaScrollLl);
        meidaScrollLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String category_type = "";
                category_title = "Broadcast Media";

                initFilterServices();

            }
        });

        LinearLayout legalScrollLl = (LinearLayout) v.findViewById(R.id.legalScrollLl);
        legalScrollLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String category_type = "";
                category_title = "Legal";

                initFilterServices();

            }
        });

        LinearLayout accountingScrollLl = (LinearLayout) v.findViewById(R.id.accountingScrollLl);
        accountingScrollLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String category_type = "";
                category_title = "Accounting";

                initFilterServices();

            }
        });

        LinearLayout securityScrollLl = (LinearLayout) v.findViewById(R.id.securityScrollLl);
        securityScrollLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String category_type = "";
                category_title = "Security";

                initFilterServices();

            }
        });

        LinearLayout cosmeticsScrollLl = (LinearLayout) v.findViewById(R.id.cosmeticsScrollLl);
        cosmeticsScrollLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String category_type = "";
                category_title = "Cosmetics";

                initFilterServices();

            }
        });

        LinearLayout landscapingScrollLl = (LinearLayout) v.findViewById(R.id.landscapingScrollLl);
        landscapingScrollLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String category_type = "";
                category_title = "Landscaping";

                initFilterServices();

            }
        });


        LinearLayout weldersScrollLl = (LinearLayout) v.findViewById(R.id.weldersScrollLl);
        weldersScrollLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String category_type = "";
                category_title = "Welders";

                initFilterServices();

            }
        });

        LinearLayout capentrScrollLl = (LinearLayout) v.findViewById(R.id.capentrScrollLl);
        capentrScrollLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String category_type = "";
                category_title = "Carpentry";

                initFilterServices();

            }
        });

        LinearLayout tailorScrollLl = (LinearLayout) v.findViewById(R.id.tailorScrollLl);
        tailorScrollLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String category_type = "";
                category_title = "Tailors";

                initFilterServices();

            }
        });

        LinearLayout companyScrollLl = (LinearLayout) v.findViewById(R.id.companyScrollLl);
        companyScrollLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getCompServices();

            }
        });

        LinearLayout profScrollLl = (LinearLayout) v.findViewById(R.id.profScrollLl);
        profScrollLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getProfServicesFromDatabase();

            }
        });

        /*LinearLayout seeallScrollLl = (LinearLayout) findViewById(R.id.seeallScrollLl);
        seeallScrollLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String electricitan_type = "";

                Intent filterPlumbers = new Intent(MapsActivity.this, ServiceTabbedActivity.class);
                filterPlumbers.putExtra("category_type", electricitan_type);
                startActivity(filterPlumbers);

            }
        });*/
    }

    private void getProfServicesFromDatabase() {
        realm = Realm.getDefaultInstance();
        final RealmHelper helper = new RealmHelper(realm);

        //RETRIEVE
        helper.filterRealmDatabase("user_role", "Professional");
        //helper.retreiveFromDB();


        // all services
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

    private void initFilterServices() {
        realm = Realm.getDefaultInstance();
        final RealmHelper helper = new RealmHelper(realm);

        //QUERY/FILTER REALM DATABASE
        helper.filterRealmDatabase("category", category_title);

       /* filterCount = (TextView) findViewById(R.id.resultsCount);
        filterCount.setText("( " + helper.refreshDatabase().size() + " ) Results");*/

        //CHECK IF DATABASE IS EMPTY
        if (helper.refreshDatabase().size() < 1 || helper.refreshDatabase().size() == 0) {
            noService = (TextView) v.findViewById(R.id.noService);
            noService.setVisibility(View.VISIBLE);
            noService.setText("Oh sorry 😌😞 this is embarrassing but no " + category_title + " services posted yet!");
        } else {

            noService = (TextView) v.findViewById(R.id.noService);
            noService.setVisibility(View.GONE);

        }

        // all services
        servicesListRv = (RecyclerView) v.findViewById(R.id.service_list);
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

    private void getCompServices() {

        realm = Realm.getDefaultInstance();
        final RealmHelper helper = new RealmHelper(realm);

        //RETRIEVE
        helper.filterRealmDatabase("user_role", "Company");

        servicesListRv = (RecyclerView) v.findViewById(R.id.service_list);
        serviceReamAdapter = new RealmServiceAdapter(getApplicationContext(), helper.refreshDatabase());

        //RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        final LinearLayoutManager compServiceLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        compServiceLayoutManager.setReverseLayout(true);
        compServiceLayoutManager.setStackFromEnd(true);
        servicesListRv.setLayoutManager(compServiceLayoutManager);
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

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        realm = Realm.getDefaultInstance();
        if (realmChangeListener != null)
            realm.removeChangeListener(realmChangeListener);
    }

}
