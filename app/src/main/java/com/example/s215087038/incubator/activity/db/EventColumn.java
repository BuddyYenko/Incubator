package com.example.s215087038.incubator.activity.db;

import android.net.Uri;
import java.util.HashMap;
import java.util.Map;

public class EventColumn extends DatabaseColumn {
    public static final String COL_DEVICE_NAME = "device_name";
    public static final String COL_ID = "number";
    public static final String COL_NAME = "name";
    public static final String COL_PIN = "pin";
    public static final String COL_TIME = "time";
    public static final Uri CONTENT_URI = Uri.parse("content://com.zkteco.contentprovider/events");
    public static final String TABLE_NAME = "events";
    private static final Map<String, String> mColumnMap = new HashMap();

    static {
        mColumnMap.put(COL_DEVICE_NAME, "text");
        mColumnMap.put(COL_NAME, "text");
        mColumnMap.put(COL_PIN, "text");
        mColumnMap.put(COL_ID, "text");
        mColumnMap.put(COL_TIME, "text");
    }

    public String getTableName() {
        return TABLE_NAME;
    }

    public Uri getTableContent() {
        return CONTENT_URI;
    }

    protected Map<String, String> getTableMap() {
        return mColumnMap;
    }
}
