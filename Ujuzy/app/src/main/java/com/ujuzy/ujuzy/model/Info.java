package com.ujuzy.ujuzy.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Shephard on 6/27/2018.
 */

public class Info
{
    @SerializedName("RestResponse")
    @Expose
    private RestResponse restResponse;

    public RestResponse getRestResponse()
    {
        return restResponse;
    }

    public void setRestResponse(RestResponse restResponse)
    {
        this.restResponse = restResponse;
    }
}
