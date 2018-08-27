package com.ujuzy.ujuzy.Tabs;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ujuzy.ujuzy.R;
import com.ujuzy.ujuzy.Realm.RealmHelper;
import com.ujuzy.ujuzy.Realm.RealmUser;
import com.ujuzy.ujuzy.Realm.RealmUserHelper;
import com.ujuzy.ujuzy.Realm.RealmUserService;
import com.ujuzy.ujuzy.activities.EditProfileActivity;
import com.ujuzy.ujuzy.model.Constants;

import org.jboss.aerogear.android.authorization.AuthorizationManager;
import org.jboss.aerogear.android.authorization.AuthzModule;
import org.jboss.aerogear.android.authorization.oauth2.OAuth2AuthorizationConfiguration;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * A simple {@link Fragment} subclass.
 */
public class AboutUserFragment extends Fragment {

    String token = "";
    private TextView userName, userEmail, userPhone, editTv, logOut, createdAt, userRole;
    private ImageView profileImage;
    private Realm realm;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        final View v = inflater.inflate(R.layout.fragment_about_user, container, false);

        editTv = (TextView) v.findViewById(R.id.tvEdit);
        createdAt = (TextView) v.findViewById(R.id.tvCreated);
        userName = (TextView) v.findViewById(R.id.tvName);
        userEmail = (TextView) v.findViewById(R.id.tvEmail);
        userPhone = (TextView) v.findViewById(R.id.tvPhone);
        logOut = (TextView) v.findViewById(R.id.tvLogOut);

        initEdit();
        initUserInfo();
        initLogOut();

        return v;

    }

    private void initLogOut() {

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*try {
                    final AuthzModule authzModule = AuthorizationManager
                            .config("KeyCloakAuthz", OAuth2AuthorizationConfiguration.class)
                            .setBaseURL(new URL(Constants.HTTP.AUTH_BASE_URL))
                            .setAuthzEndpoint("/auth/realms/ujuzy/protocol/openid-connect/auth")
                            .setAccessTokenEndpoint("/auth/realms/ujuzy/protocol/openid-connect/token")
                            .setAccountId("account")
                            .setClientId("account")
                            .setRedirectURL("https://ujuzy.com")
                            .addAdditionalAuthorizationParam((Pair.create("grant_type", "password")))
                            .asModule();

                    authzModule.deleteAccount();
                    getActivity().finish();

                    Toast.makeText(getActivity(), "You've logged out", Toast.LENGTH_LONG).show();

                } catch (MalformedURLException e) {
                    //e.printStackTrace();
                }*/
            }
        });
    }

    private void initUserInfo()
    {
        realm = Realm.getDefaultInstance();
            RealmUser realmUser = realm.where(RealmUser.class).findFirst();
            if (realmUser != null) {
                if (realmUser.getFirstname() != null)
                    userName.setText(realmUser.getFirstname() + " " + realmUser.getLastname());
                if (realmUser.getEmail() != null)
                    userEmail.setText(realmUser.getEmail());
                if (realmUser.getPhone() != null)
                    userPhone.setText(realmUser.getPhone());
                if (realmUser.getCreated_at() != null)
                    createdAt.setText(realmUser.getCreated_at());

            }

    }

    private void initEdit() {
        editTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), EditProfileActivity.class));
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (realm != null)
            realm.close();
    }
}
