package com.ujuzy.ujuzy.Realm;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.exceptions.RealmException;

public class RealmHelper
{
    Realm realm;
    RealmResults<RealmService> services;
    Boolean saved = null;

    public RealmHelper(Realm realm)
    {
        this.realm = realm;
    }

    //WRITE OR SAVE
    public Boolean save(final RealmService service)
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

                        RealmService realmService = realm.copyToRealmOrUpdate(service);
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
        services = realm.where(RealmService.class).findAll();
    }

    //FILTER DATABASE
    public void filterRealmDatabase(String category, String category_title)
    {
        services = realm.where(RealmService.class).equalTo(category, category_title).findAll();
    }

    //SEARCH FILTER DATABASE
    public void searchRealmDatabase(String searchInput, String searchText)
    {
        services = realm.where(RealmService.class).beginsWith(searchInput, searchText).findAll();
    }

    //REFRESH
    public ArrayList<RealmService> refreshDatabase()
    {
        ArrayList<RealmService> latestService = new ArrayList<>();

        for (RealmService realmService : services)
        {
            latestService.add(realmService);
        }

        return latestService;
    }


}
