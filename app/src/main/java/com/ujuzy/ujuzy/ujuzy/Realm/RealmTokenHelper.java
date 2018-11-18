package com.ujuzy.ujuzy.ujuzy.Realm;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.exceptions.RealmException;

public class RealmTokenHelper
{
    Realm realm;
    RealmResults<RealmToken> tokens;
    Boolean saved = null;

    public RealmTokenHelper(Realm realm)
    {
        this.realm = realm;
    }

    //WRITE OR SAVE
    public Boolean save(final RealmToken token)
    {
        if (token == null)
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

                        RealmToken realmToken = realm.copyToRealmOrUpdate(token);
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
        tokens = realm.where(RealmToken.class).findAll();
    }

    //SEARCH FILTER DATABASE
    public void searchRealmDatabase(String searchInput, String searchText)
    {
        tokens = realm.where(RealmToken.class).beginsWith(searchInput, searchText).findAll();
    }

    //REFRESH
    public ArrayList<RealmToken> refreshDatabase()
    {
        ArrayList<RealmToken> latestService = new ArrayList<>();

        for (RealmToken realmToken : tokens)
        {
            latestService.add(realmToken);
        }

        return latestService;
    }

}
