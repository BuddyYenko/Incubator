package com.example.s215087038.incubator.activity.db;

import android.net.Uri;
import android.provider.BaseColumns;
import java.util.ArrayList;
import java.util.Map;

public abstract class DatabaseColumn implements BaseColumns {
    public static final String AUTHORITY = "com.zkteco.contentprovider";
    public static final String DATABASE_NAME = "zkbiosecurity.db";
    public static final int DATABASE_VERSION = 1;
    public static final String[] SUBCLASSES = new String[]{DeviceColumn.class.getCanonicalName(), EventColumn.class.getCanonicalName()};

    public abstract Uri getTableContent();

    protected abstract Map<String, String> getTableMap();

    public abstract String getTableName();

    public String getTableCreateor() {
        return getTableCreator(getTableName(), getTableMap());
    }

    public static final Class<DatabaseColumn>[] getSubClasses() {
        ArrayList<Class<DatabaseColumn>> classes = new ArrayList();
        for (int i = 0; i < SUBCLASSES.length; i += DATABASE_VERSION) {
            try {
                classes.add((Class<DatabaseColumn>) Class.forName(SUBCLASSES[i]));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return (Class[]) classes.toArray(new Class[0]);
    }

    private static final String getTableCreator(String tableName, Map<String, String> map) {
        String[] keys = (String[]) map.keySet().toArray(new String[0]);
        StringBuilder creator = new StringBuilder();
        creator.append("CREATE TABLE ").append(tableName).append("( ");
        int length = keys.length;
        for (int i = 0; i < length; i += DATABASE_VERSION) {
            String value = (String) map.get(keys[i]);
            creator.append(keys[i]).append(" ");
            creator.append(value);
            if (i < length - 1) {
                creator.append(",");
            }
        }
        creator.append(")");
        return creator.toString();
    }
}
