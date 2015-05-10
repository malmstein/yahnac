package com.malmstein.yahnac.drawer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.malmstein.yahnac.drawer.items.NavDrawerItem;
import com.malmstein.yahnac.drawer.items.NavDrawerItemHolderFactory;
import com.malmstein.yahnac.drawer.items.NavDrawerItemViewHolder;
import com.malmstein.yahnac.drawer.items.NavDrawerItems;
import com.malmstein.yahnac.drawer.items.NavDrawerItemsFactory;

public class NavDrawerAdapter extends BaseAdapter {

    private final NavDrawerItemHolderFactory holderFactory;
    private final NavDrawerListener listener;
    private final NavDrawerItemsFactory itemsFactory;
    private NavDrawerItems items;

    NavDrawerAdapter(NavDrawerItemHolderFactory holderFactory, NavDrawerListener listener, NavDrawerItemsFactory itemsFactory) {
        this.holderFactory = holderFactory;
        this.listener = listener;
        this.itemsFactory = itemsFactory;
        this.items = populate();
    }

    @SuppressWarnings("ConstantConditions")
    public static NavDrawerAdapter newInstance(LayoutInflater layoutInflater, NavDrawerListener listener) {
        NavDrawerItemHolderFactory holderFactory = new NavDrawerItemHolderFactory(layoutInflater);
        NavDrawerItemsFactory itemFactory = new NavDrawerItemsFactory();
        return new NavDrawerAdapter(holderFactory, listener, itemFactory);
    }

    private NavDrawerItems populate() {
        return itemsFactory.createDrawerItems();
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public int getViewTypeCount() {
        return NavDrawerItem.ItemType.values().length;
    }

    @Override
    public int getItemViewType(int position) {
        return getItemType(position).ordinal();
    }

    private NavDrawerItem.ItemType getItemType(int position) {
        return items.getType(position);
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public NavDrawerItem getItem(int position) {
        return items.getItem(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        NavDrawerItem item = getItem(position);
        NavDrawerItemViewHolder holder;
        if (convertView instanceof NavDrawerItemViewHolder) {
            holder = (NavDrawerItemViewHolder) convertView;
        } else {
            holder = holderFactory.create(item, parent);
        }
        holder.bind(item, listener);
        return holder.asView();
    }

}
