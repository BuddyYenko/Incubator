package com.example.s215087038.incubator.activity.data;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.example.s215087038.incubator.activity.BuildConf;
import com.example.s215087038.incubator.activity.StringUtil;
import com.example.s215087038.incubator.activity.activity.Event;
import com.example.s215087038.incubator.activity.db.EventColumn;
import com.example.s215087038.incubator.activity.entity.Device;
import com.example.s215087038.incubator.activity.helpers.ThreadFactory;

import java.util.ArrayList;

public class EventDao {
    public static final String TAG = EventDao.class.getName();
    private static ContentResolver contentResolver;

    public static boolean insertOrUpdate(Context context, Event event) {
        if (event == null) {
            return false;
        }
        contentResolver = context.getContentResolver();
        ContentValues contentvalues = new ContentValues();
        contentvalues.put(EventColumn.COL_DEVICE_NAME, event.getDevice().getDeviceName());
        contentvalues.put(EventColumn.COL_NAME, event.getEventName());
        contentvalues.put(EventColumn.COL_PIN, event.getPin());
        contentvalues.put(EventColumn.COL_TIME, event.getEventTime());
        contentvalues.put(EventColumn.COL_ID, event.getNumber());
        Cursor cursor = selectEvent(context, event.getNumber());
        if (cursor != null) {
            try {
                if (cursor.getCount() > 0) {
                    contentResolver.update(EventColumn.CONTENT_URI, contentvalues, "time=?", new String[]{event.getEventTime()});
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        contentResolver.insert(EventColumn.CONTENT_URI, contentvalues);
        return true;
    }

    public static ArrayList<Event> loadEvent(Context context, Device device) {
        ArrayList<Event> dataList = new ArrayList();
        contentResolver = context.getContentResolver();
        if (device != null) {
            Cursor cursor = selectEventForDevice(context, device.getDeviceName());
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    Event data = new Event();
                    String eventName = cursor.getString(cursor.getColumnIndex(EventColumn.COL_NAME));
                    String pin = cursor.getString(cursor.getColumnIndex(EventColumn.COL_PIN));
                    String eventTime = cursor.getString(cursor.getColumnIndex(EventColumn.COL_TIME));
                    String id = cursor.getString(cursor.getColumnIndex(EventColumn.COL_ID));
                    data.setEventName(eventName);
                    data.setEventTime(eventTime);
                    data.setNumber(id);
                    data.setPin(pin);
                    dataList.add(data);
                }
                cursor.close();
            }
        }
        return dataList;
    }

    public static Cursor selectAllEvent(Context context) {
        contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(EventColumn.CONTENT_URI, null, null, null, null);
        Log.d("tag", BuildConf.FLAVOR + cursor);
        return cursor;
    }

    public static Cursor selectEventForDevice(Context context, String deviceName) {
        contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(EventColumn.CONTENT_URI, null, "device_name=?", new String[]{deviceName}, null);
        Log.d("tag", BuildConf.FLAVOR + cursor);
        return cursor;
    }

    public static Cursor selectEvent(Context context, String eventTime) {
        Cursor cursor = null;
        contentResolver = context.getContentResolver();
        try {
            cursor = contentResolver.query(EventColumn.CONTENT_URI, null, "time=?", new String[]{eventTime}, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cursor;
    }

    public static void parsingRTLog(Context context, Device device) {
        String rtLogString = null;
        if (!StringUtil.isEmpty(rtLogString)) {
            String[] parmsOneStrings = rtLogString.split("\r\n");
            for (String split : parmsOneStrings) {
                String[] singleLog = split.split(",");
                Event event = new Event();
                event.setEventTime(singleLog[0]);
                event.setPin(singleLog[1]);
                event.setNumber(singleLog[3]);
                event.setEventName(singleLog[4]);
                event.setDevice(device);
                if (insertOrUpdate(context, event)) {
                    Log.d(TAG, "insertOrUpdate success");
                }
            }
        }
    }

    public static void insertOrUpdateEvent(final Context context, final Device device) {
        ThreadFactory.startNewThread(TAG, new Runnable() {
            public void run() {
                EventDao.parsingRTLog(context, device);
            }
        });
    }
}
