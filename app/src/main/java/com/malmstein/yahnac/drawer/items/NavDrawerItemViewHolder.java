package com.malmstein.yahnac.drawer.items;

import android.view.View;

import com.malmstein.yahnac.drawer.NavDrawerListener;

public interface NavDrawerItemViewHolder {

    View asView();

    void bind(NavDrawerItem item, NavDrawerListener listener);

}
