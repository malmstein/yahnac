package com.malmstein.yahnac.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class HNewsDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "yahnac.db";
    private static final int DATABASE_VERSION = 10;

    public HNewsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_STORIES_TABLE = "CREATE TABLE " + HNewsContract.TABLE_ITEM_NAME + " (" +
                HNewsContract.StoryEntry._ID + " INTEGER PRIMARY KEY," +
                HNewsContract.StoryEntry.ITEM_ID + " INTEGER UNIQUE NOT NULL," +
                HNewsContract.StoryEntry.TYPE + " TEXT," +
                HNewsContract.StoryEntry.BY + " TEXT," +
                HNewsContract.StoryEntry.COMMENTS + " INTEGER," +
                HNewsContract.StoryEntry.URL + " TEXT," +
                HNewsContract.StoryEntry.SCORE + " INTEGER," +
                HNewsContract.StoryEntry.TITLE + " TEXT," +
                HNewsContract.StoryEntry.TIME_AGO + " INTEGER," +
                HNewsContract.StoryEntry.RANK + " INTEGER," +
                HNewsContract.StoryEntry.TIMESTAMP + " INTEGER, " +
                HNewsContract.StoryEntry.BOOKMARK + " INTEGER DEFAULT 0, " +
                HNewsContract.StoryEntry.READ + " INTEGER DEFAULT 0, " +
                HNewsContract.StoryEntry.VOTED + " INTEGER DEFAULT 0, " +
                HNewsContract.StoryEntry.FILTER + " TEXT" +
                " );";

        final String SQL_CREATE_COMMENTS_TABLE = "CREATE TABLE " + HNewsContract.TABLE_COMMENTS_NAME + " (" +
                HNewsContract.CommentsEntry._ID + " INTEGER PRIMARY KEY," +
                HNewsContract.CommentsEntry.ITEM_ID + " INTEGER," +
                HNewsContract.CommentsEntry.LEVEL + " INTEGER," +
                HNewsContract.CommentsEntry.BY + " TEXT," +
                HNewsContract.CommentsEntry.TEXT + " TEXT," +
                HNewsContract.CommentsEntry.TIME_AGO + " TEXT," +
                HNewsContract.CommentsEntry.HEADER + " INTEGER DEFAULT 0," +
                HNewsContract.CommentsEntry.COMMENT_ID + " INTEGER" +
                " );";

        final String SQL_CREATE_BOOKMARKS_TABLE = "CREATE TABLE " + HNewsContract.TABLE_BOOKMARKS_NAME + " (" +
                HNewsContract.BookmarkEntry._ID + " INTEGER PRIMARY KEY," +
                HNewsContract.BookmarkEntry.ITEM_ID + " INTEGER UNIQUE NOT NULL," +
                HNewsContract.BookmarkEntry.TYPE + " TEXT," +
                HNewsContract.BookmarkEntry.BY + " TEXT," +
                HNewsContract.BookmarkEntry.URL + " TEXT," +
                HNewsContract.BookmarkEntry.TITLE + " TEXT," +
                HNewsContract.BookmarkEntry.TIMESTAMP + " INTEGER, " +
                HNewsContract.BookmarkEntry.FILTER + " TEXT" +
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
