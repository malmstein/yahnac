package com.malmstein.yahnac.drawer.items;

import java.util.ArrayList;
import java.util.List;

public class NavDrawerItemsFactory {

    private static NavDrawerItem createHeaderItem() {
        return new NewsHeaderItem();
    }

    private static NavDrawerItem createEmptySectionHeaderItem() {
        return new SectionHeaderItem();
    }

    private static NavDrawerItem createSettingsFooterItem() {
        return new SettingsFooterItem();
    }

    private static NavDrawerItem createNewsItem() {
        return new NewsItem();
    }

    public NavDrawerItems createDrawerItems() {
        List<NavDrawerItem> items = new ArrayList<NavDrawerItem>();
        items.add(createHeaderItem());
        items.add(createHeaderMarginBottomItem());
        items.add(createNewsItem());
        items.add(createEmptySectionHeaderItem());
        items.add(createSettingsFooterItem());
        return new NavDrawerItems(items);
    }

    private NavDrawerItem createHeaderMarginBottomItem() {
        return new SpaceItem();
    }

}
