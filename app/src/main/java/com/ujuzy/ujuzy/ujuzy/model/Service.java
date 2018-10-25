package com.ujuzy.ujuzy.ujuzy.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Shephard on 7/3/2018.
 */

public class Service {
    @SerializedName("data")
    @Expose
    private List<Datum> data = null;

    public List<Datum> getData()
    {
        return data;
    }

    public void setData(List<Datum> data)
    {
        this.data = data;
    }
}
