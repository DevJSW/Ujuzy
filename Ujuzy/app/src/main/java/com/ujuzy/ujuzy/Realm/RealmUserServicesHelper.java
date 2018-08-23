package com.ujuzy.ujuzy.Realm;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.exceptions.RealmException;

public class RealmUserServicesHelper
{
    Realm realm;
    RealmResults<RealmUserService> services;
    Boolean saved = null;

    public RealmUserServicesHelper(Realm realm)
    {
        this.realm = realm;
    }

    //WRITE OR SAVE
    public Boolean save(final RealmUserService service)
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

                        RealmUserService realmUser = realm.copyToRealmOrUpdate(service);
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
        services = realm.where(RealmUserService.class).findAll();
    }

    //REFRESH
    public ArrayList<RealmUserService> refreshDatabase()
    {
        ArrayList<RealmUserService> latestUser = new ArrayList<>();

        for (RealmUserService realmUser : services)
        {
            latestUser.add(realmUser);
        }

        return latestUser;
    }


}
