package com.ujuzy.ujuzy.CustomData;

import android.content.Context;
import android.graphics.Typeface;
import android.support.design.widget.TabLayout;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ujuzy.ujuzy.R;

/**
 * Created by John on 29-May-17.
 */
public class CustomTablayout2 extends TabLayout {
    private Typeface mTypeface;

    public CustomTablayout2(Context context) {
        super(context);
        init();
    }

    public CustomTablayout2(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomTablayout2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mTypeface  = Typeface.createFromAsset(getContext().getAssets(), "fonts/Aller_Rg.ttf"); // here you will provide fully qualified path for fonts
    }

    @Override
    public void addTab(Tab tab) {
        super.addTab(tab);

        ViewGroup mainView = (ViewGroup) getChildAt(0);
        ViewGroup tabView = (ViewGroup) mainView.getChildAt(tab.getPosition());

        int tabChildCount = tabView.getChildCount();
        for (int i = 0; i < tabChildCount; i++) {
            View tabViewChild = tabView.getChildAt(i);
            if (tabViewChild instanceof TextView) {
                ((TextView) tabViewChild).setTypeface(mTypeface, Typeface.NORMAL);
                ((TextView) tabViewChild).setTextColor(getResources().getColor(R.color.font_black));
                ((TextView) tabViewChild).setTextSize(10);
            }
        }
    }


}