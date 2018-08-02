package com.ujuzy.ujuzy.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Shephard on 7/3/2018.
 */

public class Duration {
    @SerializedName("days")
    @Expose
    private Object days;
    @SerializedName("hours")
    @Expose
    private Object hours;

    public Object getDays() {
        return days;
    }

    public void setDays(Object days) {
        this.days = days;
    }

    public Object getHours() {
        return hours;
    }

    public void setHours(Object hours) {
        this.hours = hours;
    }

}
