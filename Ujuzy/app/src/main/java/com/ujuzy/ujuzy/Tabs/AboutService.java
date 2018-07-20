package com.ujuzy.ujuzy.Tabs;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.ujuzy.ujuzy.R;
import com.ujuzy.ujuzy.activities.ProfileActivity;
import com.ujuzy.ujuzy.map.MapsActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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


    private RelativeLayout rlOpenProfile, rlLocation, rlShare;

    private TextView serviceDetailsTv, serviceCreatedByTv, serviceCreatedAtTv, serviceCostTv, serviceTravelTv, serviceDurationTv, serviceAddInfoTv, serviceCategoryTv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_about, container, false);

        serviceCreatedByTv = (TextView) v.findViewById(R.id.tv_created_by);
        serviceDetailsTv = (TextView) v.findViewById(R.id.tv_service_details);
        serviceCreatedAtTv = (TextView) v.findViewById(R.id.tv_created_at);
        serviceCostTv = (TextView) v.findViewById(R.id.tv_cost);
        serviceTravelTv = (TextView) v.findViewById(R.id.tv_travel);
        serviceDurationTv = (TextView) v.findViewById(R.id.tv_duration);
        serviceAddInfoTv = (TextView) v.findViewById(R.id.tv_add_info);
        serviceCategoryTv = (TextView) v.findViewById(R.id.tv_category);

       /* rlLocation = (RelativeLayout) v.findViewById(R.id.rl_location);
        rlOpenProfile = (RelativeLayout) v.findViewById(R.id.rl_open_profile);
        rlShare = (RelativeLayout) v.findViewById(R.id.rl_share);*/

        initUserInfo();
        initServiceInfo();
       // initMenu();

        return v;
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

    private void initMenu()
    {
        rlShare.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent myIntent = new Intent(Intent.ACTION_SEND);
                myIntent.setType("text/plain");
                String shareBody ="Ujuzy App";
                String shareSub = service_details;
                myIntent.putExtra(Intent.EXTRA_SUBJECT,shareBody);
                myIntent.putExtra(Intent.EXTRA_TEXT,shareSub);
                view.getContext().startActivity(Intent.createChooser(myIntent,"Share this Service"));
            }
        });

        rlLocation.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                startActivity(new Intent(getActivity(), MapsActivity.class));
            }
        });

        rlOpenProfile.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent profileActivity = new Intent(view.getContext(), ProfileActivity.class);
                profileActivity.putExtra("service_id", service_id);
                profileActivity.putExtra("first_name", first_name);
                profileActivity.putExtra("last_name", last_name);
             //   profileActivity.putExtra("profile_pic", profile_pic);
                profileActivity.putExtra("user_role", user_role);
                startActivity(profileActivity);
            }
        });
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

        if (service_creator != null)
        serviceCreatedByTv.setText(first_name + " " + last_name);
        if (service_created_at != null)
        serviceCreatedAtTv.setText(service_created_at);
        serviceDetailsTv.setText(service_details);
        serviceDurationTv.setText(service_days + " " + service_hours);
        serviceTravelTv.setText(service_travel);
        serviceCategoryTv.setText(service_category);
        serviceCostTv.setText("Ksh " + service_cost);

    }

}
