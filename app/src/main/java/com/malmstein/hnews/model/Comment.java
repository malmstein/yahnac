package com.malmstein.hnews.model;

import android.database.Cursor;

import com.malmstein.hnews.data.HNewsContract;

public class Comment extends Item {

    private final int level;
    private final String text;
    public Comment(Long internalId, String by, Long id, Long time, int level, String text) {
        super(internalId, by, id, time);
        this.level = level;
        this.text = text;
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

        return new Comment(internalId, by, id, time, level, text);
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