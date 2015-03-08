package com.malmstein.yahnac.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class HNewsDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 3;
    public static final String DATABASE_NAME = "hnews.db";

    public HNewsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_STORIES_TABLE = "CREATE TABLE " + HNewsContract.TABLE_ITEM_NAME + " (" +
                HNewsContract.StoryEntry._ID + " INTEGER PRIMARY KEY," +
                HNewsContract.StoryEntry.COLUMN_ITEM_ID + " INTEGER UNIQUE NOT NULL," +
                HNewsContract.StoryEntry.COLUMN_TYPE + " TEXT," +
                HNewsContract.StoryEntry.COLUMN_BY + " TEXT," +
                HNewsContract.StoryEntry.COLUMN_COMMENTS + " INTEGER," +
                HNewsContract.StoryEntry.COLUMN_DOMAIN + " TEXT," +
                HNewsContract.StoryEntry.COLUMN_URL + " TEXT," +
                HNewsContract.StoryEntry.COLUMN_SCORE + " INTEGER," +
                HNewsContract.StoryEntry.COLUMN_TITLE + " TEXT," +
                HNewsContract.StoryEntry.COLUMN_TIME_AGO + " TEXT," +
                HNewsContract.StoryEntry.COLUMN_RANK + " INTEGER," +
                HNewsContract.StoryEntry.COLUMN_TIMESTAMP + " INTEGER" +
                HNewsContract.StoryEntry.COLUMN_BOOKMARK + " INTEGER DEFAULT 0" +
                " );";

        final String SQL_CREATE_COMMENTS_TABLE = "CREATE TABLE " + HNewsContract.TABLE_COMMENTS_NAME + " (" +
                HNewsContract.CommentsEntry._ID + " INTEGER PRIMARY KEY," +
                HNewsContract.CommentsEntry.COLUMN_ITEM_ID + " INTEGER," +
                HNewsContract.CommentsEntry.COLUMN_LEVEL + " INTEGER," +
                HNewsContract.CommentsEntry.COLUMN_BY + " TEXT," +
                HNewsContract.CommentsEntry.COLUMN_TEXT + " TEXT," +
                HNewsContract.CommentsEntry.COLUMN_TIME_AGO + " TEXT," +
                HNewsContract.CommentsEntry.COLUMN_HEADER + " BOOLEAN" +
                " );";

        final String SQL_CREATE_BOOKMARKS_TABLE = "CREATE TABLE " + HNewsContract.TABLE_BOOKMARKS_NAME + " (" +
                HNewsContract.BookmarkEntry._ID + " INTEGER PRIMARY KEY," +
                HNewsContract.BookmarkEntry.COLUMN_ITEM_ID + " INTEGER UNIQUE NOT NULL," +
                HNewsContract.BookmarkEntry.COLUMN_TYPE + " TEXT," +
                HNewsContract.BookmarkEntry.COLUMN_BY + " TEXT," +
                HNewsContract.BookmarkEntry.COLUMN_DOMAIN + " TEXT," +
                HNewsContract.BookmarkEntry.COLUMN_URL + " TEXT," +
                HNewsContract.BookmarkEntry.COLUMN_TITLE + " TEXT," +
                HNewsContract.BookmarkEntry.COLUMN_TIMESTAMP + " INTEGER" +
                " );";

        db.execSQL(SQL_CREATE_STORIES_TABLE);
        db.execSQL(SQL_CREATE_COMMENTS_TABLE);
        db.execSQL(SQL_CREATE_BOOKMARKS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + HNewsContract.TABLE_ITEM_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + HNewsContract.TABLE_COMMENTS_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + HNewsContract.TABLE_BOOKMARKS_NAME);
        onCreate(db);
    }
}
