package com.ujuzy.ujuzy.ujuzy.Realm;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.exceptions.RealmException;

public class RealmLocationHelper
{
    Realm realm;
    RealmResults<RealmUserLocation> locations;
    Boolean saved = null;

    public RealmLocationHelper(Realm realm)
    {
        this.realm = realm;
    }

    //WRITE OR SAVE
    public Boolean save(final RealmUserLocation location)
    {
        if (location == null)
        {
            saved = false;
        } else {

            realm = Realm.getDefaultInstance();
            realm.executeTransaction(new Realm.Transaction()
            {
                @Override
                public void execute(Realm realm)
                {

                    try {

                        RealmUserLocation realmUserLocation = realm.copyToRealmOrUpdate(location);
                        saved = true;

                    } catch (RealmException e)
                    {

                        e.printStackTrace();
                        saved = false;

                    }

                }
            });
        }

        return saved;
    }

    //RETRIEVE FROM DB
    public void retreiveFromDB()
    {
        locations = realm.where(RealmUserLocation.class).findAll();
    }


    //REFRESH
    public ArrayList<RealmUserLocation> refreshDatabase()
    {
        ArrayList<RealmUserLocation> latestLocation = new ArrayList<>();

        for (RealmUserLocation realmUserLocation : locations)
        {
            latestLocation.add(realmUserLocation);
        }

        return latestLocation;
    }


}
