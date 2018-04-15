package com.teamtreehouse.contentproviders;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.IntentFilter;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;

public class MyContentProvider extends ContentProvider {

    private Context mContext;
    private SQLiteDatabase mDatabase;

    private static final int TASKS = 100;
    private static final int TASKS_WITH_ID = 101;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(DatabaseContract.CONTENT_AUTHORITY, DatabaseContract.TABLE_NAME_TASKS, TASKS);
        sUriMatcher.addURI(DatabaseContract.CONTENT_AUTHORITY, String.format("%s/#", DatabaseContract.TABLE_NAME_TASKS), TASKS_WITH_ID);
    }

    @Override
    public boolean onCreate() {
        mContext = getContext();
        DatabaseHelper databaseHelper = new DatabaseHelper(mContext);
        mDatabase = databaseHelper.getWritableDatabase();

        return mDatabase != null;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] columns, String selection, String[] selectionArgs, String sortOrder) {
        switch (sUriMatcher.match(uri)) {
            case TASKS:
                return mDatabase.query(DatabaseContract.TABLE_NAME_TASKS, columns, selection, selectionArgs, null, null, sortOrder);
            case TASKS_WITH_ID:
                long taskId = ContentUris.parseId(uri);
                return mDatabase.query(DatabaseContract.TABLE_NAME_TASKS, columns, String.format("%s = ?", DatabaseContract.TaskColumns._ID), new String[]{String.valueOf(taskId)}, null, null, sortOrder);
            default:
                throw new IllegalArgumentException("Illegal delete URI");
        }
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues contentValues) {

        if (sUriMatcher.match(uri) == TASKS) {
            long rowId = mDatabase.insert(DatabaseContract.TABLE_NAME_TASKS, null, contentValues);
            if (rowId > -1) {
                Uri newUri = ContentUris.withAppendedId(DatabaseContract.CONTENT_URI, rowId);
                mContext.getContentResolver().notifyChange(newUri, null);
                return newUri;
            }
        }

        throw new IllegalArgumentException("Illegal delete URI " + uri);


    }

    @Override
    public int delete(@NonNull Uri uri, String where, String[] whereArgs) {
        switch (sUriMatcher.match(uri)) {
            case TASKS:
                where = (where == null) ? "1" : where;
                break;
            case TASKS_WITH_ID:
                long taskId = ContentUris.parseId(uri);
                where = String.format("%s = ?", DatabaseContract.TaskColumns._ID);
                whereArgs = new String[]{String.valueOf(taskId)};
                break;
            default:
                throw new IllegalArgumentException("Illegal delete URI");
        }


        int count = mDatabase.delete(DatabaseContract.TABLE_NAME_TASKS, where, whereArgs);
        if (count > 0) {
            mContext.getContentResolver().notifyChange(uri, null);
        }

        return count;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues contentValues, String where, String[] whereArgs) {
        switch (sUriMatcher.match(uri)) {
            case TASKS:
                where = (where == null) ? "1" : where;
                break;
            case TASKS_WITH_ID:
                long taskId = ContentUris.parseId(uri);
                where = String.format("%s = ?", DatabaseContract.TaskColumns._ID);
                whereArgs = new String[]{String.valueOf(taskId)};
                break;
            default:
                throw new IllegalArgumentException("Illegal delete URI " + uri);
        }

        return mDatabase.update(DatabaseContract.TABLE_NAME_TASKS, contentValues, where, whereArgs);
    }
}
