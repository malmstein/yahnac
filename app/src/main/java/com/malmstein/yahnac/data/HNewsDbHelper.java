package com.malmstein.yahnac.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class HNewsDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "hnews.db";

    public HNewsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_STORIES_TABLE = "CREATE TABLE " + HNewsContract.ItemEntry.TABLE_ITEM_NAME + " (" +
                HNewsContract.ItemEntry._ID + " INTEGER PRIMARY KEY," +
                HNewsContract.ItemEntry.COLUMN_ITEM_ID + " INTEGER UNIQUE NOT NULL," +
                HNewsContract.ItemEntry.COLUMN_TYPE + " TEXT," +
                HNewsContract.ItemEntry.COLUMN_BY + " TEXT," +
                HNewsContract.ItemEntry.COLUMN_COMMENTS + " INTEGER," +
                HNewsContract.ItemEntry.COLUMN_DOMAIN + " TEXT," +
                HNewsContract.ItemEntry.COLUMN_URL + " TEXT," +
                HNewsContract.ItemEntry.COLUMN_SCORE + " INTEGER," +
                HNewsContract.ItemEntry.COLUMN_TITLE + " TEXT," +
                HNewsContract.ItemEntry.COLUMN_TIME_AGO + " TEXT," +
                HNewsContract.ItemEntry.COLUMN_RANK + " INTEGER," +
                HNewsContract.ItemEntry.COLUMN_TIMESTAMP + " INTEGER" +
                " );";

        final String SQL_CREATE_COMMENTS_TABLE = "CREATE TABLE " + HNewsContract.ItemEntry.TABLE_COMMENTS_NAME + " (" +
                HNewsContract.ItemEntry._ID + " INTEGER PRIMARY KEY," +
                HNewsContract.ItemEntry.COLUMN_ITEM_ID + " INTEGER," +
                HNewsContract.ItemEntry.COLUMN_LEVEL + " INTEGER," +
                HNewsContract.ItemEntry.COLUMN_BY + " TEXT," +
                HNewsContract.ItemEntry.COLUMN_TEXT + " TEXT," +
                HNewsContract.ItemEntry.COLUMN_TIME_AGO + " TEXT," +
                HNewsContract.ItemEntry.COLUMN_HEADER + " BOOLEAN" +
                " );";

        final String SQL_CREATE_BOOKMARKS_TABLE = "CREATE TABLE " + HNewsContract.ItemEntry.TABLE_BOOKMARKS_NAME + " (" +
                HNewsContract.ItemEntry._ID + " INTEGER PRIMARY KEY," +
                HNewsContract.ItemEntry.COLUMN_ITEM_ID + " INTEGER UNIQUE NOT NULL," +
                HNewsContract.ItemEntry.COLUMN_BY + " TEXT," +
                HNewsContract.ItemEntry.COLUMN_DOMAIN + " TEXT," +
                HNewsContract.ItemEntry.COLUMN_URL + " TEXT," +
                HNewsContract.ItemEntry.COLUMN_TITLE + " TEXT," +
                HNewsContract.ItemEntry.COLUMN_TIMESTAMP + " INTEGER" +
                " );";

        db.execSQL(SQL_CREATE_STORIES_TABLE);
        db.execSQL(SQL_CREATE_COMMENTS_TABLE);
        db.execSQL(SQL_CREATE_BOOKMARKS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + HNewsContract.ItemEntry.TABLE_ITEM_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + HNewsContract.ItemEntry.TABLE_COMMENTS_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + HNewsContract.ItemEntry.TABLE_BOOKMARKS_NAME);
        onCreate(db);
    }
}
