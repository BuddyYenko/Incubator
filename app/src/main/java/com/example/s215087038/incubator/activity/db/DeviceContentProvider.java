package com.example.s215087038.incubator.activity.db;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

public class DeviceContentProvider extends ContentProvider {
    private static final int DEVICES = 1;
    private static final int EVENTS = 2;
    private static final String TAG = DeviceContentProvider.class.getName();
    private static UriMatcher uriMatcher = new UriMatcher(-1);
    private DBHelper dbHelper;

    static {
        uriMatcher.addURI(DatabaseColumn.AUTHORITY, DeviceColumn.TABLE_NAME, DEVICES);
        uriMatcher.addURI(DatabaseColumn.AUTHORITY, EventColumn.TABLE_NAME, EVENTS);
    }

    public boolean onCreate() {
        if (this.dbHelper == null) {
            this.dbHelper = DBHelper.getInstance(getContext());
        }
        return false;
    }

    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        switch (uriMatcher.match(uri)) {
            case DEVICES /*1*/:
                return this.dbHelper.query(DeviceColumn.TABLE_NAME, projection, selection, selectionArgs);
            case EVENTS /*2*/:
                return this.dbHelper.query(EventColumn.TABLE_NAME, projection, selection, selectionArgs);
            default:
                return null;
        }
    }

    public String getType(Uri uri) {
        return null;
    }

    public Uri insert(Uri uri, ContentValues values) {
        switch (uriMatcher.match(uri)) {
            case DEVICES /*1*/:
                if (values != null) {
                    this.dbHelper.insert(DeviceColumn.TABLE_NAME, values);
                    break;
                }
                break;
            case EVENTS /*2*/:
                if (values != null && this.dbHelper.insert(EventColumn.TABLE_NAME, values) >= 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                    break;
                }
        }
        return null;
    }

    public int delete(Uri uri, String selection, String[] selectionArgs) {
        switch (uriMatcher.match(uri)) {
            case DEVICES /*1*/:
                return this.dbHelper.delete(DeviceColumn.TABLE_NAME, selection, selectionArgs);
            case EVENTS /*2*/:
                return this.dbHelper.delete(EventColumn.TABLE_NAME, selection, selectionArgs);
            default:
                return 0;
        }
    }

    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        switch (uriMatcher.match(uri)) {
            case DEVICES /*1*/:
                this.dbHelper.update(DeviceColumn.TABLE_NAME, values, selection, selectionArgs);
                break;
            case EVENTS /*2*/:
                this.dbHelper.update(EventColumn.TABLE_NAME, values, selection, selectionArgs);
                break;
        }
        return 0;
    }
}
