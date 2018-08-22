package com.ujuzy.ujuzy.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Constants
{
    public static final class HTTP
    {
        public static final String BASE_URL = "https://api.ujuzy.com/";
        public static final String AUTH_BASE_URL = "https://sso.ujuzy.com/";
        public static final String TOKEN_ENDPOINT = "https://sso.ujuzy.com/auth/realms/ujuzy/protocol/openid-connect/";
        public static final String REGISTRATION_ENDPOINT = "https://sso.ujuzy.com/auth/realms/ujuzy/clients-registrations/";
        public static final String SERVICES_ENDPOINT = "https://api.ujuzy.com/";
    }

    public static final class DATABASE
    {
        public static final String DB_NAME = "services";
        public static final int DB_VERSION = 1;
        public static final String TABLE_NAME = "service";

        public static final String ID = "id";
        public static final String SERVICE_NAME = "service_name";
        public static final String SERVICE_DETAILS = "service_details";
        public static final String PHOTO = "photo";
        public static final String PHOTO_URL = "photo_url";

        public static final String GET_SERVICE_QUERY = "SELECT * FROM " + TABLE_NAME;

        public static final String DROP_QUERY = "DROP TABLE IF EXISTS " + TABLE_NAME;
        public static final String CREATE_TABLE_QUERY = "CREATE TABLE " + TABLE_NAME + "" +

                "("+ ID + " PRIMARY KEY," +
                SERVICE_NAME + " INTEGER not null,"+
                SERVICE_DETAILS + " TEXT not null,"+
                PHOTO_URL + " TEXT not null,"+
                PHOTO + " blob not null)";

    }
}