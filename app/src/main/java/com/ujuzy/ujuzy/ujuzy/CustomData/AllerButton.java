package com.ujuzy.ujuzy.ujuzy.CustomData;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Created by John on 29-May-17.
 */
public class AllerButton extends Button {


    public AllerButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public AllerButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AllerButton(Context context) {
        super(context);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Aller_Rg.ttf");
            setTypeface(tf);
        }
    }

}

