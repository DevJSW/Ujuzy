package com.ujuzy.ujuzy.CustomData;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by John on 29-May-17.
 */
public class AllerBoldEditText extends EditText {


    public AllerBoldEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public AllerBoldEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AllerBoldEditText(Context context) {
        super(context);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Aller_Bd.ttf");
            setTypeface(tf);
        }
    }

}

