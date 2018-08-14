package com.ujuzy.ujuzy.activities;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.ujuzy.ujuzy.Realm.RealmHelper;
import com.ujuzy.ujuzy.Realm.RealmTokenHelper;

import io.realm.Realm;

public class IsUserLoggedIn {

    Realm realm;
    Context context;

    public IsUserLoggedIn(Context context)
    {
        this.context = context;
    }

    public void IsUserLoggedIn() {
        realm = Realm.getDefaultInstance();
        final RealmTokenHelper helper = new RealmTokenHelper(realm);

        //CHECK IF DATABASE IS EMPTY
        if (helper.refreshDatabase().size() < 1 || helper.refreshDatabase().size() == 0)
        {
            // start activity
            Intent intent = new Intent(context, LoginActivity.class);
            context.startActivity(intent);

        } else {
            // don't do shit
        }
    }

}
