package com.ujuzy.ujuzy.activities;

import android.accessibilityservice.GestureDescription;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class TokenRequest {
    @VisibleForTesting
    static final String KEY_CLIENT_ID = "clientId";
    @VisibleForTesting
    static final String KEY_NONCE = "nonce";
    @VisibleForTesting
    static final String KEY_GRANT_TYPE = "grantType";
    @VisibleForTesting
    static final String KEY_REDIRECT_URI = "redirectUri";
    /**
     * The (optional) nonce associated with the current session.
     */

    /**
     * The client identifier.
     *
     @@ -214,6 +222,9 @@
     @NonNull private String mClientId;
     @Nullable private String mNonce;
     @Nullable private String mGrantType;
     @@ -265,6 +276,19 @@ public Builder setClientId(@NonNull String clientId) {
     return this;
     }
     /**
      * Specifies the (optional) nonce for the current session.
     */

}
