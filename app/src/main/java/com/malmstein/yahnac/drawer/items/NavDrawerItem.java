package com.malmstein.yahnac.drawer.items;

public interface NavDrawerItem {

    ItemType getType();

    public enum ItemType {
        HEADER,
        SPACE,
        SECTION_HEADER,
        CATEGORY,
        FOOTER
    }

}
