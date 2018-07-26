package com.ujuzy.ujuzy.services;



import com.ujuzy.ujuzy.model.Login;
import com.ujuzy.ujuzy.model.Service;
import com.ujuzy.ujuzy.model.Token;
import com.ujuzy.ujuzy.model.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Shephard on 7/3/2018.
 */

public interface Api
{
    @GET("services")
    Call<Service> getServices();

    @GET("services/{service_id}/reviews")
    Call<Service> getReviewsByServiceId(@Header("Authentication") String token, @Path("service_id") String service_id);

    @GET("services")
    Call<Service> getServicesById(@Query("id") String id);

    @POST("login")
    Call<Token> login(@Body Login login);
}
