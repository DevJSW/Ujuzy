package com.ujuzy.ujuzy.activities;

import android.content.Intent;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ujuzy.ujuzy.R;
import com.ujuzy.ujuzy.model.Login;
import com.ujuzy.ujuzy.model.Token;
import com.ujuzy.ujuzy.model.User;
import com.ujuzy.ujuzy.services.Api;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity
{

    private static String Token;
    private String client_type = "";
    private String grant_type = "";
    private Button btnLogin;
    private EditText inputEmail, inputPassword;
    private ProgressBar progressBar;
    private Api api;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inputEmail = (EditText) findViewById(R.id.inputEmailEt);
        inputPassword = (EditText) findViewById(R.id.inputPassEt);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        initWindows();
        iniLogin();

    }

    private void initWindows()
    {
        Window window = LoginActivity.this.getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            window.setStatusBarColor(ContextCompat.getColor( LoginActivity.this,R.color.colorPrimaryDark));

            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        }

    }

    private void iniLogin()
    {

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("https://api.ujuzy.com/")
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.build();

        api = retrofit.create(Api.class);

        btnLogin = (Button) findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                login();
            }
        });
    }

    private void login()
    {

        progressBar.setVisibility(View.VISIBLE);

        String email = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();

        Login login = new Login(email, password, client_type, grant_type);
        Call<Login> call = api.login(login);

        call.enqueue(new Callback<Login>()
        {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response)
            {
                if (response.isSuccessful())
                {
                    Token = response.body().toString();
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(LoginActivity.this, response.body().toString(), Toast.LENGTH_LONG).show();
                } else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(LoginActivity.this, "Login Incorrect", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Login> call, Throwable t)
            {

            }
        });
    }
}
