package com.teamtreehouse.contentproviders;

import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

public class DatabaseContract {
    public static final String TABLE_NAME_TASKS = "tasks";

    public static final class TaskColumns implements BaseColumns {
        public static final String COLUMN_FIELD_NAME = "name";
    }

    public static final String CONTENT_AUTHORITY = String.format("%s.provider", BuildConfig.APPLICATION_ID);

    public static final Uri CONTENT_URI = new Uri.Builder()
            .scheme("content")
            .authority(CONTENT_AUTHORITY)
            .appendPath(TABLE_NAME_TASKS)
            .build();

    /* Helpers to retrieve column values */
    public static String getColumnString(Cursor cursor, String columnName) {
        return cursor.getString( cursor.getColumnIndex(columnName) );
    }

    public static int getColumnInt(Cursor cursor, String columnName) {
        return cursor.getInt( cursor.getColumnIndex(columnName) );
    }

    public static long getColumnLong(Cursor cursor, String columnName) {
        return cursor.getLong( cursor.getColumnIndex(columnName) );
    }
}
