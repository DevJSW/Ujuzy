package com.ujuzy.ujuzy.model;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class Ujuzy extends Application
{
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration
                .Builder()
                .schemaVersion(3)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);
    }
}
