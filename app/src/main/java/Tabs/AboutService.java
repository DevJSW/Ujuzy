package Tabs;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.ujuzy.ujuzy.ujuzy.Activities.RequestServiceActivity;
import com.ujuzy.ujuzy.ujuzy.R;
import com.ujuzy.ujuzy.ujuzy.Realm.RealmHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmChangeListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class AboutService extends Fragment {

    String service_id = "";
    String service_details = "";
    String service_name = "";
    String service_creator = "";
    String service_created_at = "";
    String service_cost= "";
    String service_category= "";
    String service_travel= "";
    String service_days= "";
    String service_hours= "";
    String service_add_info= "";
    String service_duration = "";
    String no_of_personnel = "";
   // String service_created_at = "";

    String first_name = "";
    String last_name = "";
    String profile_pic = "";
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
    //private RealmSuggestedServiceAdapter serviceRealmAdapter;

    private TextView noService, duration, categery, cost, name;
    private Button bookServiceBt;

    private RecyclerView servicesListRv;
    private ExpandableTextView serviceExpTv, serviceCostExpandableTv, serviceDurationExpandableTv, serviceCategoryExpandableTv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_about, container, false);

        noService = (TextView) v.findViewById(R.id.noService);
        duration = (TextView) v.findViewById(R.id.tv_service_duration);
        categery = (TextView) v.findViewById(R.id.tv_service_category);
        cost = (TextView) v.findViewById(R.id.tv_service_cost);
        name = (TextView) v.findViewById(R.id.tv_user_name);
        servicesListRv = (RecyclerView) v.findViewById(R.id.service_list);
        serviceExpTv = (ExpandableTextView) v.findViewById(R.id.tv_service_ex);
        bookServiceBt = (Button) v.findViewById(R.id.bookService);


       /* rlLocation = (RelativeLayout) v.findViewById(R.id.rl_location);
        rlOpenProfile = (RelativeLayout) v.findViewById(R.id.rl_open_profile);
        rlShare = (RelativeLayout) v.findViewById(R.id.rl_share);*/

        initUserInfo();
        initServiceInfo();
       // initMenu();
        initSimilarServClose();
        initExpandableTv();
        initBookService();

        return v;
    }

    private void initBookService() {
        bookServiceBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent requestActivity = new Intent(getActivity(), RequestServiceActivity.class);
                requestActivity.putExtra("service_id", service_id);
                requestActivity.putExtra("first_name", first_name);
                requestActivity.putExtra("last_name", last_name);
                requestActivity.putExtra("service_cost", service_cost);
                requestActivity.putExtra("service_name", service_name);
                requestActivity.putExtra("no_of_personnel", no_of_personnel);
                requestActivity.putExtra("profile_pic", profile_pic);
                startActivity(requestActivity);
            }
        });
    }

    private void initExpandableTv() {
        serviceExpTv.setText(service_details);
        cost.setText(service_cost);
        categery.setText(service_category);
        duration.setText(service_duration);
        name.setText(first_name + " " + last_name);
        /*serviceCostExpandableTv.setText(service_cost);
        serviceDurationExpandableTv.setText(service_duration);
        serviceCategoryExpandableTv.setText(service_category);*/
    }

    private void initSimilarServClose()
    {

        noService.setVisibility(View.VISIBLE);
        noService.setText("No similar " + service_category + " services near you!");

        realm = Realm.getDefaultInstance();
        final RealmHelper helper = new RealmHelper(realm);

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

    private void initCreatedAt()
    {
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
            no_of_personnel = bundle2.getString("no_of_personnel", null);
            profile_pic = bundle2.getString("profile_pic", null);
            service_name = bundle2.getString("serviceName", null);
            service_travel = bundle2.getString("serviceTravel", null);
            service_days = bundle2.getString("serviceDays", null);
            service_hours = bundle2.getString("serviceHours", null);
            service_add_info = bundle2.getString("serviceAddInfo", null);
        }


    }

}
