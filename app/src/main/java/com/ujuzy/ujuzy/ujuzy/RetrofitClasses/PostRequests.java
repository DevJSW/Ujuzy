package com.ujuzy.ujuzy.ujuzy.RetrofitClasses;

import android.app.Activity;
import android.content.Context;
import android.media.session.MediaSession;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;
import com.ujuzy.ujuzy.ujuzy.Interfaces.Api;
import com.ujuzy.ujuzy.ujuzy.Realm.RealmToken;
import com.ujuzy.ujuzy.ujuzy.Realm.RealmTokenHelper;
import com.ujuzy.ujuzy.ujuzy.Services.DistanceTimeParser;
import com.ujuzy.ujuzy.ujuzy.model.Constants;

import org.jboss.aerogear.android.authorization.AuthorizationManager;
import org.jboss.aerogear.android.authorization.AuthzModule;
import org.jboss.aerogear.android.authorization.oauth2.OAuth2AuthorizationConfiguration;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.realm.Realm;
import io.realm.internal.objectserver.Token;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PostRequests extends Activity
{

    Retrofit retrofit = new Retrofit
            .Builder()
            .baseUrl(Constants.HTTP.BASE_URL)
            .build();

    public void CreateServiceReq( // <---------------- creating a service
            String authToken,
            String accptHeader,
            String contentType,
            String serviceName,
            String serviceDetails,
            String offerCost,
            String category,
            String serviceBilling,
            Boolean travel)
    {

        Api api = retrofit.create(Api.class);

        api.postCreateService(
                authToken,
                accptHeader,
                contentType,
                serviceName,
                serviceDetails,
                offerCost,
                category,
                serviceBilling,
                travel)
                .enqueue(new Callback<ResponseBody>()
        {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response)
            {
                if (response.isSuccessful())
                {
                    //data = response.body().user;
                }
                else {

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t)
            {

            }
        });

    }

}
