package com.ujuzy.ujuzy.Tabs;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.ujuzy.ujuzy.R;
import com.ujuzy.ujuzy.Realm.RealmAllServiceAdapter;
import com.ujuzy.ujuzy.Realm.RealmHelper;
import com.ujuzy.ujuzy.Realm.RealmServiceAdapter;
import com.ujuzy.ujuzy.Realm.RealmSuggestedServiceAdapter;
import com.ujuzy.ujuzy.activities.ProfileActivity;
import com.ujuzy.ujuzy.map.MapsActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import hakobastvatsatryan.DropdownTextView;
import io.realm.Realm;
import io.realm.RealmChangeListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class AboutService extends Fragment {

    String service_id = "";
    String service_details = "";
    String service_creator = "";
    String service_created_at = "";
    String service_cost= "";
    String service_category= "";
    String service_travel= "";
    String service_days= "";
    String service_hours= "";
    String service_add_info= "";
    String service_duration = "";
   // String service_created_at = "";

    String first_name = "";
    String last_name = "";
    //String profile_pic = "";
    String user_role = "";

    List<String> serviceDetails;
    List<String> serviceCost;
    List<String> serviceCreatedBy;
    List<String> serviceTravel;
    List<String> serviceCategory;
    List<String> serviceDutation;
    List<String> serviceAt;
    Spinner spinner_details;
    Spinner spinner_cost;
    Spinner spinner_travel;
    Spinner spinner_category;
    Spinner spinner_duration;
    Spinner spinner_createdat;
    Spinner spinner_createdby;

    private Realm realm;
    private RealmChangeListener realmChangeListener;
    private RealmSuggestedServiceAdapter serviceRealmAdapter;

    private TextView noService;

    private RecyclerView servicesListRv;
    private DropdownTextView serviceExpandableTv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_about, container, false);


        spinner_details = (Spinner) v.findViewById(R.id.spinner_details);
        spinner_cost = (Spinner) v.findViewById(R.id.spinner_cost);
        spinner_travel = (Spinner) v.findViewById(R.id.spinner_travel);
        spinner_category = (Spinner) v.findViewById(R.id.spinner_category);
        spinner_duration = (Spinner) v.findViewById(R.id.spinner_dutation);
        spinner_createdby = (Spinner) v.findViewById(R.id.spinner_createdby);
        spinner_createdat = (Spinner) v.findViewById(R.id.spinner_createdat);
        spinner_cost = (Spinner) v.findViewById(R.id.spinner_cost);

        noService = (TextView) v.findViewById(R.id.noService);
        servicesListRv = (RecyclerView) v.findViewById(R.id.service_list);


       /* rlLocation = (RelativeLayout) v.findViewById(R.id.rl_location);
        rlOpenProfile = (RelativeLayout) v.findViewById(R.id.rl_open_profile);
        rlShare = (RelativeLayout) v.findViewById(R.id.rl_share);*/

        initUserInfo();
        initServiceInfo();
       // initMenu();
        initTabs();
        initSimilarServClose();

        return v;
    }

    private void initSimilarServClose()
    {
        realm = Realm.getDefaultInstance();
        final RealmHelper helper = new RealmHelper(realm);

        //QUERY/FILTER REALM DATABASE
        helper.filterRealmDatabase("category", service_category);

        //CHECK IF DATABASE IS EMPTY
        if (helper.refreshDatabase().size() < 1 || helper.refreshDatabase().size() == 0)
        {

            noService.setVisibility(View.VISIBLE);
            noService.setText("Oh no 😌😞 this is embarrassing but no " + service_category + " services near you!");

        } else {

            noService.setVisibility(View.GONE);
        }


        serviceRealmAdapter = new RealmSuggestedServiceAdapter(getActivity(), helper.refreshDatabase());

        //RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        final LinearLayoutManager serviceLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        servicesListRv.setLayoutManager(serviceLayoutManager);
        servicesListRv.setAdapter(serviceRealmAdapter);

        //HANDLE DATA CHANGE FOR REFRESH
        realmChangeListener = new RealmChangeListener()
        {
            @Override
            public void onChange(Object o) {
                //REFRESH
                serviceRealmAdapter = new RealmSuggestedServiceAdapter(getActivity(), helper.refreshDatabase());
                servicesListRv.setAdapter(serviceRealmAdapter);
            }
        };

        //ADD CHANGE LIST TO REALM
        realm.addChangeListener(realmChangeListener);
    }

    private void initTabs()
    {
        initServiceDetailsTabs();
        if (service_creator != null)
        initCreatedby();
        if (service_creator == null)
        spinner_createdby.setVisibility(View.GONE);
        if (service_category != null)
        initCategory();
        if (service_category == null)
        spinner_category.setVisibility(View.GONE);
        if (service_travel != null)
        initTravel();
        if (service_travel == null)
        spinner_travel.setVisibility(View.GONE);
        if (service_created_at != null)
        initCreatedAt();
        if (service_created_at == null)
        spinner_createdat.setVisibility(View.GONE);
        if (service_cost != null)
        initOfferCost();
        if (service_cost == null)
            spinner_cost.setVisibility(View.GONE);
        if (service_duration != null)
        initServiceDuration();
        if (service_duration == null)
            spinner_duration.setVisibility(View.GONE);
    }

    private void initServiceDuration() {
        String[] service_duration_string = new String[]{
                "Duration",
                service_cost
        };

        serviceDutation = new ArrayList<>(Arrays.asList(service_duration_string));

        // Initializing an ArrayAdapter
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                getActivity(), R.layout.spinner_item, serviceDutation) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };


        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinner_duration.setAdapter(spinnerArrayAdapter);

    }

    private void initCreatedAt() {
        String[] service_at_string = new String[]{
                "Created At",
                service_cost
        };

        serviceAt = new ArrayList<>(Arrays.asList(service_at_string));

        // Initializing an ArrayAdapter
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                getActivity(), R.layout.spinner_item, serviceAt) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };


        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinner_createdat.setAdapter(spinnerArrayAdapter);
    }

    private void initOfferCost() {
        String[] service_cost_string = new String[]{
                "Offer Cost",
                "Ksh " + service_cost
        };

        serviceCost = new ArrayList<>(Arrays.asList(service_cost_string));

        // Initializing an ArrayAdapter
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                getActivity(), R.layout.spinner_item, serviceCost) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };


        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinner_cost.setAdapter(spinnerArrayAdapter);
    }

    private void initTravel() {

        String[] service_travel_string = new String[]{
                "Travel",
                service_travel
        };

        serviceTravel = new ArrayList<>(Arrays.asList(service_travel_string));

        // Initializing an ArrayAdapter
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                getActivity(), R.layout.spinner_item, serviceTravel) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };


        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinner_travel.setAdapter(spinnerArrayAdapter);
    }

    private void initCategory() {

        String[] service_category_string = new String[]{
                "Category",
                service_category
        };

        serviceCategory = new ArrayList<>(Arrays.asList(service_category_string));

        // Initializing an ArrayAdapter
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                getActivity(), R.layout.spinner_item, serviceCategory) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };


        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinner_category.setAdapter(spinnerArrayAdapter);
    }

    private void initCreatedby() {

        String[] service_createdby = new String[]{
                "Created By",
                service_creator
        };

        serviceCreatedBy = new ArrayList<>(Arrays.asList(service_createdby));

        // Initializing an ArrayAdapter
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                getActivity(), R.layout.spinner_item, serviceCreatedBy) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };


        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinner_createdby.setAdapter(spinnerArrayAdapter);

    }

    private void initServiceDetailsTabs()
    {
        String[] service_details_string = new String[]{
                "About service",
                service_details
        };

        serviceDetails = new ArrayList<>(Arrays.asList(service_details_string));

        // Initializing an ArrayAdapter
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                getActivity(), R.layout.spinner_item, serviceDetails) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };


        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinner_details.setAdapter(spinnerArrayAdapter);

    }

    private void initUserInfo()
    {
        Bundle bundle2 = this.getArguments();
        if (bundle2 != null) {
            service_id = bundle2.getString("serviceId", null);
            first_name = bundle2.getString("first_name", null);
            last_name = bundle2.getString("last_name", null);
          //  profile_pic = bundle2.getString("profile_pic", null);
            user_role = bundle2.getString("user_role", null);
        }
    }

    private void initServiceInfo()
    {
        Bundle bundle2 = this.getArguments();
        if (bundle2 != null) {
            service_id = bundle2.getString("serviceId", null);
            service_details = bundle2.getString("serviceDetails", null);
            if (service_creator != null)
            service_creator = bundle2.getString("serviceCreatedBy", null);
            if (service_created_at != null)
            service_created_at = bundle2.getString("serviceCreatedAt", null);
            service_cost = bundle2.getString("serviceCost", null);

            service_category = bundle2.getString("serviceCategory", null);
            service_travel = bundle2.getString("serviceTravel", null);
            service_days = bundle2.getString("serviceDays", null);
            service_hours = bundle2.getString("serviceHours", null);
            service_add_info = bundle2.getString("serviceAddInfo", null);
        }


    }

}
