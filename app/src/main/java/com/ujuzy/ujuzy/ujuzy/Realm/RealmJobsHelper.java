package com.ujuzy.ujuzy.ujuzy.Realm;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.exceptions.RealmException;

public class RealmJobsHelper
{
    Realm realm;
    RealmResults<RealmJobs> services;
    Boolean saved = null;

    public RealmJobsHelper(Realm realm)
    {
        this.realm = realm;
    }

    //WRITE OR SAVE
    public Boolean save(final RealmJobs service)
    {
        if (service == null)
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

                        RealmJobs realmUser = realm.copyToRealmOrUpdate(service);
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
        services = realm.where(RealmJobs.class).findAll();
    }

    //REFRESH
    public ArrayList<RealmJobs> refreshDatabase()
    {
        ArrayList<RealmJobs> latestUser = new ArrayList<>();

        for (RealmJobs realmUser : services)
        {
            latestUser.add(realmUser);
        }

        return latestUser;
    }


}
