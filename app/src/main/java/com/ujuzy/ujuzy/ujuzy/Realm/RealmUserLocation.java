package com.ujuzy.ujuzy.ujuzy.Realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class RealmUserLocation extends RealmObject
{
    @PrimaryKey
    private String city;
    private String state;
    private String country;
    private String postal_code;
    private String known_name;
    private String latitude;
    private String longitude;

    public RealmUserLocation() {

    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPostal_code() {
        return postal_code;
    }

    public void setPostal_code(String postal_code) {
        this.postal_code = postal_code;
    }

    public String getKnown_name() {
        return known_name;
    }

    public void setKnown_name(String known_name) {
        this.known_name = known_name;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }


}
