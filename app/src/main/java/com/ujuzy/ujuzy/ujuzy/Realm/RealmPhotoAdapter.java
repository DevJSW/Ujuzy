package com.ujuzy.ujuzy.ujuzy.Realm;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.ujuzy.ujuzy.ujuzy.R;

import java.util.ArrayList;

public class RealmPhotoAdapter extends RecyclerView.Adapter<RealmPhotoAdapter.ServicesViewHolder>
{

    private ArrayList<RealmService> servicesList;
    private Context context;

    public RealmPhotoAdapter(Context context, ArrayList<RealmService> servicesList)
    {
        this.servicesList = servicesList;
        this.context = context;
    }

    @Override
    public RealmPhotoAdapter.ServicesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.photo_grid, parent,false);

        return new RealmPhotoAdapter.ServicesViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final RealmPhotoAdapter.ServicesViewHolder holder, final int position) {

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
                /*Intent openRead = new Intent(view.getContext(), NewServiceActivity.class);
                openRead.putExtra("service_url", servicesList.get(position).getImage());
                openRead.putExtra("service_name", servicesList.get(position).getServiceName());
                openRead.putExtra("service_category", servicesList.get(position).getCategory());
                openRead.putExtra("service_duration_days", servicesList.get(position).getService_duration_days());
                openRead.putExtra("service_duration_hours", servicesList.get(position).getService_duration_hours());
                openRead.putExtra("first_name", servicesList.get(position).getFirst_name());
                openRead.putExtra("last_name", servicesList.get(position).getLast_name());
                openRead.putExtra("service_cost", servicesList.get(position).getCost());
                openRead.putExtra("service_createdby", servicesList.get(position).getCreated_by());
                openRead.putExtra("user_role", servicesList.get(position).getUser_role());
                openRead.putExtra("service_latitude", servicesList.get(position).getLatitude());
                openRead.putExtra("service_longitude", servicesList.get(position).getLongitude());
                view.getContext().startActivity(openRead);*/
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
        ImageView avatar;

        public ServicesViewHolder(View itemView)
        {
            super(itemView);

            mView = itemView;
            avatar = itemView.findViewById(R.id.photoIv);

        }
    }
}
