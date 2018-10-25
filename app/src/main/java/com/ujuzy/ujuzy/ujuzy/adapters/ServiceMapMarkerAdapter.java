package com.ujuzy.ujuzy.ujuzy.adapters;

import android.app.Activity;
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
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.ujuzy.ujuzy.ujuzy.Activities.MapsActivity;
import com.ujuzy.ujuzy.ujuzy.Activities.ServiceTabbedActivity;
import com.ujuzy.ujuzy.ujuzy.R;
import com.ujuzy.ujuzy.ujuzy.Realm.RealmService;
import com.ujuzy.ujuzy.ujuzy.model.Datum;
import com.ujuzy.ujuzy.ujuzy.model.ServiceMarker;

import java.util.ArrayList;
import java.util.HashMap;

import io.realm.Realm;
import io.realm.RealmResults;


/**
 * Created by Shephard on 6/29/2018.
 */

public class ServiceMapMarkerAdapter implements GoogleMap.InfoWindowAdapter
{
    private ArrayList<ServiceMarker> mMyMarkersArray = new ArrayList<ServiceMarker>();
    private HashMap<Marker, ServiceMarker> mMarkersHashMap;
    private Context context;
    private Realm realm;
    private Marker markerShowingInfoWindow;
    ServiceMarker myMarker;

    protected View mWindow;

    public ServiceMapMarkerAdapter(Context context, HashMap<Marker, ServiceMarker> mMarkersHashMap)
    {
        this.mMarkersHashMap = mMarkersHashMap;
        this.context = context;
    }

    @Override
    public View getInfoWindow(Marker marker)
    {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker)
    {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mWindow = inflater.inflate(R.layout.infowindow_layout, null);



        markerShowingInfoWindow = marker;
        //myMarker = mMarkersHashMap.get(marker);

        realm = Realm.getDefaultInstance();

        RealmResults<RealmService> services = realm
                .where(RealmService.class)
                .findAll();

        ImageView markerIcon = (ImageView) mWindow.findViewById(R.id.iv_avatar);
        TextView markerLabel = (TextView)mWindow.findViewById(R.id.tv_service_name);
        TextView markerExp = (TextView)mWindow.findViewById(R.id.tv_service_expln);
        markerIcon.setImageResource(R.drawable.service_icon);


        markerLabel.setText(marker.getTitle());
        //markerExp.setText("Cost: Call & Ask");
        markerExp.setText(marker.getSnippet());

           /* if (marker.getSnippet() == null || marker.getSnippet().equals("null")) {
                markerExp.setText("Cost: Call & Ask");
            } else {
                markerExp.setText("Cost: Ksh " + marker.getSnippet());
            }*/


        /*if (services != null && services.size() > 0) {
            for (int i = 0; i < services.size(); i++) {
                RealmService service = services.get(i);

                markerLabel.setText(service.getServiceName());
                if (service.getCost() == null || service.getCost().equals("null")) {
                    markerExp.setText("Cost: Call & Ask");
                } else {
                    markerExp.setText("Cost: Ksh " + marker.getTitle());
                }


            }
        }*/
        return mWindow;
    }


}
