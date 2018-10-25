package com.ujuzy.ujuzy.ujuzy.model;

import com.google.android.gms.maps.model.LatLng;

public class MyService /*implements ClusterItem*/
{

    LatLng mPosition;
    String mTitle;
    String mSnippet;

    public MyService(double lat, double lng) {
        mPosition = new LatLng(lat, lng);
    }

    public MyService(double lat, double lng, String title, String snippet) {
        mPosition = new LatLng(lat, lng);
        mTitle = title;
        mSnippet = snippet;
    }

    public LatLng getmPosition() {
        return mPosition;
    }

    public String getmTitle() {
        return mTitle;
    }

    public String getmSnippet() {
        return mSnippet;
    }

}
