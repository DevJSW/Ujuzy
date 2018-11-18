package com.ujuzy.ujuzy.ujuzy.BottomSheetFragments;


import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.RequestQueue;
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
public class FavouritesFragments extends BottomSheetDialogFragment {

    private Realm realm;
    private RealmChangeListener realmChangeListener;
    private RealmServiceAdapter serviceReamAdapter;
    private RecyclerView favouritesListRv;
    private RequestQueue requestQueue;
    RealmList<RealmServiceImage> serviceImages;
    String category_title = "";
    private TextView noService;
    private RealmFavouritesAdapter serviceFavRealmAdapter;
    View v;

    public FavouritesFragments() {
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
        v = inflater.inflate(R.layout.fragment_fav, container, false);

        initFavourites();
        return v;
    }

    private void initFavourites() {
        realm = Realm.getDefaultInstance();
        final RealmFavouriteHelper helper = new RealmFavouriteHelper(realm);

        //QUERY/FILTER REALM DATABASE
        helper.retreiveFromDB();

        //CHECK IF DATABASE IS EMPTY
        if (helper.refreshDatabase().size() < 1 || helper.refreshDatabase().size() == 0) {
            noService = (TextView) v.findViewById(R.id.noFav);
            noService.setVisibility(View.VISIBLE);
            noService.setText("All your favourite services will be collected here");
        } else {
            noService = (TextView) v.findViewById(R.id.noService);
            noService.setVisibility(View.GONE);
        }


        favouritesListRv = (RecyclerView) v.findViewById(R.id.favourite_list);
        serviceFavRealmAdapter = new RealmFavouritesAdapter(getApplicationContext(), helper.refreshDatabase());

        //RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        final LinearLayoutManager serviceLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        favouritesListRv.setLayoutManager(serviceLayoutManager);
        favouritesListRv.setAdapter(serviceFavRealmAdapter);

        //HANDLE DATA CHANGE FOR REFRESH
        realmChangeListener = new RealmChangeListener() {
            @Override
            public void onChange(Object o) {
                //REFRESH
                serviceFavRealmAdapter = new RealmFavouritesAdapter(getApplicationContext(), helper.refreshDatabase());
                favouritesListRv.setAdapter(serviceFavRealmAdapter);
            }
        };

        //ADD CHANGE LIST TO REALM
        realm.addChangeListener(realmChangeListener);
    }


    @Override
    public void onDestroy()
    {
        super.onDestroy();
        realm = Realm.getDefaultInstance();
        if (realmChangeListener != null)
            realm.removeChangeListener(realmChangeListener);
    }
}
