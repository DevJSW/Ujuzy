package com.ujuzy.ujuzy.Utils;

import com.ujuzy.ujuzy.model.Service;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Query;

public interface ElasticSearchApi {

    @GET("search")
    Call<Service> search(@HeaderMap Map<String, String> headers,
                         @Query("default_operator") String operator,
                         @Query("q") String query
    );
}
