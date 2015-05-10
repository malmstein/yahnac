package com.malmstein.yahnac.drawer.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.malmstein.yahnac.drawer.NavDrawerListener;
import com.malmstein.yahnac.drawer.items.NavDrawerItem;
import com.malmstein.yahnac.drawer.items.NavDrawerItemViewHolder;

public class NavDrawerHeaderMarginBottomView extends View implements NavDrawerItemViewHolder {

    public NavDrawerHeaderMarginBottomView(Context context) {
        super(context);
    }

    public NavDrawerHeaderMarginBottomView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NavDrawerHeaderMarginBottomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public View asView() {
        return this;
    }

    @Override
    public void bind(NavDrawerItem item, NavDrawerListener listener) {
        // No-op (hey, I'm just a spacer!)
    }

}
