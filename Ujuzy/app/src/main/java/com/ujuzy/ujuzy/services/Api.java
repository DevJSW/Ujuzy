package com.ujuzy.ujuzy.services;



import com.ujuzy.ujuzy.model.Login;
import com.ujuzy.ujuzy.model.Service;
import com.ujuzy.ujuzy.model.SignUp;
import com.ujuzy.ujuzy.model.Token;
import com.ujuzy.ujuzy.model.User;

import io.realm.RealmList;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
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

    @GET("/services")
    Call<ResponseBody> getPosts();

    @GET("services")
    Call<RealmList<ResponseBody>> getResServices();

    @GET("services/{service_id}/reviews")
    Call<Service> getReviewsByServiceId(@Header("Authentication") String token, @Path("service_id") String service_id);

    @GET("services")
    Call<Service> getServicesById(@Query("id") String id);

    @FormUrlEncoded
    @POST("token")
    Call<Login> login(@Field("username") String username,
                      @Field("password") String password,
                      @Field("grant_type") String grant_type,
                      @Field("client_id") String client_id,
                      @Field("cient_secret") String cient_secret);

    @FormUrlEncoded
    @POST("auth")
    Call<SignUp> signUp(@Field("firstName") String firstName,
                        @Field("lastName") String lastName,
                        @Field("email") String email,
                        @Field("password") String password,
                        @Field("confirm-password") String confirm_password);

}
