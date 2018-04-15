package com.teamtreehouse.contentproviders;

import android.database.Cursor;

public class Task {
    public int id;
    public String name;
    public Task() {

    }

    public Task(Cursor cursor) {
        id = DatabaseContract.getColumnInt(cursor, DatabaseContract.TaskColumns._ID);
        name = DatabaseContract.getColumnString(cursor, DatabaseContract.TaskColumns.COLUMN_FIELD_NAME);
    }

    @Override
    public String toString() {
        return String.format("%s. %s", id, name);
    }
}
