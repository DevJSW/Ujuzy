package com.ujuzy.ujuzy.ujuzy.Realm;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class RealmServiceImage extends RealmObject
{
    @PrimaryKey
    private String id;
    private String thumb;



    public RealmServiceImage(String id, String thumb) {
        this.id = id;
        this.thumb = thumb;

    }


    public RealmServiceImage() {

    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }
}
