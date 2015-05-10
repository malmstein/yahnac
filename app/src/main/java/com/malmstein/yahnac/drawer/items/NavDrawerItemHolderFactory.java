package com.malmstein.yahnac.drawer.items;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.malmstein.yahnac.R;
import com.novoda.notils.exception.DeveloperError;

public class NavDrawerItemHolderFactory {

    protected final LayoutInflater layoutInflater;

    public NavDrawerItemHolderFactory(LayoutInflater layoutInflater) {
        this.layoutInflater = layoutInflater;
    }

    public NavDrawerItemViewHolder create(NavDrawerItem item, ViewGroup parent) {
        NavDrawerItem.ItemType type = item.getType();
        switch (type) {
            case HEADER:
                return createHeaderViewHolder(parent);
            case SPACE:
                return createSpaceViewHolder(parent);
            case SECTION_HEADER:
                return createSectionHeaderViewHolder(parent);
            case CATEGORY:
                return createCategoryViewHolder(parent);
            default:
                throw new DeveloperError(String.format("Item of type '%s' is not supported in HoloNavDrawerItemHolderFactory", type));
        }
    }

    protected NavDrawerItemViewHolder createHeaderViewHolder(ViewGroup parent) {
        return (NavDrawerItemViewHolder) layoutInflater.inflate(R.layout.view_navdrawer_material_news_header, parent, false);
    }

    protected NavDrawerItemViewHolder createSpaceViewHolder(ViewGroup parent) {
        return (NavDrawerItemViewHolder) layoutInflater.inflate(R.layout.view_navdrawer_material_space, parent, false);
    }

    private NavDrawerItemViewHolder createSectionHeaderViewHolder(ViewGroup parent) {
        return (NavDrawerItemViewHolder) layoutInflater.inflate(R.layout.view_navdrawer_material_section_header, parent, false);
    }

    private NavDrawerItemViewHolder createCategoryViewHolder(ViewGroup parent) {
        return (NavDrawerItemViewHolder) layoutInflater.inflate(R.layout.view_navdrawer_material_category_item, parent, false);
    }
}
