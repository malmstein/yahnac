package com.malmstein.hnews.data;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class HNewsContract {

    public static final String CONTENT_AUTHORITY = "com.malmstein.hnews";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_ITEM = "item";

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

        public static Uri buildItemsUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

}
