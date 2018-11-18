package com.ujuzy.ujuzy.ujuzy.BottomSheetFragments;


import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ujuzy.ujuzy.ujuzy.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MessegesFragment extends Fragment {

    View v;
    private TextView toolbarTv;

    public MessegesFragment() {
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
        v = inflater.inflate(R.layout.fragment_messages, container, false);

        init();
        initToolBarTv();
        return v;
    }

    private void initToolBarTv()
    {
        toolbarTv.setText("Messages");
    }

    private void init()
    {
        toolbarTv = (TextView) v.findViewById(R.id.toolbarTv);
    }

}
