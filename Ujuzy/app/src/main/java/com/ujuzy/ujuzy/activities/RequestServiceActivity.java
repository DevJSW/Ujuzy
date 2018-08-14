package com.ujuzy.ujuzy.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.ujuzy.ujuzy.R;
import com.ujuzy.ujuzy.Realm.RealmFavourite;
import com.ujuzy.ujuzy.Realm.RealmFavouriteHelper;
import com.ujuzy.ujuzy.map.MapsActivity;

import io.realm.Realm;

public class RequestServiceActivity extends AppCompatActivity {

    String serviceCost = "";
    String serviceName = "";
    String serviceId = "";
    String first_name = "";
    String last_name = "";
    String profile_pic = "";
    String no_of_personnel = "";

    private TextView serviceNameTv, userFullName, noOfPersonnel, serviceCostTv;
    private ImageView userAvatar, backBtnIv;
    private EditText inputDateEt, inputRequestEt;
    private Button confirmBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_service);
        initWindows();
        initServiceInfo();
        initBackBtn();
        initConfirmBtn();
    }

    private void initConfirmBtn() {

        confirmBtn = (Button) findViewById(R.id.btn_request);
        inputDateEt = (EditText) findViewById(R.id.inputDateTxt);
        inputRequestEt = (EditText) findViewById(R.id.inputRequestTxt);

        final String date = inputDateEt.getText().toString();
        final String request = inputRequestEt.getText().toString();

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(date)) {
                    inputDateEt.setError("Enter time and date most suitable for you");
                } else if (TextUtils.isEmpty(request)){
                    inputRequestEt.setError("Describe your request!");
                } else {

                }

            }
        });
    }

    private void initServiceInfo()
    {

        userAvatar = (ImageView) findViewById(R.id.iv_avatar);
        serviceNameTv = (TextView) findViewById(R.id.tv_service_name);
        userFullName = (TextView) findViewById(R.id.tv_user_name);
        noOfPersonnel = (TextView) findViewById(R.id.tv_service_no_personnel);
        serviceCostTv = (TextView) findViewById(R.id.tv_service_cost);

        serviceId = getIntent().getStringExtra("service_id");
        serviceName = getIntent().getStringExtra("service_name");

        serviceCost = getIntent().getStringExtra("service_cost");
        no_of_personnel = getIntent().getStringExtra("no_of_personnel");

        first_name = getIntent().getStringExtra("first_name");
        last_name = getIntent().getStringExtra("last_name");
        profile_pic = getIntent().getStringExtra("profile_pic");

        serviceNameTv = (TextView) findViewById(R.id.tv_service_name);

        serviceNameTv.setText(serviceName);
        if (no_of_personnel != null)
        noOfPersonnel.setText( no_of_personnel);
        if (serviceCost != null)
        serviceCostTv.setText( serviceCost);
        if (first_name != null || last_name != null)
        userFullName.setText( first_name + " " + last_name);


        Glide.with(getApplicationContext())
                .load(profile_pic)
                .asBitmap()
                .placeholder(R.drawable.placeholder_image)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .fitCenter()
                .into(new BitmapImageViewTarget(userAvatar) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(getApplicationContext().getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        userAvatar.setImageDrawable(circularBitmapDrawable);
                    }
                });

    }


    private void initWindows()
    {
        Window window = RequestServiceActivity.this.getWindow();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            window.setStatusBarColor(ContextCompat.getColor( RequestServiceActivity.this,R.color.colorPrimaryDark));

            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        }

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

}
