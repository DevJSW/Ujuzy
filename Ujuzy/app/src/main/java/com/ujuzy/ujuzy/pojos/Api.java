package com.ujuzy.ujuzy.pojos;

import com.ujuzy.ujuzy.model.Service;

import java.util.logging.Filter;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Shephard on 7/10/2018.
 */

public interface Api
{
    @GET("/services/{service_id}/reviews")
    Call<ResponseBody> getReviewsByServiceId(@Query("service_id") String service_id);
    @GET("/services")
    Call<Service> getServicesByCreatedBy(@Query("created_by") String created_by);
    @GET("/services")
    Call<Filter> getFilterList(@Query("id") String service_id);
}
