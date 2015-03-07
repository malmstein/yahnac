package com.malmstein.yahnac.model;

import android.database.Cursor;

import com.malmstein.yahnac.data.HNewsContract;

public class Comment {

    private final Long internalId;
    private final String by;
    private final Long id;
    private final int level;
    private final String text;
    private final String timeText;
    private final boolean isHeader;

    public Comment(Long internalId, String by, Long id, int level, String text, String timeText, boolean isHeader) {
        this.internalId = internalId;
        this.by = by;
        this.id = id;
        this.level = level;
        this.text = text;
        this.timeText = timeText;
        this.isHeader = isHeader;
    }

    public String getText() {
        return text;
    }

    public int getLevel() {
        return level;
    }

    public static Comment from(Cursor cursor) {
        Long internalId = cursor.getLong(HNewsContract.COLUMN_ID);
        Long id = cursor.getLong(HNewsContract.COLUMN_ITEM_ID);
        String by = cursor.getString(HNewsContract.COLUMN_BY);

        int level = cursor.getInt(HNewsContract.COLUMN_LEVEL);
        String text = cursor.getString(HNewsContract.COLUMN_TEXT);
        String timeText = cursor.getString(HNewsContract.COLUMN_TIME_TEXT);
        boolean isHeader = cursor.getInt(HNewsContract.COLUMN_HEADER) == HNewsContract.TRUE_BOOLEAN;

        return new Comment(internalId, by, id, level, text, timeText, isHeader);
    }

    public String getTimeText() {
        return timeText;
    }

    public Long getInternalId() {
        return internalId;
    }

    public String getBy() {
        return by;
    }

    public Long getId() {
        return id;
    }

    public boolean isHeader() {
        return isHeader;
    }
}
