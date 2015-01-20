package com.malmstein.hnews.data;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class HNewsContract {

    public static final String CONTENT_AUTHORITY = "com.malmstein.hnews";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_ITEM = "item";
    public static final String PATH_ITEM_TMP = "item_tmp";
    public static final String PATH_COMMENT = "comment";

    public static final String[] STORY_COLUMNS = {
            ItemEntry._ID,
            ItemEntry.COLUMN_ITEM_ID,
            ItemEntry.COLUMN_TYPE,
            ItemEntry.COLUMN_BY,
            ItemEntry.COLUMN_COMMENTS,
            ItemEntry.COLUMN_DOMAIN,
            ItemEntry.COLUMN_URL,
            ItemEntry.COLUMN_SCORE,
            ItemEntry.COLUMN_TITLE,
            ItemEntry.COLUMN_TIME,
            ItemEntry.COLUMN_ITEM_ORDER,
    };

    public static final String[] COMMENT_COLUMNS = {
            ItemEntry._ID,
            ItemEntry.COLUMN_ITEM_ID,
            ItemEntry.COLUMN_BY,
            ItemEntry.COLUMN_TIME,
            ItemEntry.COLUMN_TEXT,
            ItemEntry.COLUMN_LEVEL,
            ItemEntry.COLUMN_TIME_TEXT,
    };

    public static final int COLUMN_ID = 0;
    public static final int COLUMN_ITEM_ID = 1;
    public static final int COLUMN_TYPE = 2;
    public static final int COLUMN_BY = 3;
    public static final int COLUMN_COMMENTS = 4;
    public static final int COLUMN_DOMAIN = 5;
    public static final int COLUMN_URL = 6;
    public static final int COLUMN_SCORE = 7;
    public static final int COLUMN_TITLE = 8;
    public static final int COLUMN_TIME = 9;
    public static final int COLUMN_ITEM_ORDER = 10;
    public static final int COLUMN_UPVOTE_URL = 11;

    public static final int COLUMN_TEXT = 4;
    public static final int COLUMN_LEVEL = 5;
    public static final int COLUMN_TIME_TEXT = 6;

    public static final class ItemEntry implements BaseColumns {

        public static final Uri CONTENT_STORY_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_ITEM).build();
        public static final Uri CONTENT_STORY_TMP_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_ITEM_TMP).build();
        public static final Uri CONTENT_COMMENTS_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_COMMENT).build();

        public static final String CONTENT_STORY_TYPE = "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_ITEM;
        public static final String CONTENT_STORY_TMP_TYPE = "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_ITEM_TMP;
        public static final String CONTENT_COMMENT_TYPE = "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_COMMENT;
        public static final String CONTENT_STORY_ITEM_TYPE = "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_ITEM;

        public static final String TABLE_ITEM_NAME = "item";
        public static final String TABLE_ITEM_TMP_NAME = "item_tmp";
        public static final String TABLE_COMMENTS_NAME = "comment";

        public static final String COLUMN_ITEM_ID = "item_id";
        public static final String COLUMN_TYPE = "type";
        public static final String COLUMN_BY = "by";
        public static final String COLUMN_TIME = "time";
        public static final String COLUMN_ITEM_ORDER = "item_order";
        public static final String COLUMN_TEXT = "text";
        public static final String COLUMN_COMMENTS = "comments";
        public static final String COLUMN_URL = "url";
        public static final String COLUMN_SCORE = "score";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_LEVEL = "level";
        public static final String COLUMN_DOMAIN = "domain";
        public static final String COLUMN_TIME_TEXT = "time_text";
        public static final String COLUMN_UPVOTE_URL = "upvote_url";

        public static Uri buildStoryUriWith(long id) {
            return ContentUris.withAppendedId(CONTENT_STORY_URI, id);
        }

        public static Uri buildStoriesUri() {
            return CONTENT_STORY_TMP_URI.buildUpon().build();
        }

        public static Uri buildCommentUriWith(long id) {
            return ContentUris.withAppendedId(CONTENT_COMMENTS_URI, id);
        }

        public static Uri buildCommentsUri() {
            return CONTENT_COMMENTS_URI.buildUpon().build();
        }
    }

}
