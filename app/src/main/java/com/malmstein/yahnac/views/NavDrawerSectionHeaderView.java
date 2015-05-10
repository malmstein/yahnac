package com.malmstein.yahnac.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.malmstein.yahnac.drawer.NavDrawerListener;
import com.malmstein.yahnac.drawer.items.NavDrawerItem;
import com.malmstein.yahnac.drawer.items.NavDrawerItemViewHolder;
import com.malmstein.yahnac.drawer.items.SectionHeaderItem;
import com.malmstein.yahnac.views.fonts.YahnacTextView;

public class NavDrawerSectionHeaderView extends YahnacTextView implements NavDrawerItemViewHolder {

    private static final String NO_TITLE = "";

    public NavDrawerSectionHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NavDrawerSectionHeaderView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public View asView() {
        return this;
    }

    @Override
    public void bind(NavDrawerItem item, NavDrawerListener listener) {
        if (!(item instanceof SectionHeaderItem)) {
            throw new IllegalArgumentException(String.format("SectionHeaderItem expected - got '%s' instead.", item.getClass().getName()));
        }
        SectionHeaderItem sectionHeaderItem = (SectionHeaderItem) item;
        setText(getTitleFrom(sectionHeaderItem));
    }

    private String getTitleFrom(SectionHeaderItem item) {
        if (item.hasTitle()) {
            return getResources().getString(item.getTitleResourceId());
        }
        return NO_TITLE;
    }

}
