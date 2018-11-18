package com.ujuzy.ujuzy.ujuzy.RetrofitClasses;

import android.app.Activity;

import com.ujuzy.ujuzy.ujuzy.Interfaces.Api;
import com.ujuzy.ujuzy.ujuzy.model.Constants;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class GetRequests
{

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Constants.HTTP.BASE_URL)
            .build();



}
