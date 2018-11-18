package com.ujuzy.ujuzy.ujuzy.BottomSheetFragments;


import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ujuzy.ujuzy.ujuzy.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class RequestFragment extends BottomSheetDialogFragment {

    View v;
    private TextView toolbarTv;
    private ImageView toolback;

    public RequestFragment() {
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
        v = inflater.inflate(R.layout.fragment_request, container, false);

        init();
        initBack();
        initToolBarTv();
        return v;
    }

    private void initBack()
    {
        toolback.setOnClickListener(new View.OnClickListener()
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
        toolbarTv.setText("Request");
    }

    private void init()
    {
        toolbarTv = (TextView) v.findViewById(R.id.toolbarTv);
        toolback = (ImageView) v.findViewById(R.id.backBtn);
    }

}
