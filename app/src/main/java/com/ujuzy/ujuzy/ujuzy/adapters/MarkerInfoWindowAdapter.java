package com.ujuzy.ujuzy.ujuzy.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.ujuzy.ujuzy.ujuzy.R;
import com.ujuzy.ujuzy.ujuzy.model.ServiceMarker;

import java.util.ArrayList;
import java.util.HashMap;

public class MarkerInfoWindowAdapter implements GoogleMap.InfoWindowAdapter
{
    private ArrayList<ServiceMarker> mMyMarkersArray = new ArrayList<ServiceMarker>();
    private HashMap<Marker, ServiceMarker> mMarkersHashMap;
    private Context ctx;

    public MarkerInfoWindowAdapter(Context ctx)
    {
        this.ctx=ctx;
    }

    @Override
    public View getInfoWindow(Marker marker)
    {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker)
    {
        //View v  = getLayoutInflater().inflate(R.layout.infowindow_layout, null);
        View v = LayoutInflater.from(ctx).inflate(R.layout.infowindow_layout, null);

        ServiceMarker myMarker = mMarkersHashMap.get(marker);
/*
        ImageView markerIcon = (ImageView) v.findViewById(R.id.marker_icon);

        TextView markerLabel = (TextView)v.findViewById(R.id.marker_label);

        markerIcon.setImageResource(manageMarkerIcon(myMarker.getmIcon()));

        markerLabel.setText(myMarker.getmLabel());*/

        return v;
    }

    private int manageMarkerIcon(String markerIcon)
    {
        if (markerIcon.equals("icon1"))
            return R.drawable.service_marker;
        else if(markerIcon.equals("icon2"))
            return R.drawable.service_marker;
        else if(markerIcon.equals("icon3"))
            return R.drawable.service_marker;
        else if(markerIcon.equals("icon4"))
            return R.drawable.service_marker;
        else if(markerIcon.equals("icon5"))
            return R.drawable.service_marker;
        else if(markerIcon.equals("icon6"))
            return R.drawable.service_marker;
        else if(markerIcon.equals("icon7"))
            return R.drawable.service_marker;
        else
            return R.drawable.service_marker;
    }
}
