package com.ujuzy.ujuzy.model;

public class Login
{
    private String username;
    private String password;
    private String grant_type;
    private String client_id;

    public Login (String username, String password, String grant_type, String client_id)
    {
        this.username = username;
        this.password = password;
        this.grant_type = grant_type;
        this.client_id = client_id;
    }


}
