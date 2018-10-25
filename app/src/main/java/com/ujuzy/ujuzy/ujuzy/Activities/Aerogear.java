package com.ujuzy.ujuzy.ujuzy.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Pair;

import com.ujuzy.ujuzy.ujuzy.Realm.RealmToken;
import com.ujuzy.ujuzy.ujuzy.Realm.RealmTokenHelper;
import com.ujuzy.ujuzy.ujuzy.app_intro.SigninOrSkipActivity;
import com.ujuzy.ujuzy.ujuzy.model.Constants;

import org.jboss.aerogear.android.authorization.AuthorizationManager;
import org.jboss.aerogear.android.authorization.AuthzModule;
import org.jboss.aerogear.android.authorization.oauth2.OAuth2AuthorizationConfiguration;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

import io.realm.Realm;

public class Aerogear
{

    public Context context;
    public Realm realm;

    public void initAerogearLogin()
    {
        try {

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

            authzModule.requestAccess((Activity) context, new org.jboss.aerogear.android.core.Callback<String>() {
                @Override
                public void onSuccess(final String data) {

                    //SAVE TOKEN TO REALM DATABASE
                    RealmToken token = new RealmToken();
                    token.setToken(data);

                    realm = Realm.getDefaultInstance();
                    RealmTokenHelper helper = new RealmTokenHelper(realm);
                    helper.save(token);

                }

                @Override
                public void onFailure(Exception e) {
                    //Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });


        } catch (MalformedURLException e) {
            // e.printStackTrace();
        }
    }
}
