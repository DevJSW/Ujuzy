package com.ujuzy.ujuzy.model;

public class Login
{
    private String username, password, grant_type, client_id, code, client_secret;

    public Login (String username, String password, String grant_type, String client_id, String code, String client_secret)
    {
        this.username = username;
        this.password = password;
        this.grant_type = grant_type;
        this.client_id = client_id;
        this.code = code;
        this.client_secret = client_secret;
    }
}
