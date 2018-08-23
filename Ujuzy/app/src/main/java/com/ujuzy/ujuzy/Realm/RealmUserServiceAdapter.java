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
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.ujuzy.ujuzy.R;
import com.ujuzy.ujuzy.activities.NewServiceActivity;

import java.util.ArrayList;

import io.realm.Realm;

public class RealmUserServiceAdapter extends RecyclerView.Adapter<RealmUserServiceAdapter.ServicesViewHolder>
{

    private ArrayList<RealmUserService> servicesList;
    private Context context;
    private Realm realm;

    public RealmUserServiceAdapter(Context context, ArrayList<RealmUserService> servicesList)
    {
        this.servicesList = servicesList;
        this.context = context;
    }

    @Override
    public RealmUserServiceAdapter.ServicesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.service_row2, parent,false);

        return new RealmUserServiceAdapter.ServicesViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final RealmUserServiceAdapter.ServicesViewHolder holder, final int position) {


        /**
         *
         * ASSIGN DATA TO REALM DATABASE SERVICE
         */

        RealmUserService realmService = new  RealmUserService();
        realmService.setId(servicesList.get(position).getId());
        realmService.setServiceName(servicesList.get(position).getServiceName());
        realmService.setServiceDetails(servicesList.get(position).getServiceDetails());
        realmService.setCost(servicesList.get(position).getCost().toString());
        realmService.setCreatedBy(servicesList.get(position).getFirst_name());
        realmService.setCategory(servicesList.get(position).getCategory());
        realmService.setTravel(servicesList.get(position).getTravel());
        //realmService.setImage(servicesList.get(position).getImages().get(0).toString());
        realmService.setFirst_name(servicesList.get(position).getFirst_name());
        realmService.setLast_name(servicesList.get(position).getLast_name());
        realmService.setUser_role(servicesList.get(position).getUser_role());
       /* realmService.setLongitude((Double) servicesList.get(position).getLocation().getLng().toString());
        realmService.setLatitude((Double) servicesList.get(position).getLocation().getLat());*/
       /* realmService.setService_duration_days(servicesList.get(position).getDuration().getDays().toString());
        realmService.setService_duration_hours(servicesList.get(position).getDuration().getHours().toString());*/
       /* realmService.setNo_of_personnel(servicesList.get(position).getNoOfPersonnel());
        realmService.setUser_id(servicesList.get(position).getUser().getId());*/
        realmService.setUser_thumb(servicesList.get(position).getUser_thumb());

        //SAVE
        realm = Realm.getDefaultInstance();
        RealmUserServicesHelper helper = new RealmUserServicesHelper(realm);
        helper.save(realmService);


        /***************************** END *******************************/


        holder.serviceName.setText(servicesList.get(position).getServiceName());
        holder.serviceDetails.setText(servicesList.get(position).getServiceDetails());
        holder.costTv.setText("Ksh " + servicesList.get(position).getCost());

        Glide.with(context)
                .load(servicesList.get(position).getImage())
                .asBitmap()
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
                Intent openRead = new Intent(view.getContext(), NewServiceActivity.class);
                openRead.putExtra("service_url", servicesList.get(position).getImage());
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
        TextView serviceName, serviceDetails, senderFirstName, senderLastName, senderRole, costTv;
        ImageView avatar, userAvatar;

        public ServicesViewHolder(View itemView)
        {
            super(itemView);

            mView = itemView;
            serviceName = itemView.findViewById(R.id.tv_service_name);
            costTv = itemView.findViewById(R.id.costTv);
            senderFirstName = itemView.findViewById(R.id.tv_user_name);
            senderRole = itemView.findViewById(R.id.tv_user_role);
            serviceDetails = itemView.findViewById(R.id.tv_service_details);
            avatar = itemView.findViewById(R.id.iv_avatar);
            userAvatar = itemView.findViewById(R.id.iv_user_avatar);

        }
    }
}
