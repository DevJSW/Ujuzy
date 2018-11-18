package com.ujuzy.ujuzy.ujuzy.model;

import com.android.volley.RequestQueue;
import com.ujuzy.ujuzy.ujuzy.Realm.RealmToken;
import com.ujuzy.ujuzy.ujuzy.Realm.RealmUser;

import io.realm.Realm;

public class Constants
{

    public static final class HTTP
    {
        public static final String BASE_URL = "https://api.ujuzy.com/";
        public static final String ADS_BASE_URL = "https://ads.ujuzy.com/";
        public static final String AUTH_BASE_URL = "https://sso.ujuzy.com/";
        public static final String SERVICES_ENDPOINT = BASE_URL + "services?page=";
        public static final String CREATE_SERVICE_JSON_URL = BASE_URL + "services";
        public static final String REQUEST_SERVICE_JSON_URL = BASE_URL + "requests/services";
        public static final String UPGRADE_PROFILE_JSON_URL = BASE_URL + "users/change-role";
        public static final String EDIT_ACCOUNT_JSON_URL = BASE_URL + "users";
        public static final String USER_SERVICES_JSON_URL = BASE_URL + "users/my-services";
        public static final String USER_REQUESTS_SERVICES_JSON_URL = BASE_URL + "requests";
        public static final String USER_JOB_REQUESTS_SERVICES_JSON_URL = BASE_URL + "requests/my-jobs";
        public static final String USER_PROFILE_JSON_URL = BASE_URL + "users/profile";
        public static final String USER_LOGIN_JSON_URL = BASE_URL + "auth/login";
        public static final String SEARCH_ENDPOINT = BASE_URL + "services/search?q=";
        public static final String REVIEW_SERVICE_JSON_URL = BASE_URL + "reviews";
        public static final String ADS_URL = ADS_BASE_URL + "api/target/all";
    }

    public static final class HEADERS
    {
        public static final String CONTENT_TYPE = "application/json charset=utf-8";
        public static final String ACCEPT = "application/json";
    }

    public static final class Volley
    {
        public RequestQueue requestQueue;
    }

}