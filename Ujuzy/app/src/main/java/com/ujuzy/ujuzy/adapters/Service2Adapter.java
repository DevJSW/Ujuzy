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
import com.ujuzy.ujuzy.Realm.RealmUserService;
import com.ujuzy.ujuzy.Realm.RealmUserServicesHelper;
import com.ujuzy.ujuzy.activities.NewServiceActivity;
import com.ujuzy.ujuzy.model.Datum;

import java.util.ArrayList;

import io.realm.Realm;


/**
 * Created by Shephard on 6/29/2018.
 */

public class Service2Adapter extends RecyclerView.Adapter<Service2Adapter.ServicesViewHolder>
{

    private ArrayList<Datum> servicesList;
    private Context context;
    private Realm realm;   // it will be in the activation to settings in the inner first name

    public Service2Adapter(Context context, ArrayList<Datum> servicesList)
    {
        this.servicesList = servicesList;
        this.context = context;
    }


    @Override
    public ServicesViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.service_grid, parent,false);

        return new ServicesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ServicesViewHolder holder, final int position)
    {

        /**
         *
         * ASSIGN DATA TO REALM DATABASE SERVICE
         */

        RealmUserService realmService = new  RealmUserService();
        realmService.setId(servicesList.get(position).getId());
        realmService.setServiceName(servicesList.get(position).getServiceName());
        realmService.setServiceDetails(servicesList.get(position).getServiceDetails());
        realmService.setCost(servicesList.get(position).getCost().toString());
        realmService.setCreatedBy(servicesList.get(position).getUser().getFirstname());
        realmService.setCategory(servicesList.get(position).getCategory());
        realmService.setTravel(servicesList.get(position).getTravel());
        realmService.setImage(servicesList.get(position).getImages().get(0).toString());
        realmService.setFirst_name(servicesList.get(position).getUser().getFirstname());
        realmService.setLast_name(servicesList.get(position).getUser().getLastname());
        realmService.setUser_role(servicesList.get(position).getUser().getUserRole());
        realmService.setCreatedBy(servicesList.get(position).getCreatedBy().getFirstname());
       /* realmService.setLongitude((Double) servicesList.get(position).getLocation().getLng().toString());
        realmService.setLatitude((Double) servicesList.get(position).getLocation().getLat());*/
       /* realmService.setService_duration_days(servicesList.get(position).getDuration().getDays().toString());
        realmService.setService_duration_hours(servicesList.get(position).getDuration().getHours().toString());*/
        realmService.setNo_of_personnel(servicesList.get(position).getNoOfPersonnel());
        realmService.setUser_id(servicesList.get(position).getUser().getId());
        realmService.setUser_thumb(servicesList.get(position).getUser().getProfilePic());

        //SAVE
        realm = Realm.getDefaultInstance();
        RealmUserServicesHelper helper = new RealmUserServicesHelper(realm);
        helper.save(realmService);


        /***************************** END *******************************/


        final String photo = servicesList.get(position).getImages().get(0).getThumb();
        //final String user_photo = servicesList.get(position).getUser().;

        holder.serviceName.setText(servicesList.get(position).getServiceName());
        holder.serviceDetails.setText(servicesList.get(position).getServiceDetails());
        if (holder.senderFirstName != null)
        holder.senderFirstName.setText("Posted by:  " + servicesList.get(position).getUser().getFirstname());
        if (holder.senderRole != null)
        holder.senderRole.setText("An Ujuzy " + servicesList.get(position).getUser().getUserRole());

        holder.costTv.setText("Ksh " + servicesList.get(position).getCost());

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

      /*  if (user_photo != null || holder.userAvatar != null) {
            Glide.with(context)
                    .load(user_photo).asBitmap()
                    .placeholder(R.drawable.placeholder_image)
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .centerCrop()
                    .into(new BitmapImageViewTarget(holder.userAvatar) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable =
                                    RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                            circularBitmapDrawable.setCircular(false);
                            holder.userAvatar.setImageDrawable(circularBitmapDrawable);
                        }
                    });
        }*/

        holder.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent openRead = new Intent(view.getContext(), NewServiceActivity.class);
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
                //openRead.putExtra("profile_pic", servicesList.get(position).getUser().getProfilePic().toString());
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

        TextView serviceName, serviceDetails, senderFirstName, senderLastName, senderRole, costTv;
        ImageView avatar, userAvatar;

        public ServicesViewHolder(View itemView)
        {
            super(itemView);

            serviceName = itemView.findViewById(R.id.tv_service_name);
            costTv = itemView.findViewById(R.id.costTv);
            senderFirstName = itemView.findViewById(R.id.tv_user_name);
            senderRole = itemView.findViewById(R.id.tv_user_role);
            serviceDetails = itemView.findViewById(R.id.tv_service_details);
            avatar = itemView.findViewById(R.id.iv_avatar);
            userAvatar = itemView.findViewById(R.id.iv_user_avatar);
            /*senderFirstName = itemView.findViewById(R.id.tv_sender_name);
            senderLastName = itemView.findViewById(R.id.tv_sender_last_name);*/

        }
    }
}
