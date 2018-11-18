package com.ujuzy.ujuzy.ujuzy.Realm;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.exceptions.RealmException;

public class RealmUserHelper
{
    Realm realm;
    RealmResults<RealmUser> users;
    Boolean saved = null;

    public RealmUserHelper(Realm realm)
    {
        this.realm = realm;
    }

    //WRITE OR SAVE
    public Boolean save(final RealmUser user)
    {
        if (user == null)
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

                        RealmUser realmUser = realm.copyToRealmOrUpdate(user);
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
        users = realm.where(RealmUser.class).findAll();
    }

    //REFRESH
    public ArrayList<RealmUser> refreshDatabase()
    {
        ArrayList<RealmUser> latestUser = new ArrayList<>();

        for (RealmUser realmUser : users)
        {
            latestUser.add(realmUser);
        }

        return latestUser;
    }


}
