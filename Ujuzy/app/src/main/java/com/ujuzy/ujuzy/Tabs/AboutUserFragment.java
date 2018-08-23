package com.ujuzy.ujuzy.Tabs;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ujuzy.ujuzy.R;
import com.ujuzy.ujuzy.Realm.RealmHelper;
import com.ujuzy.ujuzy.Realm.RealmUser;
import com.ujuzy.ujuzy.Realm.RealmUserHelper;
import com.ujuzy.ujuzy.Realm.RealmUserService;
import com.ujuzy.ujuzy.activities.EditProfileActivity;

import io.realm.Realm;

/**
 * A simple {@link Fragment} subclass.
 */
public class AboutUserFragment extends Fragment {

    private TextView userName, userEmail, userPhone, editTv;
    private Realm realm;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        final View v = inflater.inflate(R.layout.fragment_about_user, container, false);

        editTv = (TextView) v.findViewById(R.id.tvEdit);
        userName = (TextView) v.findViewById(R.id.tvName);
        userEmail = (TextView) v.findViewById(R.id.tvEmail);
        userPhone = (TextView) v.findViewById(R.id.tvPhone);

        initEdit();
        initUserInfo();

        return v;

    }

    private void initUserInfo()
    {
        realm = Realm.getDefaultInstance();
        final RealmUserHelper helper = new RealmUserHelper(realm);

        if (helper != null) {

            RealmUser realmUser = realm.where(RealmUser.class).findFirst();
            if (realmUser != null) {
                if (realmUser.getFirstname() != null)
                    userName.setText(realmUser.getFirstname() + " " + realmUser.getLastname());
                if (realmUser.getEmail() != null)
                    userEmail.setText(realmUser.getEmail());
                if (realmUser.getPhone() != null)
                    userPhone.setText(realmUser.getPhone());
            }
        } else {
            //
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

}
