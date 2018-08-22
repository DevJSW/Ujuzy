package com.ujuzy.ujuzy.activities;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ujuzy.ujuzy.R;
import com.ujuzy.ujuzy.model.Constants;
import com.ujuzy.ujuzy.model.Login;
import com.ujuzy.ujuzy.model.SignUp;
import com.ujuzy.ujuzy.model.Token;
import com.ujuzy.ujuzy.services.Api;
import com.ujuzy.ujuzy.services.ServiceClient;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignUpActivity extends AppCompatActivity {

    private String webview_url = "https://sso.ujuzy.com/auth/realms/ujuzy/login-actions/reset-credentials?client_id=account&tab_id=zyBFF6oQ_B0";
    private static String Token;
    private Button btnLogin, btnSignUp, forgtPassBtn;
    private EditText inputEmail, inputPassword, inputFirstName, inputLastName, inputConfirmPass;
    private ImageView backBtnIv;
    private ProgressBar progressBar;
    private Retrofit retrofit;
    private Api api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        inputFirstName = (EditText) findViewById(R.id.inputFirstNameEt);
        inputLastName = (EditText) findViewById(R.id.inputLastNameEt);
        inputConfirmPass = (EditText) findViewById(R.id.inputConfirmPassEt);
        inputEmail = (EditText) findViewById(R.id.inputEmailEt);
        inputPassword = (EditText) findViewById(R.id.inputPassEt);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        initWindows();
        iniLogin();
        initSignUp();
        initBackBtn();
        initWebView();
    }

    private Retrofit getRetrofitAuth()
    {

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        if (this.retrofit == null)
        {
            this.retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.HTTP.REGISTRATION_ENDPOINT)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }

        return this.retrofit;
    }

    public void SignUp(String firstName, String lastName, String email, String password, String confirm_password)
    {
        Api api = getRetrofitAuth().create(Api.class);
        Call<SignUp> ServiceData =  api.signUp(firstName, lastName,email ,password, confirm_password);
        ServiceData.enqueue(new Callback<SignUp>() {
            @Override
            public void onResponse(Call<SignUp> call, Response<SignUp> response) {
                SignUp serviceResult = response.body();
                Toast.makeText(SignUpActivity.this, (CharSequence) serviceResult, Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<SignUp> call, Throwable t) {

                 Toast.makeText(SignUpActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
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
                Intent webView = new Intent(SignUpActivity.this, WebViewActivity.class);
                webView.putExtra("webview_url", webview_url);
                webView.putExtra("page_title", "Forgot Password");
                startActivity(webView);
            }
        });

    }

    private void initBackBtn()
    {
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
                finish();
            }
        });
    }

    private void initWindows()
    {
        Window window = SignUpActivity.this.getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            window.setStatusBarColor(ContextCompat.getColor( SignUpActivity.this,R.color.colorPrimaryDark));

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

        String first_name = inputFirstName.getText().toString();
        String last_name = inputLastName.getText().toString();
        String email = inputEmail.getText().toString();
        String confirm_password = inputConfirmPass.getText().toString();
        String password = inputPassword.getText().toString();

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);

        if (TextUtils.isEmpty(first_name)) {
            inputFirstName.setError("Enter name!");
            //Toast.makeText(getApplicationContext(), "Enter name!", Toast.LENGTH_SHORT).show();

        }

        else if (first_name.length() < 3 || first_name.length() > 15 ){
            System.out.println("Name too short or too long");
            inputFirstName.setError("Name too short or too long");
        }

        else if (TextUtils.isEmpty(last_name)) {
        inputLastName.setError("Enter your username!");
        }

        else if (last_name.length() < 3 || last_name.length() > 15 ){
            System.out.println("Name too short or too long");
            inputLastName.setError("Name too short or too long");
        }
        else if (first_name.matches(".*[!@#$%^&*+=?-].*") || last_name.matches(".*[!@#$%^&*+=?-].*")) {
            inputFirstName.setError("Name should not contain special chars e.g [! @ # $ % ^ & * + = ? -].");
            //Toast.makeText(getApplicationContext(), "Username should be one word e.g JohnDoe instead of John Doe!", Toast.LENGTH_SHORT).show();
        }

        else if (last_name.matches(".*[!@#$%^&*+=?-].*")) {
            inputLastName.setError("Name should not contain special chars e.g [! @ # $ % ^ & * + = ? -].");
            //Toast.makeText(getApplicationContext(), "Username should be one word e.g JohnDoe instead of John Doe!", Toast.LENGTH_SHORT).show();
        }

        else if (first_name.length() < 3 || first_name.length() > 15 ){
            System.out.println("Username too short or too long");
            inputFirstName.setError("Name too short or too long");
        }

        else if (last_name.length() < 3 || last_name.length() >15 ){
            System.out.println("Username too short or too long");
            inputLastName.setError("Name too short or too long");
        }

        else if (TextUtils.isEmpty(email)) {
            inputEmail.setError("Enter email address!");
            //Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();


        }
        else if (!matcher.matches()) {
            inputEmail.setError("Invalid email Address!");
            // Toast.makeText(getApplicationContext(), "Invalid email Address!", Toast.LENGTH_SHORT).show();
        }

        else if (TextUtils.isEmpty(password)) {
            inputPassword.setError("Enter password!");
            //Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();

        }

        else if (password.length() < 6 ){
            System.out.println("pass too short or too long");
            inputPassword.setError("Password too short!");
            //Toast.makeText(getApplicationContext(), "Password too short or too long!", Toast.LENGTH_SHORT).show();
        }

        else if (!password.matches(".*\\d.*")){
            System.out.println("no digits found");
            inputPassword.setError("No digits found in password! e.g [0 - 9] ");
            //Toast.makeText(getApplicationContext(), "No digits found in password! e.g [0 - 9] ", Toast.LENGTH_SHORT).show();
        }

        else if (!password.matches(".*[a-z].*")) {
            System.out.println("no lowercase letters found");
            inputPassword.setError("No lowercase letters found in password! e.g [a - z] ");
            //Toast.makeText(getApplicationContext(), "No lowercase letters found in password! e.g [a - z] ", Toast.LENGTH_SHORT).show();
        }
        else if (!password.matches(".*[!@#$%^&*+=?-].*")) {
            System.out.println("no special chars found");
            inputPassword.setError("No special chars found in password! e.g [! @ # $ % ^ & * + = ? -] ");
            //Toast.makeText(getApplicationContext(), "No special chars found in password! e.g [! @ # $ % ^ & * + = ? -] ", Toast.LENGTH_SHORT).show();

        }
        else if (TextUtils.isEmpty(confirm_password)) {
            inputConfirmPass.setError("Confirm password!");
            //Toast.makeText(getApplicationContext(), "Confirm password!", Toast.LENGTH_SHORT).show();

        } else if (!password.equals(confirm_password)) {
            inputConfirmPass.setError("Your password and confirmation password do not match!");
            //Toast.makeText(getApplicationContext(), "Your password and confirmation password do not match!", Toast.LENGTH_SHORT).show();

        }
        else {

            progressBar.setVisibility(View.VISIBLE);

           /* ServiceClient serviceClient = new ServiceClient();
            serviceClient.SignUp(first_name, last_name, email, password, confirm_password);*/

            SignUp(first_name, last_name, email, password, confirm_password);

            /*Login login = new Login(email, password);
            Call<com.ujuzy.ujuzy.model.Token> call = api.login(login);

            call.enqueue(new Callback<Token>() {
                @Override
                public void onResponse(Call<Token> call, Response<Token> response) {
                    if (response.isSuccessful()) {
                        Token = response.body().getToken();
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(SignUpActivity.this, response.body().getToken(), Toast.LENGTH_LONG).show();
                    } else {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(SignUpActivity.this, "Signup Incorrect", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Token> call, Throwable t) {

                }
            });
*/
        }

    }


}
