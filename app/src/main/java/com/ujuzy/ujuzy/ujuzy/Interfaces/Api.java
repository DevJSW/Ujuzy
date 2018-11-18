package com.ujuzy.ujuzy.ujuzy.Interfaces;

import com.ujuzy.ujuzy.ujuzy.model.Service;

import java.util.logging.Filter;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
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
    @POST("/services")
    Call<ResponseBody> postCreateService(@Header("Authorization: ") String token,
                                         @Header("Accept: ") String accptAppJson,
                                         @Header("Content-Type: ") String contentType,
                                         @Query("service_name") String service_name,
                                         @Query("service_details") String service_details,
                                         @Query("offer_cost") String service_cost,
                                         @Query("category") String service_category,
                                         @Query("billing") String service_billing,
                                         @Query("travel") Boolean travel);
}
