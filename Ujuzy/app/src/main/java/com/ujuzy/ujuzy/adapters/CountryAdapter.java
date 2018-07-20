package com.ujuzy.ujuzy.adapters;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ujuzy.ujuzy.R;
import com.ujuzy.ujuzy.activities.ProfileActivity;
import com.ujuzy.ujuzy.activities.ServiceActivity;
import com.ujuzy.ujuzy.map.MapsActivity;
import com.ujuzy.ujuzy.model.Result;

import java.util.ArrayList;

/**
 * Created by Shephard on 6/28/2018.
 */

public class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.CountrysViewHolder> {

    private ArrayList<Result> countriesList;

    public CountryAdapter(ArrayList<Result> countriesList) {
        this.countriesList = countriesList;
    }

    @Override
    public CountrysViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.service_row, parent, false);

        return new CountrysViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CountrysViewHolder holder, int position) {
        // holder.contryNameTv.setText(countriesList.get(position).getName());


        /** Service row buttons functions *//*
        holder.rl_call.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_CALL);

                intent.setData(Uri.parse("tel:0796190644"));
                if (ActivityCompat.checkSelfPermission(view.getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
                {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;  // it will be in a more local way of doing
                }
                view.getContext().startActivity(intent);
            }
        });

        holder.rl_open_location.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent serviceActivity = new Intent(view.getContext(), MapsActivity.class);
                //openRead.putExtra("user_id", user_id);
                view.getContext().startActivity(serviceActivity);
            }
        });
        holder.rl_share.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent myIntent = new Intent(Intent.ACTION_SEND);
                myIntent.setType("text/plain");
                String shareBody ="Ujuzy App";
                String shareSub = "This is where the service message will appear";
                myIntent.putExtra(Intent.EXTRA_SUBJECT,shareBody);
                myIntent.putExtra(Intent.EXTRA_TEXT,shareSub);
                view.getContext().startActivity(Intent.createChooser(myIntent,"Share this Service"));
            }
        });

        // Open service activity
        holder.rl_open_service.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent serviceActivity = new Intent(view.getContext(), ServiceActivity.class);
                //openRead.putExtra("user_id", user_id);
                view.getContext().startActivity(serviceActivity);
            }
        });

        holder.avatar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Intent serviceActivity = new Intent(view.getContext(), ProfileActivity.class);
                //openRead.putExtra("user_id", user_id);
                view.getContext().startActivity(serviceActivity);
            }
        });

        holder.mView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Intent serviceActivity = new Intent(view.getContext(), ServiceActivity.class);
                //openRead.putExtra("user_id", user_id);
                view.getContext().startActivity(serviceActivity);
            }
        });
*/
        /** END */
    }

    @Override
    public int getItemCount()
    {
        return countriesList.size();
    }

    class CountrysViewHolder extends RecyclerView.ViewHolder
    {
        View mView;
        TextView contryNameTv;
        RelativeLayout rl_call, rl_open_location, rl_share, rl_open_service;
        ImageView avatar;

        public CountrysViewHolder(View itemView)
        {
            super(itemView);

            mView = itemView;
            //contryNameTv = itemView.findViewById(R.id.tv_service_details);
            /*rl_call = itemView.findViewById(R.id.call);
            rl_open_location = itemView.findViewById(R.id.openLocation);
            rl_share = itemView.findViewById(R.id.shareService);
            rl_open_service = itemView.findViewById(R.id.openService);
            avatar = itemView.findViewById(R.id.user_avator);*/
        }
    }

}
