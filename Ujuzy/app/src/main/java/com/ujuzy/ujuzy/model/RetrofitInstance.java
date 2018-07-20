package com.ujuzy.ujuzy.model;


import com.ujuzy.ujuzy.services.Api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Shephard on 6/27/2018.
 */

public class RetrofitInstance
{

    private static Retrofit retrofit = null;
    private static String BASE_URL = "https://api.ujuzy.com/";

    public static Api getService()
    {
        if (retrofit == null)
        {
            retrofit = new Retrofit
                    .Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit.create(Api.class);
    }
}
