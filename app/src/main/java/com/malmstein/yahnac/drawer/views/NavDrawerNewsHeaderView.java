package com.malmstein.yahnac.drawer.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.malmstein.yahnac.R;
import com.malmstein.yahnac.drawer.NavDrawerListener;
import com.malmstein.yahnac.drawer.items.NavDrawerItem;
import com.malmstein.yahnac.drawer.items.NavDrawerItemViewHolder;

public class NavDrawerNewsHeaderView extends FrameLayout implements NavDrawerItemViewHolder {

    public NavDrawerNewsHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NavDrawerNewsHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        LayoutInflater.from(getContext()).inflate(R.layout.merge_news_header_view, this, true);
    }

    @Override
    public View asView() {
        return this;
    }

    @Override
    public void bind(NavDrawerItem item, NavDrawerListener listener) {
        // no op
    }

}
