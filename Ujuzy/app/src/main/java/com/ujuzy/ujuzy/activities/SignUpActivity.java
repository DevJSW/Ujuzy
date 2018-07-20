package com.ujuzy.ujuzy.activities;

import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.ujuzy.ujuzy.R;
import com.ujuzy.ujuzy.services.Api;

public class SignUpActivity extends AppCompatActivity
{

    private static String Token;
    private Button btnSignUp;
    private EditText inputFirstName, inputLastName, inputEmail, inputPassword, inputConfirmPassword;
    private ProgressBar progressBar;
    private Api api;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        inputConfirmPassword = (EditText) findViewById(R.id.inputConfirmPassEt);
        inputEmail = (EditText) findViewById(R.id.inputEmailEt);
        inputFirstName = (EditText) findViewById(R.id.inputFirstNameEt);
        inputLastName = (EditText) findViewById(R.id.inputLastNameEt);
        inputPassword = (EditText) findViewById(R.id.inputPassEt);

        initWindows();
        initSignUp();
    }

    private void initSignUp()
    {

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
}
