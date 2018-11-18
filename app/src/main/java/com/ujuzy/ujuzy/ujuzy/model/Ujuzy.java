package com.ujuzy.ujuzy.ujuzy.model;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class Ujuzy extends Application
{
    //private Tracker tracker;

    @Override
    public void onCreate()
    {
        super.onCreate();
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration
                .Builder()
                .schemaVersion(3)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);
    }

   /* public synchronized Tracker getTracker() {
        if (tracker == null) tracker = Piwik.getInstance(this).newTracker(new TrackerConfig("http://domain.tld/piwik.php", 1));
        return tracker;
    }*/
}
