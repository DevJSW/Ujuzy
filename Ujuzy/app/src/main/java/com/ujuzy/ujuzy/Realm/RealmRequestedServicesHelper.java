package com.ujuzy.ujuzy.Realm;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.exceptions.RealmException;

public class RealmRequestedServicesHelper
{
    Realm realm;
    RealmResults<RealmRequestedService> services;
    Boolean saved = null;

    public RealmRequestedServicesHelper(Realm realm)
    {
        this.realm = realm;
    }

    //WRITE OR SAVE
    public Boolean save(final RealmRequestedUserService service)
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

                        RealmRequestedUserService realmUser = realm.copyToRealmOrUpdate(service);
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
        services = realm.where(RealmRequestedService.class).findAll();
    }

    //REFRESH
    public ArrayList<RealmRequestedService> refreshDatabase()
    {
        ArrayList<RealmRequestedService> latestUser = new ArrayList<>();

        for (RealmRequestedService realmUser : services)
        {
            latestUser.add(realmUser);
        }

        return latestUser;
    }


}
