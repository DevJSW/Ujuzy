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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ujuzy.ujuzy.R;
import com.ujuzy.ujuzy.model.Constants;
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

    private String webview_url = "https://sso.ujuzy.com/auth/realms/ujuzy/login-actions/reset-credentials?client_id=account&tab_id=zyBFF6oQ_B0";
    private static String Token;
    private Button btnLogin, btnSignUp, forgtPassBtn;
    private EditText inputEmail, inputPassword;
    private TextView backBtnTv;
    private ImageView backBtnIv;
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
        initSignUp();
        initBackBtn();
        initWebView();
    }

    private void initWebView()
    {
        forgtPassBtn = (Button) findViewById(R.id.btn_reset_password);
        forgtPassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent webView = new Intent(LoginActivity.this, WebViewActivity.class);
                webView.putExtra("webview_url", webview_url);
                webView.putExtra("page_titile", "Forgot Password");
                startActivity(webView);
            }
        });

    }

    private void initBackBtn()
    {
        backBtnTv = (TextView) findViewById(R.id.backBtnTv);
        backBtnTv.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });

        backBtnIv = (ImageView) findViewById(R.id.backBtn);
        backBtnIv.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });
    }

    private void initSignUp()
    {
        btnSignUp = (Button) findViewById(R.id.btn_signup);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });
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
                .baseUrl(Constants.HTTP.BASE_URL)
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
        String password = inputEmail.getText().toString();

        if (!email.isEmpty() || !password.isEmpty()) {

            Login login = new Login(email, password);
            Call<Token> call = api.login(login);

            call.enqueue(new Callback<Token>() {
                @Override
                public void onResponse(Call<Token> call, Response<Token> response) {
                    if (response.isSuccessful()) {
                        Token = response.body().getToken();
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(LoginActivity.this, response.body().getToken(), Toast.LENGTH_LONG).show();
                    } else {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(LoginActivity.this, "Login Incorrect", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Token> call, Throwable t) {

                }
            });

        } else {

            progressBar.setVisibility(View.GONE);
            Toast.makeText(LoginActivity.this, "Email & password must not be empty!", Toast.LENGTH_LONG).show();

        }
    }
}
