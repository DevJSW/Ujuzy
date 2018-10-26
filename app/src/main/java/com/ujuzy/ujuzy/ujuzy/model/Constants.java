package com.ujuzy.ujuzy.ujuzy.model;

import com.android.volley.RequestQueue;

public class Constants
{
    public static final class HTTP
    {
        public static final String BASE_URL = "https://api.ujuzy.com/";
        public static final String AUTH_BASE_URL = "https://sso.ujuzy.com/";
        public static final String TOKEN_ENDPOINT = "https://sso.ujuzy.com/auth/realms/ujuzy/protocol/openid-connect/";
        public static final String REGISTRATION_ENDPOINT = "https://sso.ujuzy.com/auth/realms/ujuzy/clients-registrations/";
        public static final String SERVICES_ENDPOINT = "https://api.ujuzy.com/services";
        public static final String CREATE_SERVICE_JSON_URL = "https://api.ujuzy.com/services";
        public static final String SERVICES_SEARCH = "https://api.ujuzy.com/services/search";
        public static final String REQUEST_SERVICE_JSON_URL = "https://api.ujuzy.com/requests/services";
        public static final String UPGRADE_PROFILE_JSON_URL = "https://api.ujuzy.com/users/change-role";
        public static final String EDIT_ACCOUNT_JSON_URL = "https://api.ujuzy.com/users/edit";
        public static final String USER_SERVICES_JSON_URL = "https://api.ujuzy.com/users/my-services";
        public static final String USER_REQUESTS_SERVICES_JSON_URL = "https://api.ujuzy.com/requests";
        public static final String USER_JOB_REQUESTS_SERVICES_JSON_URL = "https://api.ujuzy.com/requests/my-jobs";
        public static final String USER_PROFILE_JSON_URL = "https://api.ujuzy.com/users/profile";
    }

    public static final class Volley
    {
        public RequestQueue requestQueue;
    }

}