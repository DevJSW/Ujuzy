package com.ujuzy.ujuzy.ujuzy.Realm;

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
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.ramotion.foldingcell.FoldingCell;
import com.ujuzy.ujuzy.ujuzy.Activities.RequestServiceActivity;
import com.ujuzy.ujuzy.ujuzy.R;

import java.util.ArrayList;

import io.realm.Realm;

public class RealmSearchAdapter extends RecyclerView.Adapter<RealmSearchAdapter.ServicesViewHolder>
{

    private ArrayList<RealmSearch> servicesList;
    private Context context;

    public RealmSearchAdapter(Context context, ArrayList<RealmSearch> servicesList)
    {
        this.servicesList = servicesList;
        this.context = context;
    }

    @Override
    public RealmSearchAdapter.ServicesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.service_row, parent,false);

        return new RealmSearchAdapter.ServicesViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final RealmSearchAdapter.ServicesViewHolder holder, final int position) {

        float mLat=(float) servicesList.get(position).getLatitude();
        String latitude=Float.toString(mLat);
        float mLong=(float) servicesList.get(position).getLongitude();
        String longitude=Float.toString(mLong);

        /*holder.serviceName.setText(servicesList.get(position).getServiceName());
        holder.serviceDetails.setText(servicesList.get(position).getServiceDetails());*/
        if (servicesList.get(position).getCost() == null || servicesList.get(position).getCost().equals("null")) {
            holder.costTv.setText("Ksh: Ask");
        } else {
            holder.costTv.setText("Ksh: " + servicesList.get(position).getCost());
        }

        holder.serviceRatingBr.setRating(Float.parseFloat(servicesList.get(position).getRating()));
        holder.titleRatingBr.setRating(Float.parseFloat(servicesList.get(position).getRating()));

        Realm realm = Realm.getDefaultInstance();

        realm = Realm.getDefaultInstance();
        RealmUserLocation realmUserLocation = realm.where(RealmUserLocation.class).findFirst();
        if (realmUserLocation != null) {
            if (realmUserLocation.getKnown_name() != null) {

                holder.content_to_address_2.setText(realmUserLocation.getKnown_name() + ", " +realmUserLocation.getCity() + ", " + realmUserLocation.getCountry());
                holder.content_to_address.setText(realmUserLocation.getCity());
            } else if (realmUserLocation.getCity() != null) {

            }
        } else {
            holder.toAddress.setText("Your location");
        }

        holder.toAddress.setText(servicesList.get(position).getCity());
        holder.time.setText(servicesList.get(position).getService_duration_hours());
        holder.date.setText(servicesList.get(position).getCreated_at());
        holder.fromAddress.setText(servicesList.get(position).getServiceName());
        holder.categoryTv.setText(servicesList.get(position).getCategory());
        holder.content_date.setText("Date:");
        holder.content_time.setText(servicesList.get(position).getService_duration_hours());
        holder.content_from_address.setText(servicesList.get(position).getCity());
        holder.content_location.setText(servicesList.get(position).getCity());
        //holder.requestsCount.setText(servicesList.get(position).getNo_of_personnel());

        holder.nameTv.setText(servicesList.get(position).getFirst_name() + " " + servicesList.get(position).getLast_name());

        if (servicesList.get(position).getCost() == null || servicesList.get(position).getCost().equals("null")) {
            holder.pledgePrice.setText("Open");
            holder.price.setText("$ Ask");
        } else {
            holder.pledgePrice.setText("$" + servicesList.get(position).getCost());
            holder.price.setText("$" + servicesList.get(position).getCost());
        }

        holder.title_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //holder.fc.toggle(false);

                if (holder.fc == null) {

                    // binding view parts to view holder

                } else {
                    // for existing cell set valid valid state(without animation)
                    if (servicesList.contains(position)) {
                        holder.fc.unfold(true);
                    } else {
                        holder.fc.fold(true);
                    }
                    holder.fc.getTag();
                }

                holder.fc.unfold(false);

                // set custom btn handler for list item from that item
               /* if (item.getRequestBtnClickListener() != null) {
                    viewHolder.contentRequestBtn.setOnClickListener(item.getRequestBtnClickListener());
                } else {
                    // (optionally) add "default" handler if no handler found in item
                    viewHolder.contentRequestBtn.setOnClickListener(defaultRequestBtnClickListener);
                }*/

            }
        });

        holder.content_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               holder.fc.fold(false);
            }
        });

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

        Glide.with(context)
                .load(servicesList.get(position).getImage())
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .fitCenter()
                .into(new BitmapImageViewTarget(holder.title_serv_image) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                        circularBitmapDrawable.setCircular(false);
                        holder.title_serv_image.setImageDrawable(circularBitmapDrawable);
                    }
                });


        holder.content_request_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                holder.fc.fold(false);

                //Toast.makeText(view.getContext(), (int) servicesList.get(position).getLatitude(), Toast.LENGTH_LONG).show();

                Intent openRead = new Intent(view.getContext(), RequestServiceActivity.class);
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
                openRead.putExtra("service_ratings", servicesList.get(position).getRating());
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
        ImageView avatar, title_serv_image, favIconIv;
        RatingBar serviceRatingBr, titleRatingBr;
        FoldingCell fc;
        LinearLayout content_layout, title_layout;

        TextView price;
        TextView contentRequestBtn;
        TextView pledgePrice;
        TextView fromAddress;
        TextView toAddress;
        TextView requestsCount;
        TextView date;
        TextView time;
        TextView nameTv;
        TextView categoryTv;
        TextView content_date;
        TextView content_time;
        TextView content_location;
        TextView content_from_address;
        TextView content_request_btn;
        TextView content_to_address_2;
        TextView content_to_address;

        public ServicesViewHolder(View itemView)
        {
            super(itemView);

            mView = itemView;
            serviceName = itemView.findViewById(R.id.tv_service_name);
            costTv = itemView.findViewById(R.id.costTv);
            serviceDetails = itemView.findViewById(R.id.tv_service_details);
            avatar = itemView.findViewById(R.id.iv_avatar);
            serviceRatingBr = itemView.findViewById(R.id.serviceRating);
            fc = (FoldingCell) itemView.findViewById(R.id.folding_cell);

            price = itemView.findViewById(R.id.title_price);
            time = itemView.findViewById(R.id.title_time_label);
            date = itemView.findViewById(R.id.title_date_label);
            fromAddress = itemView.findViewById(R.id.title_from_address);
            toAddress = itemView.findViewById(R.id.title_to_address);
            requestsCount = itemView.findViewById(R.id.title_requests_count);
            pledgePrice = itemView.findViewById(R.id.title_pledge);
            contentRequestBtn = itemView.findViewById(R.id.review_btn);

            nameTv = itemView.findViewById(R.id.content_name_view);
            categoryTv = itemView.findViewById(R.id.categoryTv);
            content_date = itemView.findViewById(R.id.content_date);
            content_time = itemView.findViewById(R.id.content_time);
            content_location = itemView.findViewById(R.id.content_location);
            content_from_address = itemView.findViewById(R.id.content_from_address_1);
            content_to_address = itemView.findViewById(R.id.provider_name);
            content_to_address_2 = itemView.findViewById(R.id.content_to_address_2);

            content_layout = itemView.findViewById(R.id.content_layout);
            title_layout = itemView.findViewById(R.id.title_layout);
            content_request_btn = itemView.findViewById(R.id.review_btn);

            title_serv_image = itemView.findViewById(R.id.title_service_bg);
            titleRatingBr = itemView.findViewById(R.id.titleRatingBr);

        }
    }

}
