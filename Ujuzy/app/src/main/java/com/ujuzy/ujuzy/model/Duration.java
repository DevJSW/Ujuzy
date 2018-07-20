package com.ujuzy.ujuzy.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Shephard on 7/3/2018.
 */

public class Duration {
    @SerializedName("days")
    @Expose
    private Integer days;
    @SerializedName("hours")
    @Expose
    private Integer hours;

    public Integer getDays() {
        return days;
    }

    public void setDays(Integer days) {
        this.days = days;
    }

    public Integer getHours() {
        return hours;
    }

    public void setHours(Integer hours) {
        this.hours = hours;
    }
}
