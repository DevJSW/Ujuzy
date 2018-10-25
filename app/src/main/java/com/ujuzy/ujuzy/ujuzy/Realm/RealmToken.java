package com.ujuzy.ujuzy.ujuzy.Realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class RealmToken extends RealmObject {

    @PrimaryKey
    private String Token;

    public RealmToken(String token) {
        Token = token;
    }

    public RealmToken() {

    }

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }
}
