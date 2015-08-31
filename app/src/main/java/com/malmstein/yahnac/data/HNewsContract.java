package com.malmstein.yahnac.data;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class HNewsContract {

    public static final String CONTENT_AUTHORITY = "com.malmstein.yahnac";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_ITEM = "item";
    public static final String CONTENT_STORY_TYPE = "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_ITEM;
    public static final String CONTENT_STORY_ITEM_TYPE = "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_ITEM;
    public static final String PATH_COMMENT = "comment";
    public static final String CONTENT_COMMENT_TYPE = "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_COMMENT;
    public static final String PATH_BOOKMARKS = "bookmarks";
    public static final String TABLE_ITEM_NAME = "item";
    public static final String TABLE_COMMENTS_NAME = "comment";
    public static final String TABLE_BOOKMARKS_NAME = "bookmarks";
    public static final int FALSE_BOOLEAN = 0;
    public static final int TRUE_BOOLEAN = 1;

    public static final class StoryEntry implements BaseColumns {

        public static final Uri CONTENT_STORY_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_ITEM).build();
        public static final String ITEM_ID = "item_id";
        public static final String TYPE = "type";
        public static final String BY = "by";
        public static final String COMMENTS = "comments";
        public static final String URL = "url";
        public static final String SCORE = "score";
        public static final String TITLE = "title";
        public static final String TIME_AGO = "time_ago";
        public static final String RANK = "rank";
        public static final String TIMESTAMP = "timestamp";
        public static final String BOOKMARK = "bookmark";
        public static final String READ = "read";
        public static final String VOTED = "voted";
        public static final String FILTER = "filter";

        public static final String[] STORY_COLUMNS = {
                StoryEntry._ID,
                StoryEntry.ITEM_ID,
                StoryEntry.TYPE,
                StoryEntry.BY,
                StoryEntry.COMMENTS,
                StoryEntry.URL,
                StoryEntry.SCORE,
                StoryEntry.TITLE,
                StoryEntry.TIME_AGO,
                StoryEntry.RANK,
                StoryEntry.TIMESTAMP,
                StoryEntry.BOOKMARK,
                StoryEntry.READ,
                StoryEntry.VOTED,
                StoryEntry.FILTER,
        };

        public static final int COLUMN_ID = 0;
        public static final int COLUMN_ITEM_ID = 1;
        public static final int COLUMN_TYPE = 2;
        public static final int COLUMN_BY = 3;
        public static final int COLUMN_COMMENTS = 4;
        public static final int COLUMN_URL = 5;
        public static final int COLUMN_SCORE = 6;
        public static final int COLUMN_TITLE = 7;
        public static final int COLUMN_TIME_AGO = 8;
        public static final int COLUMN_RANK = 9;
        public static final int COLUMN_TIMESTAMP = 10;
        public static final int COLUMN_BOOKMARK = 11;
        public static final int COLUMN_READ = 12;
        public static final int COLUMN_VOTED = 13;
        public static final int COLUMN_FILTER = 14;

        public static Uri buildStoryUriWith(long id) {
            return ContentUris.withAppendedId(CONTENT_STORY_URI, id);
        }

        public static Uri buildStoriesUri() {
            return CONTENT_STORY_URI.buildUpon().build();
        }

    }

    public static final class BookmarkEntry implements BaseColumns {

        public static final Uri CONTENT_BOOKMARKS_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_BOOKMARKS).build();
        public static final String ITEM_ID = "item_id";
        public static final String TYPE = "type";
        public static final String BY = "by";
        public static final String URL = "url";
        public static final String TITLE = "title";
        public static final String TIMESTAMP = "timestamp";
        public static final String FILTER = "filter";

        public static final String[] BOOKMARK_COLUMNS = {
                BookmarkEntry._ID,
                BookmarkEntry.ITEM_ID,
                BookmarkEntry.TYPE,
                BookmarkEntry.BY,
                BookmarkEntry.URL,
                BookmarkEntry.TITLE,
                BookmarkEntry.TIMESTAMP,
                BookmarkEntry.FILTER,
        };
        public static final int COLUMN_ID = 0;
        public static final int COLUMN_ITEM_ID = 1;
        public static final int COLUMN_TYPE = 2;
        public static final int COLUMN_BY = 3;
        public static final int COLUMN_URL = 4;
        public static final int COLUMN_TITLE = 5;
        public static final int COLUMN_TIMESTAMP = 6;
        public static final int COLUMN_FILTER = 7;

        public static Uri buildBookmarksUriWith(long id) {
            return ContentUris.withAppendedId(CONTENT_BOOKMARKS_URI, id);
        }

        public static Uri buildBookmarksUri() {
            return CONTENT_BOOKMARKS_URI.buildUpon().build();
        }

    }

    public static final class CommentsEntry implements BaseColumns {

        public static final Uri CONTENT_COMMENTS_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_COMMENT).build();

        public static final String ITEM_ID = "item_id";
        public static final String LEVEL = "level";
        public static final String BY = "by";
        public static final String TEXT = "text";
        public static final String TIME_AGO = "time_ago";
        public static final String HEADER = "header";
        public static final String COMMENT_ID = "comment_id";

        public static final String[] COMMENT_COLUMNS = {
                CommentsEntry._ID,
                CommentsEntry.ITEM_ID,
                CommentsEntry.LEVEL,
                CommentsEntry.BY,
                CommentsEntry.TEXT,
                CommentsEntry.TIME_AGO,
                CommentsEntry.HEADER,
                CommentsEntry.COMMENT_ID,
        };

        public static final int COLUMN_ID = 0;
        public static final int COLUMN_ITEM_ID = 1;
        public static final int COLUMN_LEVEL = 2;
        public static final int COLUMN_BY = 3;
        public static final int COLUMN_TEXT = 4;
        public static final int COLUMN_TIME_AGO = 5;
        public static final int COLUMN_HEADER = 6;
        public static final int COLUMN_COMMENT_ID = 7;

        public static Uri buildCommentUriWith(long id) {
            return ContentUris.withAppendedId(CONTENT_COMMENTS_URI, id);
        }

        public static Uri buildCommentsUri() {
            return CONTENT_COMMENTS_URI.buildUpon().build();
        }

    }

}
