package com.malmstein.yahnac.drawer.items;

import java.util.Collections;
import java.util.List;

public class NavDrawerItems {

    private final List<NavDrawerItem> items;

    public NavDrawerItems(List<NavDrawerItem> items) {
        this.items = Collections.unmodifiableList(items);
    }

    public NavDrawerItem getItem(int position) {
        return items.get(position);
    }

    public NavDrawerItem.ItemType getType(int position) {
        NavDrawerItem item = getItem(position);
        return item.getType();
    }

    public int size() {
        return items.size();
    }

}
