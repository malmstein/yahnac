package com.malmstein.hnews.views;

import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;

public class InsetColorDrawable extends ColorDrawable {

    private final Rect insets;

    public InsetColorDrawable(int color, Rect insets) {
        super(color);
        this.insets = insets;
    }

    @Override
    public void setBounds(int left, int top, int right, int bottom) {
        super.setBounds(left - insets.left,
                top - insets.top,
                right - insets.right,
                bottom - insets.bottom);
    }

}
