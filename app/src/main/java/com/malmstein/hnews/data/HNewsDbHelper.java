package com.malmstein.hnews.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class HNewsDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "hnews.db";

    public HNewsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_ITEM_TABLE = "CREATE TABLE " + HNewsContract.ItemEntry.TABLE_NAME + " (" +
                HNewsContract.ItemEntry._ID + " INTEGER PRIMARY KEY," +
                HNewsContract.ItemEntry.COLUMN_ITEM_ID + " INTEGER UNIQUE NOT NULL," +
                HNewsContract.ItemEntry.COLUMN_DELETED + " BOOLEAN NOT NULL," +
                HNewsContract.ItemEntry.COLUMN_TYPE + " TEXT," +
                HNewsContract.ItemEntry.COLUMN_BY + " TEXT," +
                HNewsContract.ItemEntry.COLUMN_TIME + " INTEGER ," +
                HNewsContract.ItemEntry.COLUMN_TEXT + " TEXT," +
                HNewsContract.ItemEntry.COLUMN_DEAD + " BOOLEAN," +
                HNewsContract.ItemEntry.COLUMN_PARENT + " INTEGER," +
                HNewsContract.ItemEntry.COLUMN_KIDS + " TEXT," +
                HNewsContract.ItemEntry.COLUMN_URL + " TEXT," +
                HNewsContract.ItemEntry.COLUMN_SCORE + " INTEGER," +
                HNewsContract.ItemEntry.COLUMN_TITLE + " TEXT," +
                HNewsContract.ItemEntry.COLUMN_PARTS + " TEXT," +
                " );";

        db.execSQL(SQL_CREATE_ITEM_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + HNewsContract.ItemEntry.TABLE_NAME);
        onCreate(db);
    }
}
