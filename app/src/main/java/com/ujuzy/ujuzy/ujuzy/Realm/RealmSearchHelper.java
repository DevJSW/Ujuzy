package com.ujuzy.ujuzy.ujuzy.Realm;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.exceptions.RealmException;

public class RealmSearchHelper
{
    Realm realm;
    RealmResults<RealmSearch> services;
    Boolean saved = null;

    public RealmSearchHelper(Realm realm)
    {
        this.realm = realm;
    }

    //WRITE OR SAVE
    public Boolean save(final RealmSearch service)
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

                        RealmSearch realmService = realm.copyToRealmOrUpdate(service);
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
        services = realm.where(RealmSearch.class).findAll();
    }

    //SEARCH FILTER DATABASE
    public void searchRealmDatabase(String searchInput, String searchText)
    {
        services = realm.where(RealmSearch.class).beginsWith(searchInput, searchText).findAll();
    }

    //REFRESH
    public ArrayList<RealmSearch> refreshDatabase()
    {
        ArrayList<RealmSearch> latestService = new ArrayList<>();

        for (RealmSearch realmService : services)
        {
            latestService.add(realmService);
        }

        return latestService;
    }


}
