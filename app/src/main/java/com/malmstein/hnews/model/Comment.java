package com.malmstein.hnews.model;

import android.database.Cursor;

import com.malmstein.hnews.data.HNewsContract;

public class Comment {

    private final Long internalId;
    private final String by;
    private final Long id;
    private final Long time;
    private final int level;
    private final String text;
    private final String timeText;

    public Comment(Long internalId, String by, Long id, Long time, int level, String text, String timeText) {
        this.internalId = internalId;
        this.by = by;
        this.id = id;
        this.time = time;
        this.level = level;
        this.text = text;
        this.timeText = timeText;
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
        Long time = cursor.getLong(HNewsContract.COLUMN_TIME);

        int level = cursor.getInt(HNewsContract.COLUMN_LEVEL);
        String text = cursor.getString(HNewsContract.COLUMN_TEXT);
        String timeText = cursor.getString(HNewsContract.COLUMN_TIME_TEXT);

        return new Comment(internalId, by, id, time, level, text, timeText);
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

    public Long getTime() {
        return time;
    }
}

//{
//        "by":"norvig",
//        "id":2921983,
//        "kids":[2922097,2922429,2924562,2922709,2922573,2922140,2922141],
//        "parent":2921506,
//        "text":"Aw shucks, guys ... you make me blush with your compliments.<p>Tell you what, Ill make a deal: I'll keep writing if you keep reading. K?",
//        "time":1314211127,
//        "type":"comment"
//        }