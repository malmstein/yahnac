package com.malmstein.hnews.data;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class HNewsContract {

    public static final String CONTENT_AUTHORITY = "com.malmstein.hnews";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_ITEM = "item";

    public static final String[] STORY_COLUMNS = {
            ItemEntry._ID,
            ItemEntry.COLUMN_ITEM_ID,
            ItemEntry.COLUMN_BY,
            ItemEntry.COLUMN_TIME,
            ItemEntry.COLUMN_INSERTED,
            ItemEntry.COLUMN_TYPE,
            ItemEntry.COLUMN_SCORE,
            ItemEntry.COLUMN_TITLE,
            ItemEntry.COLUMN_URL,
            ItemEntry.COLUMN_KIDS,
    };

    public static final String[] COMMENT_COLUMNS = {
            ItemEntry._ID,
            ItemEntry.COLUMN_ITEM_ID,
            ItemEntry.COLUMN_BY,
            ItemEntry.COLUMN_TIME,
            ItemEntry.COLUMN_INSERTED,
            ItemEntry.COLUMN_PARENT,
            ItemEntry.COLUMN_TEXT,
            ItemEntry.COLUMN_KIDS,
    };

    public static final int COLUMN_ID = 0;
    public static final int COLUMN_ITEM_ID = 1;
    public static final int COLUMN_BY = 2;
    public static final int COLUMN_TIME = 3;
    public static final int COLUMN_UPDATED = 4;

    public static final int COLUMN_TYPE = 5;
    public static final int COLUMN_SCORE = 6;
    public static final int COLUMN_TITLE = 7;
    public static final int COLUMN_URL = 8;
    public static final int COLUMN_KIDS = 9;

    public static final int COLUMN_PARENT = 5;
    public static final int COLUMN_TEXT = 6;


    /* Inner class that defines the table contents of the location table */
    public static final class ItemEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_ITEM).build();

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_ITEM;
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_ITEM;

        public static final String TABLE_NAME = "item";

        public static final String COLUMN_ITEM_ID = "item_id";
        public static final String COLUMN_DELETED = "deleted";
        public static final String COLUMN_TYPE = "type";
        public static final String COLUMN_BY = "by";
        public static final String COLUMN_TIME = "time";
        public static final String COLUMN_TEXT = "text";
        public static final String COLUMN_DEAD = "dead";
        public static final String COLUMN_PARENT = "parent";
        public static final String COLUMN_KIDS = "kids";
        public static final String COLUMN_URL = "url";
        public static final String COLUMN_SCORE = "score";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_PARTS = "parts";
        public static final String COLUMN_INSERTED = "timestamp";

        public static Uri buildItemUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildItemsUri() {
            return CONTENT_URI.buildUpon().build();
//            return CONTENT_URI.buildUpon().appendQueryParameter(COLUMN_TYPE, itemType.name()).build();
        }

    }

}
