package com.ujuzy.ujuzy.ujuzy.model;

import com.google.android.gms.maps.model.BitmapDescriptor;

public class ServiceMarker
{
    private String mLabel;
    private String mIcon;
    private String mCost;
    private String mCity;
    private String service_provider;
    private String service_id;
    private String service_category;
    private String service_details;
    private Double mLatitude;
    private Double mLongitude;

    public ServiceMarker(String label, String icon, String mCost, String mCity, String service_provider, String service_id, String service_category, String service_details, Double latitude, Double longitude)
    {
        this.mLabel = label;
        this.mCity = mCity;
        this.service_provider = service_provider;
        this.service_id = service_id;
        this.service_category = service_category;
        this.service_details = service_details;
        this.mLatitude = latitude;
        this.mLongitude = longitude;
        this.mIcon = icon;
        this.mCost = mCost;
    }

    public String getmLabel()
    {
        return mLabel;
    }

    public void setmLabel(String mLabel)
    {
        this.mLabel = mLabel;
    }

    public String getmIcon()
    {
        return mIcon;
    }

    public void setmIcon(String icon)
    {
        this.mIcon = icon;
    }

    public Double getmLatitude()
    {
        return mLatitude;
    }

    public void setmLatitude(Double mLatitude)
    {
        this.mLatitude = mLatitude;
    }

    public Double getmLongitude()
    {
        return mLongitude;
    }

    public void setmLongitude(Double mLongitude)
    {
        this.mLongitude = mLongitude;
    }

    public String getmCost() {
        return mCost;
    }

    public void setmCost(String mCost) {
        this.mCost = mCost;
    }

    public String getmCity() {
        return mCity;
    }

    public void setmCity(String mCity) {
        this.mCity = mCity;
    }

    public String getService_provider() {
        return service_provider;
    }

    public void setService_provider(String service_provider) {
        this.service_provider = service_provider;
    }

    public String getService_id() {
        return service_id;
    }

    public void setService_id(String service_id) {
        this.service_id = service_id;
    }

    public String getService_category() {
        return service_category;
    }

    public void setService_category(String service_category) {
        this.service_category = service_category;
    }

    public String getService_details() {
        return service_details;
    }

    public void setService_details(String service_details) {
        this.service_details = service_details;
    }
}
