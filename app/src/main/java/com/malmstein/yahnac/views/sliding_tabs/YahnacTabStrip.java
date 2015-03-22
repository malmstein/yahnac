package com.malmstein.yahnac.views.sliding_tabs;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import com.astuetz.PagerSlidingTabStrip;
import com.malmstein.yahnac.views.fonts.TypefaceFactory;
import com.novoda.notils.exception.DeveloperError;

public class YahnacTabStrip extends PagerSlidingTabStrip {

    private final TypefaceFactory typeFaceFactory;

    public YahnacTabStrip(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public YahnacTabStrip(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.typeFaceFactory = new TypefaceFactory();
        initTypeface(context, attrs);
    }

    private void initTypeface(Context context, AttributeSet attrs) {
        if (isInEditMode()) {
            return;
        }

        Typeface typeface = typeFaceFactory.createFrom(context, attrs);
        super.setTypeface(typeface, Typeface.NORMAL);
    }

    @Override
    public void setTypeface(Typeface typeface, int style) {
        throw new DeveloperError("Sorry buddy, only Chuck Norris can change the TypeFace in RobotoMediumTabStrip.");
    }

}
