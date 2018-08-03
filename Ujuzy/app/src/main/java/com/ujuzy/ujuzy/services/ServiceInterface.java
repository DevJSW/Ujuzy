package com.ujuzy.ujuzy.services;

import com.ujuzy.ujuzy.model.Service;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ServiceInterface {
    @GET("/services")
    Call<Service> getServices();
}
