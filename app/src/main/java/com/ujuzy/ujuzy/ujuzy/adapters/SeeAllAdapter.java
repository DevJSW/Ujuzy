package com.ujuzy.ujuzy.ujuzy.adapters;

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
import com.ujuzy.ujuzy.ujuzy.Activities.ServiceTabbedActivity;
import com.ujuzy.ujuzy.ujuzy.R;
import com.ujuzy.ujuzy.ujuzy.model.Datum;

import java.util.ArrayList;


/**
 * Created by Shephard on 6/29/2018.
 */

public class SeeAllAdapter extends RecyclerView.Adapter<SeeAllAdapter.ServicesViewHolder>
{

    private ArrayList<Datum> servicesList;
    private Context context;

    public SeeAllAdapter(Context context, ArrayList<Datum> servicesList)
    {
        this.servicesList = servicesList;
        this.context = context;
    }


    @Override
    public ServicesViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.service_row2, parent,false);

        return new ServicesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ServicesViewHolder holder, final int position)
    {

        final String photo = servicesList.get(position).getImages().get(0).toString();

        holder.serviceName.setText(servicesList.get(position).getServiceName());
        holder.serviceDetails.setText(servicesList.get(position).getServiceDetails());
        holder.costTv.setText("Ksh " + servicesList.get(position).getCost());
        /*holder.senderFirstName.setText(servicesList.get(position).getUser().getFirstname());
        holder.senderLastName.setText(servicesList.get(position).getUser().getLastname());*/

        Glide.with(context)
                .load(photo).asBitmap()
                .placeholder(R.drawable.placeholder_image)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .fitCenter()
                .into(new BitmapImageViewTarget(holder.avatar) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                        circularBitmapDrawable.setCircular(false);
                        holder.avatar.setImageDrawable(circularBitmapDrawable);
                    }
                });

        holder.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent openRead = new Intent(view.getContext(), ServiceTabbedActivity.class);
                openRead.putExtra("user_id", servicesList.get(position).getCreatedBy().getId());
                openRead.putExtra("service_id", servicesList.get(position).getId());
                openRead.putExtra("service_url", photo);
                openRead.putExtra("service_name", servicesList.get(position).getServiceName());
                openRead.putExtra("service_details", servicesList.get(position).getServiceDetails());
                //openRead.putExtra("service_location", servicesList.get(position).getCity().toString());
                openRead.putExtra("service_createdby", servicesList.get(position).getCreatedBy().getFirstname());
                openRead.putExtra("service_cost", servicesList.get(position).getCost().toString());
                if (servicesList.get(position).getDuration().getDays() != null)
                    openRead.putExtra("service_duration_days", servicesList.get(position).getDuration().getDays().toString());
                if (servicesList.get(position).getDuration().getHours() != null)
                    openRead.putExtra("service_duration_hours", servicesList.get(position).getDuration().getHours().toString());
                openRead.putExtra("service_created_at", servicesList.get(position).getCreatedAt());
                openRead.putExtra("service_category", servicesList.get(position).getCategory());
                openRead.putExtra("service_additional_info", servicesList.get(position).getCategory());
                openRead.putExtra("service_travel", servicesList.get(position).getTravel());
                openRead.putExtra("user_thumb", servicesList.get(position).getCreatedBy().getThumb());
                openRead.putExtra("no_of_personnel", servicesList.get(position).getNoOfPersonnel());

                //USER LOCATION
                //openRead.putExtra("service_latitude", servicesList.get(position).getLocation().getLat().toString());
                // openRead.putExtra("service_longitude", servicesList.get(position).getLocation().getLon().toString());

                // USER INFORMATION
                openRead.putExtra("user_id", servicesList.get(position).getUser().getId());
                openRead.putExtra("first_name", servicesList.get(position).getUser().getFirstname());
                openRead.putExtra("last_name", servicesList.get(position).getUser().getLastname());
                openRead.putExtra("user_role", servicesList.get(position).getUser().getUserRole());
                openRead.putExtra("profile_pic", servicesList.get(position).getUser().getProfilePic());
                view.getContext().startActivity(openRead);
            }
        });

    }

    @Override
    public int getItemCount()
    {
        return servicesList.size();
    }

    class ServicesViewHolder extends RecyclerView.ViewHolder
    {

        TextView serviceName, serviceDetails, senderFirstName, senderLastName, costTv;
        ImageView avatar;

        public ServicesViewHolder(View itemView)
        {
            super(itemView);

            serviceName = itemView.findViewById(R.id.tv_service_name);
            serviceDetails = itemView.findViewById(R.id.tv_service_details);
            avatar = itemView.findViewById(R.id.iv_avatar);
            costTv = itemView.findViewById(R.id.costTv);
            /*senderFirstName = itemView.findViewById(R.id.tv_sender_name);
            senderLastName = itemView.findViewById(R.id.tv_sender_last_name);*/

        }
    }
}
