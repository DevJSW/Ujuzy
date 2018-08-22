package com.ujuzy.ujuzy.services;

import android.content.Context;
import android.widget.Toast;

import com.ujuzy.ujuzy.activities.SignUpActivity;
import com.ujuzy.ujuzy.model.Constants;
import com.ujuzy.ujuzy.model.Login;
import com.ujuzy.ujuzy.model.Service;
import com.ujuzy.ujuzy.model.SignUp;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public class ServiceClient {

    private Retrofit retrofit;
    private Context context;

    private Retrofit getRetrofit()
    {
        if (this.retrofit == null)
        {
            this.retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.HTTP.TOKEN_ENDPOINT)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return this.retrofit;
    }

    private Retrofit getRetrofitAuth()
    {
        if (this.retrofit == null)
        {
            this.retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.HTTP.REGISTRATION_ENDPOINT)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();  // internet of things
        }
        return this.retrofit;
    }

  /*  public void postLogin(String username, String password, String grant_type, String client_id)
    {
        Api api = getRetrofit().create(Api.class);
        Call<Login> ServiceData =  api.login(username, password, grant_type, client_id);
        ServiceData.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
                Login serviceResult = response.body();
            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {

               // Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }*/

    public void SignUp(String firstName, String lastName, String email, String password, String confirm_password)
    {
        Api api = getRetrofitAuth().create(Api.class);
        Call<SignUp> ServiceData =  api.signUp(firstName, lastName,email ,password, confirm_password);
        ServiceData.enqueue(new Callback<SignUp>() {
            @Override
            public void onResponse(Call<SignUp> call, Response<SignUp> response)
            {
                SignUp serviceResult = response.body();
            }

            @Override
            public void onFailure(Call<SignUp> call, Throwable t)
            {

               // Toast.makeText(SignUpActivity., t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }

    public void getProfServices()
    {
        Api api = getRetrofit().create(Api.class);
        Call<Service> ServiceData =  api.getServices();
        ServiceData.enqueue(new Callback<Service>() {
            @Override
            public void onResponse(Call<Service> call, Response<Service> response) {
                Service serviceResult = response.body();
            }

            @Override
            public void onFailure(Call<Service> call, Throwable t) {

            }
        });
    }
}
