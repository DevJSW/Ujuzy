package com.ujuzy.ujuzy.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.ujuzy.ujuzy.R;
import com.ujuzy.ujuzy.Realm.RealmHelper;
import com.ujuzy.ujuzy.Realm.RealmService;
import com.ujuzy.ujuzy.activities.NewServiceActivity;
import com.ujuzy.ujuzy.model.Datum;
import com.ujuzy.ujuzy.model.SkillList;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;


/**
 * Created by Shephard on 6/29/2018.
 */

public class SkillsAdapter extends RecyclerView.Adapter<SkillsAdapter.ServicesViewHolder>
{

    private List<SkillList> servicesList;
    private Context context;

    public SkillsAdapter(List<SkillList> servicesList, Context context)
    {
        this.servicesList = servicesList;
        this.context = context;
    }

    @Override
    public ServicesViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.skill_row, parent,false);

        return new ServicesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ServicesViewHolder holder, final int position)
    {

        holder.serviceName.setText(servicesList.get(position).getServiceName());
        holder.serviceOfferCost.setText("Ksh " + servicesList.get(position).getOfferCost());
        holder.serviceBilling.setText(servicesList.get(position).getBilling());
        holder.noOfPersonel.setText(servicesList.get(position).getNoOfPersonnel());
        holder.costTv.setText("Ksh " + servicesList.get(position).getCost());

    }

    @Override
    public int getItemCount()
    {
        return servicesList.size();
    }

    class ServicesViewHolder extends RecyclerView.ViewHolder
    {

        TextView serviceName, serviceOfferCost, serviceBilling, costTv, noOfPersonel;

        public ServicesViewHolder(View itemView)
        {
            super(itemView);

            serviceName = itemView.findViewById(R.id.tv_service_name);
            costTv = itemView.findViewById(R.id.tv_service_cost);
            serviceOfferCost = itemView.findViewById(R.id.tv_service_offer_cost);
            serviceBilling = itemView.findViewById(R.id.tv_service_billing);
            noOfPersonel = itemView.findViewById(R.id.tv_service_no_personnel);

        }
    }
}
