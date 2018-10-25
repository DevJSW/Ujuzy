package com.ujuzy.ujuzy.ujuzy.Realm;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.exceptions.RealmException;

public class RealmFavouriteHelper
{
    Realm realm;
    RealmResults<RealmFavourite> services;
    Boolean saved = null;

    public RealmFavouriteHelper(Realm realm)
    {
        this.realm = realm;
    }

    //WRITE OR SAVE
    public Boolean save(final RealmFavourite service)
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

                        RealmFavourite realmService = realm.copyToRealmOrUpdate(service);
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
        services = realm.where(RealmFavourite.class).findAll();
    }

    //FILTER DATABASE
    public void filterRealmDatabase(String category, String category_title)
    {
        services = realm.where(RealmFavourite.class).equalTo(category, category_title).findAll();
    }

    //SEARCH FILTER DATABASE
    public void searchRealmDatabase(String searchInput, String searchText)
    {
        services = realm.where(RealmFavourite.class).beginsWith(searchInput, searchText).findAll();
    }

    //REFRESH
    public ArrayList<RealmFavourite> refreshDatabase()
    {
        ArrayList<RealmFavourite> latestService = new ArrayList<>();

        for (RealmFavourite realmService : services)
        {
            latestService.add(realmService);
        }

        return latestService;
    }


}
