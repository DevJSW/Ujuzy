package com.ujuzy.ujuzy.ujuzy.Activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ujuzy.ujuzy.ujuzy.R;
import com.ujuzy.ujuzy.ujuzy.Services.PrefManager;

public class UpgradeActivity extends AppCompatActivity
{

    private ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
    private LinearLayout dotsLayout;
    private TextView[] dots;
    private int[] layouts;
    private Button btnUpgrade;
    private PrefManager prefManager;
    private TextView toolbarTv;
    private ImageView toolBack;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upgrade);

        init();
        initWindow();
        initSliderLayouts();
        initBack();
        initToolBarTv();
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
        toolbarTv.setText("Choose your plan");
    }

    private void initSliderLayouts()
    {
        layouts = new int[]
                {
                    R.layout.upgrade_slider_1,
                    R.layout.upgrade_slider_2,
                    R.layout.upgrade_slider_3
                };

        //adding bottom dots
        //addBottomDots(0);
    }

//    private void addBottomDots(int currentPage)
//    {
//        dots = new TextView[layouts.length];
//
//        int[] colorsActive = getResources().getIntArray(R.array_dot_active);
//        int[] colorsInActive = getResources().getIntArray(R.array_dot_inactive);
//
//        dotsLayout.removeAllViews();
//        for (int i = 0; i < dots.length; i++)
//        {
//            dots[i] = new TextView(this);
//            dots[i].setText(Html.fromHtml("&#8226;"));
//            dots[i].setTextSize(33);
//            dots[i].setTextColor(colorsInActive[currentPage]);
//            dotsLayout.addView(dots[i]);
//        }
//
//        if (dots.length > 0)
//            dots[currentPage].setTextColor(colorsActive[currentPage]);
//    }

    private void initWindow()
    {
        //Making notification bar transparent
        if (Build.VERSION.SDK_INT > 21)
        {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_FULLSCREEN);
        }

        //CHECKING FOR FIRST TIME LAUNCH - BEFORE CALLING SETCONTINENTVIEW
        prefManager = new PrefManager(this);

        Window window = UpgradeActivity.this.getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            window.setStatusBarColor(ContextCompat.getColor(UpgradeActivity.this, R.color.white));

            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        }
    }

    private void init()
    {
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        dotsLayout = (LinearLayout) findViewById(R.id.layoutDots);
        btnUpgrade = (Button) findViewById(R.id.btnUpgrade);
        toolbarTv = (TextView) findViewById(R.id.toolbarTv);
        toolBack = (ImageView) findViewById(R.id.backBtn);
    }

    private void addBottomDots(int currentPage)
    {
        dots = new TextView[layouts.length];

        int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
        int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++)
        {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(33);
            dots[i].setTextColor(colorsInactive[currentPage]);
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(colorsActive[currentPage]);
    }

    /**
     * View pager adapter
     */
    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position)
        {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(layouts[position], container, false);
            container.addView(view);
            return view;
        }

        @Override
        public int getCount()
        {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj)
        {

            return view == obj;

        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object)
        {
            View view = (View) object;
            container.removeView(view);
        }
    }

}
