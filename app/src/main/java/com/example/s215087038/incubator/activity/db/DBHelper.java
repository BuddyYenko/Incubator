package com.example.s215087038.incubator.activity.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.s215087038.incubator.activity.BuildConf;


public class DBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "zk";
    private static final int DB_VERSION = 2;
    private static DBHelper mdbHelper;
    private SQLiteDatabase db;

    public static DBHelper getInstance(Context context) {
        if (mdbHelper == null) {
            mdbHelper = new DBHelper(context);
        }
        return mdbHelper;
    }

    private DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    private DBHelper(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public void onCreate(SQLiteDatabase db) {
        this.db = db;
        operateTable(db, BuildConf.FLAVOR);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            operateTable(db, "DROP TABLE IF EXISTS ");
            onCreate(db);
        }
    }

    public void operateTable(SQLiteDatabase db, String actionString) {
        Class<DatabaseColumn>[] columnsClasses = DatabaseColumn.getSubClasses();
        int i = 0;
        while (i < columnsClasses.length) {
            try {
                DatabaseColumn columns = (DatabaseColumn) columnsClasses[i].newInstance();
                if (BuildConf.FLAVOR.equals(actionString) || actionString == null) {
                    db.execSQL(columns.getTableCreateor());
                    i++;
                } else {
                    db.execSQL(actionString + columns.getTableName());
                    i++;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public long insert(String Table_Name, ContentValues values) {
        if (this.db == null) {
            this.db = getWritableDatabase();
        }
        return this.db.insert(Table_Name, null, values);
    }

    public int delete(String Table_Name, String WhereClause, String[] whereArgs) {
        if (this.db == null) {
            this.db = getWritableDatabase();
        }
        return this.db.delete(Table_Name, WhereClause, whereArgs);
    }

    public int update(String Table_Name, ContentValues values, String WhereClause, String[] whereArgs) {
        if (this.db == null) {
            this.db = getWritableDatabase();
        }
        return this.db.update(Table_Name, values, WhereClause, whereArgs);
    }

    public Cursor query(String Table_Name, String[] columns, String whereStr, String[] whereArgs) {
        if (this.db == null) {
            this.db = getReadableDatabase();
        }
        return this.db.query(Table_Name, columns, whereStr, whereArgs, null, null, null);
    }

    public Cursor rawQuery(String sql, String[] args) {
        if (this.db == null) {
            this.db = getReadableDatabase();
        }
        return this.db.rawQuery(sql, args);
    }

    public void ExecSQL(String sql) {
        if (this.db == null) {
            this.db = getWritableDatabase();
        }
        this.db.execSQL(sql);
    }

    public void closeDb() {
        if (this.db != null) {
            this.db.close();
            this.db = null;
        }
    }
}
