package com.malmstein.yahnac.drawer.items;

public class SectionHeaderItem implements NavDrawerItem {

    private static final int NO_TITLE_RES_ID = 0;
    private final int titleResourceId;

    public SectionHeaderItem() {
        this(NO_TITLE_RES_ID);
    }

    public SectionHeaderItem(int titleResourceId) {
        this.titleResourceId = titleResourceId;
    }

    @Override
    public ItemType getType() {
        return ItemType.SECTION_HEADER;
    }

    public int getTitleResourceId() {
        return titleResourceId;
    }

    public boolean hasTitle() {
        return titleResourceId != NO_TITLE_RES_ID;
    }

}
