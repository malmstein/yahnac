package com.malmstein.hnews.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class HNewsProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private HNewsDbHelper mOpenHelper;

    private static final int STORY = 100;
    private static final int STORY_ITEM = 101;
    private static final int COMMENT = 102;

    @Override
    public boolean onCreate() {
        mOpenHelper = new HNewsDbHelper(getContext());
        mOpenHelper.getReadableDatabase().execSQL("PRAGMA foreign_keys=ON");
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            case STORY: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        HNewsContract.ItemEntry.TABLE_ITEM_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case STORY_ITEM: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        HNewsContract.ItemEntry.TABLE_ITEM_NAME,
                        projection,
                        HNewsContract.ItemEntry._ID + " = '" + ContentUris.parseId(uri) + "'",
                        null,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case COMMENT: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        HNewsContract.ItemEntry.TABLE_COMMENTS_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case STORY:
                return HNewsContract.ItemEntry.CONTENT_STORY_TYPE;
            case STORY_ITEM:
                return HNewsContract.ItemEntry.CONTENT_STORY_ITEM_TYPE;
            case COMMENT:
                return HNewsContract.ItemEntry.CONTENT_COMMENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case STORY: {
                Cursor exists = db.query(HNewsContract.ItemEntry.TABLE_ITEM_NAME,
                        new String[]{HNewsContract.ItemEntry.COLUMN_ITEM_ID},
                        HNewsContract.ItemEntry.COLUMN_ITEM_ID + " = ?",
                        new String[]{values.getAsString(HNewsContract.ItemEntry.COLUMN_ITEM_ID)},
                        null,
                        null,
                        null);
                if (exists.moveToLast()) {
                    long _id = db.update(HNewsContract.ItemEntry.TABLE_ITEM_NAME, values, HNewsContract.ItemEntry.COLUMN_ITEM_ID + " = ?",
                            new String[]{values.getAsString(HNewsContract.ItemEntry.COLUMN_ITEM_ID)});
                    if (_id > 0) {
                        returnUri = HNewsContract.ItemEntry.buildStoryUriWith(_id);
                    } else {
                        throw new android.database.SQLException("Failed to insert row into " + uri);
                    }
                } else {
                    long _id = db.insert(HNewsContract.ItemEntry.TABLE_ITEM_NAME, null, values);
                    if (_id > 0) {
                        returnUri = HNewsContract.ItemEntry.buildStoryUriWith(_id);
                    } else {
                        throw new android.database.SQLException("Failed to insert row into " + uri);
                    }
                }
                exists.close();
                break;
            }
            case COMMENT: {
                long _id = db.insert(HNewsContract.ItemEntry.TABLE_COMMENTS_NAME, null, values);
                if (_id > 0) {
                    returnUri = HNewsContract.ItemEntry.buildCommentUriWith(_id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        switch (match) {
            case STORY:
                rowsDeleted = db.delete(
                        HNewsContract.ItemEntry.TABLE_ITEM_NAME, selection, selectionArgs);
                break;
            case COMMENT:
                rowsDeleted = db.delete(
                        HNewsContract.ItemEntry.TABLE_COMMENTS_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (selection == null || rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case STORY:
                rowsUpdated = db.update(HNewsContract.ItemEntry.TABLE_ITEM_NAME, values, selection,
                        selectionArgs);
                break;
            case COMMENT:
                rowsUpdated = db.update(HNewsContract.ItemEntry.TABLE_COMMENTS_NAME, values, selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int returnCount;

        switch (match) {
            case COMMENT:
                db.beginTransaction();
                returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(HNewsContract.ItemEntry.TABLE_COMMENTS_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;

            case STORY:
                db.beginTransaction();
                returnCount = 0;
                try {
                    for (ContentValues value : values) {

                        Cursor exists = db.query(HNewsContract.ItemEntry.TABLE_ITEM_NAME,
                                new String[]{HNewsContract.ItemEntry.COLUMN_ITEM_ID},
                                HNewsContract.ItemEntry.COLUMN_ITEM_ID + " = ?",
                                new String[]{value.getAsString(HNewsContract.ItemEntry.COLUMN_ITEM_ID)},
                                null,
                                null,
                                null);

                        if (exists.moveToLast()) {
                            long _id = db.update(HNewsContract.ItemEntry.TABLE_ITEM_NAME, value, HNewsContract.ItemEntry.COLUMN_ITEM_ID + " = ?",
                                    new String[]{value.getAsString(HNewsContract.ItemEntry.COLUMN_ITEM_ID)});

                            if (_id != -1) {
                                returnCount++;
                            }
                        } else {
                            long _id = db.insert(HNewsContract.ItemEntry.TABLE_ITEM_NAME, null, value);
                            if (_id != -1) {
                                returnCount++;
                            }
                        }
                        exists.close();
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            default:
                return super.bulkInsert(uri, values);
        }
    }

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = HNewsContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, HNewsContract.PATH_ITEM, STORY);
        matcher.addURI(authority, HNewsContract.PATH_ITEM + "/*", STORY_ITEM);
        matcher.addURI(authority, HNewsContract.PATH_COMMENT, COMMENT);

        return matcher;
    }
}
