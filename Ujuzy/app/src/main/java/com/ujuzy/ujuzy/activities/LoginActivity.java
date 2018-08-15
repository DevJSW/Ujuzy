package com.ujuzy.ujuzy.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.ujuzy.ujuzy.Realm.RealmToken;
import com.ujuzy.ujuzy.Realm.RealmTokenHelper;
import com.ujuzy.ujuzy.model.Constants;
import com.ujuzy.ujuzy.model.Login;
import com.ujuzy.ujuzy.services.Api;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity
{

    private String username, password, grant_type, client_id, code;
    private String webview_url = "https://sso.ujuzy.com/auth/realms/ujuzy/login-actions/reset-credentials?client_id=account&tab_id=zyBFF6oQ_B0";
    private static String Token;
    private Button btnLogin, btnSignUp, forgtPassBtn;
    private EditText inputEmail, inputPassword;
    private TextView backBtnTv;
    private ImageView backBtnIv;
    private ProgressBar progressBar;
    private Retrofit retrofit;
    private Api api;

    private Realm realm;
    private RealmChangeListener realmChangeListener;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inputEmail = (EditText) findViewById(R.id.inputEmailEt);
        inputPassword = (EditText) findViewById(R.id.inputPassEt);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        //checkIfUserIsLoggedIn();

        initWindows();
        initSignUp();
        initBackBtn();
        initWebView();

        initLogniBtn();
    }

    private void checkIfUserIsLoggedIn()
    {
        IsUserLoggedIn IsUserLoggedIn = new IsUserLoggedIn(getApplicationContext());
        IsUserLoggedIn.IsUserLoggedIn();
     }

    private void initLogniBtn() {

        btnLogin = (Button) findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                initLogin();
            }
        });
    }

    private void initLogin()
    {
        username = inputEmail.getText().toString();
        password = inputPassword.getText().toString();
        client_id = "account";
        grant_type = "password";
        code = "password";

        if (TextUtils.isEmpty(username)) {
            inputEmail.setError("Enter your email!");
        } else if (TextUtils.isEmpty(password)){
            inputPassword.setError("Enter your password!");
        } else {
            postLogin(username, password, grant_type, client_id);
        }

    }

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

    public void postLogin(String username, String password, String grant_type, String client_id)
    {
        Api api = getRetrofit().create(Api.class);
        Call<Login> ServiceData =  api.login(username, password, grant_type, client_id);
        ServiceData.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
                Login token = response.body();
                progressBar.setVisibility(View.GONE);

                if (token != null)
                {
                    RealmToken realmToken = new RealmToken();
                    realmToken.setToken(token.toString());

                    //SAVE
                    realm = Realm.getDefaultInstance();
                    RealmTokenHelper helper = new RealmTokenHelper(realm);
                    helper.save(realmToken);
                }
            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {

                 Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);

            }
        });
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
                webView.putExtra("page_title", "Forgot Password");
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

}
