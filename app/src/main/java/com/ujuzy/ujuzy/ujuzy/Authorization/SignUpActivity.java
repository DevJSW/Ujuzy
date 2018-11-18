package com.ujuzy.ujuzy.ujuzy.Authorization;

import android.content.Intent;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import com.ujuzy.ujuzy.ujuzy.Activities.MapsActivity;
import com.ujuzy.ujuzy.ujuzy.R;

public class SignUpActivity extends AppCompatActivity
{

    private EditText inputFirstName, inputLastName, inputEmail, inputPass, inputConfirmPass;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        init();
        initWindows();
        initSignUpBtn();
    }

    private void init()
    {
        inputConfirmPass = (EditText) findViewById(R.id.inputConfirmPass);
        inputEmail = (EditText) findViewById(R.id.inputEmail);
        inputLastName = (EditText) findViewById(R.id.inputLastName);
        inputFirstName = (EditText) findViewById(R.id.inputEmail);
    }

    private void initSignUpBtn()
    {
        startActivity(new Intent(SignUpActivity.this, MapsActivity.class));
    }

    private void initWindows()
    {
        Window window = this.getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            window.setStatusBarColor(ContextCompat.getColor( this,R.color.white));

            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        }

    }
}
