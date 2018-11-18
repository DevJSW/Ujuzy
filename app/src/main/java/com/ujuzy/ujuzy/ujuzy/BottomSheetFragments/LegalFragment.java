package com.ujuzy.ujuzy.ujuzy.BottomSheetFragments;


import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
public class LegalFragment extends BottomSheetDialogFragment {

    View v;
    private TextView toolbarTv;
    private ImageView toolBack;

    public LegalFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_legal, container, false);

        init();
        initBack();
        initToolBarTv();
        return v;
    }

    private void initBack()
    {
        toolBack.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                getFragmentManager().popBackStackImmediate();
            }
        });
    }

    private void initToolBarTv()
    {
        toolbarTv.setText("Legal");
    }

    private void init()
    {
        toolbarTv = (TextView) v.findViewById(R.id.toolbarTv);
        toolBack = (ImageView) v.findViewById(R.id.backBtn);
    }

}
