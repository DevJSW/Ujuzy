package com.ujuzy.ujuzy.ujuzy.Activities;

import android.media.Rating;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Pair;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ujuzy.ujuzy.ujuzy.R;
import com.ujuzy.ujuzy.ujuzy.Realm.RealmToken;
import com.ujuzy.ujuzy.ujuzy.Realm.RealmTokenHelper;
import com.ujuzy.ujuzy.ujuzy.model.Constants;

import org.jboss.aerogear.android.authorization.AuthorizationManager;
import org.jboss.aerogear.android.authorization.AuthzModule;
import org.jboss.aerogear.android.authorization.oauth2.OAuth2AuthorizationConfiguration;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmChangeListener;

public class PostReviewsActivity extends AppCompatActivity {

    String feedback = "";
    float ratingsValue = 0;
    String service_id = "";
    private TextView toolbarTv, ratingTv;
    private EditText feedbackInput;
    private RatingBar feedbackRating;
    private RequestQueue requestQueue;
    private Realm realm;
    private RealmChangeListener realmChangeListener;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_reviews);

        service_id = getIntent().getStringExtra("service_id");

        initToolbar();
        initPostFeedback();
        initRatingsValue();
    }

    private void initRatingsValue()
    {
        ratingTv = (TextView) findViewById(R.id.ratingTv);
        feedbackRating.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ratingsValue = feedbackRating.getRating();
                ratingTv.setText((int) ratingsValue);
            }
        });

    }

    private void initPostFeedback()
    {
        feedbackRating = (RatingBar) findViewById(R.id.serviceRatingBr);
        feedbackInput = (EditText) findViewById(R.id.inputFeedback);
        feedback = feedbackInput.getText().toString();

        if (TextUtils.isEmpty(feedback)) {
            feedbackInput.setError("Feedback must not be Empty");
        } else {

            try
            {

                final AuthzModule authzModule = AuthorizationManager
                        .config("KeyCloakAuthz", OAuth2AuthorizationConfiguration.class)
                        .setBaseURL(new URL(Constants.HTTP.AUTH_BASE_URL))
                        .setAuthzEndpoint("/auth/realms/ujuzy/protocol/openid-connect/auth")
                        .setAccessTokenEndpoint("/auth/realms/ujuzy/protocol/openid-connect/token")
                        .setAccountId("account")
                        .setClientId("account")
                        .setRedirectURL("https://ujuzy.com")
                        .setScopes(Arrays.asList("openid"))
                        .addAdditionalAuthorizationParam((Pair.create("grant_type", "password")))
                        .asModule();

                authzModule.requestAccess(PostReviewsActivity.this, new org.jboss.aerogear.android.core.Callback<String>() {
                    @Override
                    public void onSuccess(final String data) {

                        //SAVE TOKEN TO REALM DATABASE
                        RealmToken token = new RealmToken();
                        token.setToken(data);

                        //initRetrofit();
                        Map<String, String> params= new HashMap<String, String>();
                        params.put("review",feedback);
                        params.put("service_id",service_id);
                        params.put("professionalism", String.valueOf(feedbackRating.getRating()));

                        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                                Constants.HTTP.REVIEW_SERVICE_JSON_URL, new JSONObject(params),
                                new com.android.volley.Response.Listener<JSONObject>()
                                {
                                    @Override
                                    public void onResponse(JSONObject response)
                                    {

                                        Toast.makeText(PostReviewsActivity.this, "Your feedback successfully", Toast.LENGTH_LONG).show();
                                        finish();

                                    }
                                }, new com.android.volley.Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error)
                            {
                                Toast.makeText(PostReviewsActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                            }

                        }) {

                            /**
                             * Passing some request headers
                             * */
                            @Override
                            public Map<String, String> getHeaders() throws AuthFailureError {

                                HashMap<String, String> headers = new HashMap<String, String>();
                                headers.put("Authorization","Bearer "+ data);
                                headers.put("Content-Type", "application/json; charset=utf-8");
                                headers.put("Accept","application/json");
                                return headers;
                            }

                        };

                        // Adding request to request queue
                        requestQueue = Volley.newRequestQueue(PostReviewsActivity.this);
                        requestQueue.add(jsonObjReq);

                        realm = Realm.getDefaultInstance();
                        RealmTokenHelper helper = new RealmTokenHelper(realm);
                        helper.save(token);

                    }

                    @Override
                    public void onFailure(Exception e) {
                        //Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        authzModule.deleteAccount();
                    }
                });


            } catch (MalformedURLException e) {
                // e.printStackTrace();
            }
        }
    }

    private void initToolbar()
    {
        toolbarTv = (TextView) findViewById(R.id.toolbarTv);
        toolbarTv.setText("Feedback");
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        realm = Realm.getDefaultInstance();
        if (realmChangeListener != null)
            realm.removeChangeListener(realmChangeListener);
    }
}
