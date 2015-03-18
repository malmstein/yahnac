package com.malmstein.yahnac.views.fonts;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class YahnacTextView extends TextView {

    private final TypefaceFactory typeFaceFactory;

    public YahnacTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.typeFaceFactory = new TypefaceFactory();
        initTypeface(context, attrs);
    }

    public YahnacTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.typeFaceFactory = new TypefaceFactory();
        initTypeface(context, attrs);
    }

    private void initTypeface(Context context, AttributeSet attrs) {
        if (isInEditMode()) {
            return;
        }

        Typeface typeface = typeFaceFactory.createFrom(context, attrs);
        setTypeface(typeface);
    }

}
