package com.ujuzy.ujuzy.adapters;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.ujuzy.ujuzy.R;
import com.ujuzy.ujuzy.activities.ProfileActivity;
import com.ujuzy.ujuzy.activities.ServiceActivity;
import com.ujuzy.ujuzy.model.Datum;
import com.ujuzy.ujuzy.model.Result;

import java.util.ArrayList;

/**
 * Created by Shephard on 6/28/2018.
 */

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.CountrysViewHolder>
{

    private ArrayList<Datum> reviewsList;
    private Context context;

    public ReviewsAdapter(Context context, ArrayList<Datum> reviewsList)
    {
        this.reviewsList = reviewsList;
        this.context = context;
    }

    @Override
    public CountrysViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.review_row, parent, false);

        return new CountrysViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CountrysViewHolder holder, int position) {
        // holder.contryNameTv.setText(countriesList.get(position).getName());

        //holder.reviewSenderName.setText(reviewsList.get(position).getCreatedBy());
        //holder.reviewSenderLastName.setText(reviewsList.get(position).getServiceName());
       // holder.reviewMessage.setText(reviewsList.get(position).get);


       /* Glide.with(context)
                .load(photo).asBitmap()
                .placeholder(R.drawable.placeholder_image)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .centerCrop()
                .into(new BitmapImageViewTarget(holder.avatar) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                        circularBitmapDrawable.setCircular(false);
                        holder.avatar.setImageDrawable(circularBitmapDrawable);
                    }
                });
*/

        holder.avatar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Intent serviceActivity = new Intent(view.getContext(), ProfileActivity.class);
                //openRead.putExtra("user_id", user_id);
                view.getContext().startActivity(serviceActivity);
            }
        });

        /** END */
    }

    @Override
    public int getItemCount()
    {
        return reviewsList.size();
    }

    class CountrysViewHolder extends RecyclerView.ViewHolder
    {
        View mView;
        TextView reviewSenderName, reviewSenderLastName, reviewMessage;
        RelativeLayout rl_call, rl_open_location, rl_share, rl_open_service;
        ImageView avatar;

        public CountrysViewHolder(View itemView)
        {
            super(itemView);

            mView = itemView;
            reviewSenderName = itemView.findViewById(R.id.tv_sender_name);
            reviewSenderLastName = itemView.findViewById(R.id.tv_sender_last_name);
            reviewMessage = itemView.findViewById(R.id.tv_review_details);
            avatar = itemView.findViewById(R.id.user_avator);
        }
    }

}
