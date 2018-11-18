package com.ujuzy.ujuzy.ujuzy.BottomSheetFragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.ujuzy.ujuzy.ujuzy.Authorization.LoginActivity;
import com.ujuzy.ujuzy.ujuzy.R;
import com.ujuzy.ujuzy.ujuzy.Realm.RealmFavouriteHelper;
import com.ujuzy.ujuzy.ujuzy.Realm.RealmFavouritesAdapter;
import com.ujuzy.ujuzy.ujuzy.Realm.RealmServiceAdapter;
import com.ujuzy.ujuzy.ujuzy.Realm.RealmServiceImage;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmList;

import static io.realm.internal.SyncObjectServerFacade.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragments extends Fragment {

    private TextView inviteFriend, legal, openEdit, openLogIn, openLegal, openCreateService;
    View v;

    public SettingsFragments() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_settings, container, false);

        init();
        initInviteFriend();
        initLogIn();
        initOpenEdit();
        initCreateService();
        initLegal();
        return v;
    }

    private void initCreateService()
    {
        openCreateService.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                ServiceDetailsFragment serviceDetailsFragment = new ServiceDetailsFragment();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame, serviceDetailsFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
    }

    private void initLegal()
    {
        openLegal.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                LegalFragment legalFragments = new LegalFragment();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_right);
                fragmentTransaction.replace(R.id.frame, legalFragments);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
    }

    private void initLogIn()
    {
        openLogIn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(getActivity(), LoginActivity.class));
            }
        });
    }

    private void initOpenEdit()
    {
        openEdit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                ProfileFragments profileFragments = new ProfileFragments();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_right);
                fragmentTransaction.replace(R.id.frame, profileFragments);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });
    }

    private void initInviteFriend()
    {
        inviteFriend.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent myIntent = new Intent(Intent.ACTION_SEND);
                myIntent.setType("text/plain");
                String shareBody ="Ujuzy App";
                String shareSub = "Hi, this is an invitation to download Ujuzy app  https://play.google.com/store/apps/details?id=com.ujuzy.www";
                myIntent.putExtra(Intent.EXTRA_SUBJECT,shareBody);
                myIntent.putExtra(Intent.EXTRA_TEXT,shareSub);
                startActivity(Intent.createChooser(myIntent,"Invite a friend"));
            }
        });
    }

    private void init()
    {

        inviteFriend = (TextView) v.findViewById(R.id.inviteFriend);
        legal = (TextView) v.findViewById(R.id.legal);
        openEdit = (TextView) v.findViewById(R.id.openEditProfile);
        openLogIn = (TextView) v.findViewById(R.id.openLogin);
        openLegal = (TextView) v.findViewById(R.id.legal);
        openCreateService = (TextView) v.findViewById(R.id.openCreateService);

    }

}
