package com.ujuzy.ujuzy.ujuzy.Authorization;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.InputQueue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ujuzy.ujuzy.ujuzy.Activities.RequestServiceActivity;
import com.ujuzy.ujuzy.ujuzy.R;
import com.ujuzy.ujuzy.ujuzy.Realm.RealmToken;
import com.ujuzy.ujuzy.ujuzy.Realm.RealmTokenHelper;
import com.ujuzy.ujuzy.ujuzy.UI_UX.Windows;
import com.ujuzy.ujuzy.ujuzy.model.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import io.realm.Realm;

public class LoginActivity extends AppCompatActivity
{

    private String username, password, grant_type, client_id, client_secret;
    private static String Token;
    private Button btnLogin, btnSignUp, forgtPassBtn;
    private EditText inputEmail, inputPassword;
    private TextView backBtnTv;
    private ImageView backBtnIv;
    private RequestQueue requestQueue;
    private Realm realm;
    private Windows window;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initWindows();
        initLognBtn();
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

    private void initLognBtn()
    {

        btnLogin = (Button) findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                //initLogin();
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });
    }

    private void initLogin()
    {
        inputEmail = (EditText) findViewById(R.id.inputEmailEt);
        inputPassword = (EditText) findViewById(R.id.inputPassEt);

        username = inputEmail.getText().toString();
        password = inputPassword.getText().toString();

        if (TextUtils.isEmpty(username))
        {
            inputEmail.setError("Enter your username!");
        } else if (TextUtils.isEmpty(password))
        {
            inputPassword.setError("Enter your password!");
        } else {

            initUserLogin();
        }

    }

    private void initUserLogin()
    {

        //initRetrofit();

        Map<String, String> params= new HashMap<String, String>();
        params.put("username",username);
        params.put("password",password);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Constants.HTTP.USER_LOGIN_JSON_URL, new JSONObject(params),
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response)
                    {

                        //SAVE TOKEN TO REALM DATABASE
                       /* RealmToken token = new RealmToken();
                        realm = Realm.getDefaultInstance();
                        RealmTokenHelper helper = new RealmTokenHelper(realm);
                        helper.save(token);*/

                        Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_LONG).show();
                        finish();

                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Toast.makeText(LoginActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                NetworkResponse response = error.networkResponse;
                if (error instanceof ServerError && response != null) {
                    try {
                        String res = new String(response.data,
                                HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                        // Now you can use any deserializer to make sense of data
                        JSONObject obj = new JSONObject(res);
                    } catch (UnsupportedEncodingException e1)
                    {
                        // Couldn't properly decode data to string
                        e1.printStackTrace();
                    } catch (JSONException e2) {
                        // returned data is not JSONObject?
                        e2.printStackTrace();
                    }
                }
            }

        }) {

            /**
             * Passing some request headers
             * */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("Accept","application/json");
                return headers;
            }

        };

        // Adding request to request queue
        requestQueue = Volley.newRequestQueue(LoginActivity.this);
        requestQueue.add(jsonObjReq);


    }


}
