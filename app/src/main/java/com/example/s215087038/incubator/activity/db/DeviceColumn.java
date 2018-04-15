package com.example.s215087038.incubator.activity.db;

import android.net.Uri;
import java.util.HashMap;
import java.util.Map;

public class DeviceColumn extends DatabaseColumn {
    public static final String COL_DOOR = "door";
    public static final String COL_IP = "ip";
    public static final String COL_NAME = "name";
    public static final String COL_PASSWORD = "password";
    public static final Uri CONTENT_URI = Uri.parse("content://com.zkteco.contentprovider/devices");
    public static final String TABLE_NAME = "devices";
    private static final Map<String, String> mColumnMap = new HashMap();

    static {
        mColumnMap.put(COL_NAME, "text");
        mColumnMap.put(COL_IP, "text");
        mColumnMap.put(COL_DOOR, "text");
        mColumnMap.put(COL_PASSWORD, "text");
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
