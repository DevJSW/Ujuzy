package com.ujuzy.ujuzy.Realm;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.ujuzy.ujuzy.R;
import com.ujuzy.ujuzy.activities.ChatroomActivity;
import com.ujuzy.ujuzy.activities.NewServiceActivity;

import java.util.ArrayList;

import io.realm.Realm;

public class RealmRequestedServiceAdapter extends RecyclerView.Adapter<RealmRequestedServiceAdapter.ServicesViewHolder>
{

    private ArrayList<RealmRequestedUserService> servicesList;
    private Context context;

    public RealmRequestedServiceAdapter(Context context, ArrayList<RealmRequestedUserService> servicesList)
    {
        this.servicesList = servicesList;
        this.context = context;
    }

    @Override
    public RealmRequestedServiceAdapter.ServicesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.request_row, parent,false);

        return new RealmRequestedServiceAdapter.ServicesViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final RealmRequestedServiceAdapter.ServicesViewHolder holder, final int position) {

        holder.serviceName.setText(servicesList.get(position).getServiceName());
        holder.serviceCreatedby.setText(servicesList.get(position).getName());
        holder.serviceCreatedat.setText(servicesList.get(position).getCreated_at());

        Glide.with(context)
                .load(servicesList.get(position).getImage())
                .asBitmap()
                .placeholder(R.drawable.placeholder_image)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .fitCenter()
                .into(new BitmapImageViewTarget(holder.userAvatar) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                        circularBitmapDrawable.setCircular(false);
                        holder.userAvatar.setImageDrawable(circularBitmapDrawable);
                    }
                });

        holder.connectBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent openRead = new Intent(view.getContext(), ChatroomActivity.class);
                openRead.putExtra("service_url", servicesList.get(position).getImage());
                openRead.putExtra("service_id", servicesList.get(position).getId());
                openRead.putExtra("service_name", servicesList.get(position).getServiceName());
                openRead.putExtra("service_detail", servicesList.get(position).getServiceDetails());
                openRead.putExtra("service_category", servicesList.get(position).getCategory());
                openRead.putExtra("service_duration_days", servicesList.get(position).getService_duration_days());
                openRead.putExtra("service_duration_hours", servicesList.get(position).getService_duration_hours());
                openRead.putExtra("first_name", servicesList.get(position).getFirst_name());
                openRead.putExtra("last_name", servicesList.get(position).getLast_name());
                openRead.putExtra("user_id", servicesList.get(position).getUser_id());
                openRead.putExtra("service_cost", servicesList.get(position).getCost());
                openRead.putExtra("service_createdby", servicesList.get(position).getCreated_by());
                openRead.putExtra("user_role", servicesList.get(position).getUser_role());
                openRead.putExtra("service_latitude", servicesList.get(position).getLatitude());
                openRead.putExtra("service_longitude", servicesList.get(position).getLongitude());
                openRead.putExtra("service_location", servicesList.get(position).getCity());
                view.getContext().startActivity(openRead);
            }
        });

    }

    @Override
    public int getItemCount() {
        return servicesList.size();
    }

    class ServicesViewHolder extends RecyclerView.ViewHolder
    {
        View mView;
        TextView serviceName, serviceCreatedby, serviceCreatedat;
        ImageView avatar, userAvatar;
        Button connectBtn;

        public ServicesViewHolder(View itemView)
        {
            super(itemView);

            mView = itemView;
            serviceName = itemView.findViewById(R.id.tv_service_name);
            serviceCreatedby = itemView.findViewById(R.id.tv_created_by);
            serviceCreatedat = itemView.findViewById(R.id.tv_created_at);
            userAvatar = itemView.findViewById(R.id.iv_avatar);
            connectBtn = itemView.findViewById(R.id.connectBtn);

        }
    }
}
