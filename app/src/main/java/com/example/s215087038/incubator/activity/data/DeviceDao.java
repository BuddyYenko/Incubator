package com.example.s215087038.incubator.activity.data;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.example.s215087038.incubator.activity.BuildConf;
import com.example.s215087038.incubator.activity.StringUtil;
import com.example.s215087038.incubator.activity.db.DeviceColumn;
import com.example.s215087038.incubator.activity.db.EventColumn;
import com.example.s215087038.incubator.activity.entity.Device;
import com.example.s215087038.incubator.activity.entity.Door;

import java.util.ArrayList;
import java.util.List;

public class DeviceDao {
    public static final String TAG = DeviceDao.class.getName();
    private static ContentResolver contentResolver;

    public static boolean insertOrUpdate(Context context, Device device) {
        if (device == null) {
            return false;
        }
        contentResolver = context.getContentResolver();
        String pwd = device.getPassWord();
        ContentValues contentvalues = new ContentValues();
        contentvalues.put(EventColumn.COL_NAME, device.getDeviceName());
        contentvalues.put(DeviceColumn.COL_IP, device.getDeviceIp());
        contentvalues.put(DeviceColumn.COL_DOOR, device.getDeviceParam());
        contentvalues.put(DeviceColumn.COL_PASSWORD, device.getPassWord());// MD5.encode(pwd, Device.key));
        Cursor cursor = selectDevice(context, device.getDeviceIp());
        if (cursor != null) {
            try {
                if (cursor.getCount() > 0) {
                    contentResolver.update(DeviceColumn.CONTENT_URI, contentvalues, "ip=?", new String[]{device.getDeviceIp()});
                    Log.d(TAG, "insertOrUpdate device success");
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.d(TAG, "insertOrUpdate device fail");
                return false;
            }
        }
        contentResolver.insert(DeviceColumn.CONTENT_URI, contentvalues);
        Log.d(TAG, "insertOrUpdate device success");
        return true;
    }

    public static Device selectedDevice(Context context, String ipString) {
        contentResolver = context.getContentResolver();
        Cursor cursor = selectDevice(context, ipString);
        if (cursor == null || cursor.getCount() <= 0) {
            return null;
        }
        Device device = new Device();
        if (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex(EventColumn.COL_NAME));
            String[] ipArray = cursor.getString(cursor.getColumnIndex(DeviceColumn.COL_IP)).split(":");
            device.setDeviceName(name);
            device.setiP(ipArray[0]);
            device.setPort(ipArray[1]);
        }
        cursor.close();
        return device;
    }

    public static int getDeviceNumFromDb(Context context) {
        Cursor cursor = selectAllDevice(context);
        int num = 0;
        if (cursor != null) {
            num = cursor.getCount();
        }
        Log.d(TAG, "getDeviceNumFromDb =" + num);
        return num;
    }

    public static ArrayList<Device> loadDevice(Context context) {
        ArrayList<Device> dataList = new ArrayList();
        contentResolver = context.getContentResolver();
        Cursor cursor = selectAllDevice(context);
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Device device = new Device();
                String name = cursor.getString(cursor.getColumnIndex(EventColumn.COL_NAME));
                String ip = cursor.getString(cursor.getColumnIndex(DeviceColumn.COL_IP));
                String doorCount = cursor.getString(cursor.getColumnIndex(DeviceColumn.COL_DOOR));
                String passWord = cursor.getString(cursor.getColumnIndex(DeviceColumn.COL_PASSWORD));
                if (!StringUtil.isEmpty(doorCount)) {
                    device.setDoors(parseDeviceParms(Integer.parseInt(doorCount)));
                    device.setDeviceParam(doorCount);
                }
                if (!StringUtil.isEmpty(ip)) {
                    String[] ipArray = ip.split(":");
                    device.setiP(ipArray[0]);
                    device.setPort(ipArray[1]);
                }
                device.setDeviceName(name);
                device.setPassWord(passWord);//MD5.decode(passWord, Device.key));
                dataList.add(device);
            }
            cursor.close();
        }
        return dataList;
    }

    public static boolean deleteDevice(Context context, String ip) {
        int ret = 0;
        Cursor cursor = selectDevice(context, ip);
        if (cursor != null && cursor.getCount() > 0) {
            contentResolver = context.getContentResolver();
            try {
                ret = contentResolver.delete(DeviceColumn.CONTENT_URI, "ip=?", new String[]{ip});
            } catch (Exception e) {
                ret = -1;
                e.printStackTrace();
            }
        }
        if (ret > 0) {
            return true;
        }
        return false;
    }

    public static boolean DeviceIsNullFromDb(Context context, String ip) {
        if (selectDevice(context, ip) == null) {
            return true;
        }
        return false;
    }

    public static Cursor selectDevice(Context context, String ip) {
        Cursor cursor = null;
        contentResolver = context.getContentResolver();
        try {
            cursor = contentResolver.query(DeviceColumn.CONTENT_URI, null, "ip=?", new String[]{ip}, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cursor;
    }

    public static Cursor selectAllDevice(Context context) {
        contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(DeviceColumn.CONTENT_URI, null, null, null, null);
        Log.d("tag", BuildConf.FLAVOR + cursor);
        return cursor;
    }

    public static List<Door> parseDeviceParms(String parms) {
        if (parms == null) {
            return null;
        }
        int doorCountTemp = Integer.parseInt(parms.split("=")[1]);
        List<Door> arrayList = new ArrayList();
        for (int i = 1; i <= doorCountTemp; i++) {
            Door door = new Door();
            door.setDoorPinString("00" + String.valueOf(i));
            arrayList.add(door);
        }
        return arrayList;
    }

    public static List<Door> parseDeviceParms(int parms) {
        List<Door> doors = new ArrayList();
        for (int i = 1; i <= parms; i++) {
            Door door = new Door();
            door.setDoorPinString("00" + String.valueOf(i));
            door.setDoorNum(i);
            doors.add(door);
        }
        return doors;
    }

    public static int getAllDoorNumFromDb(Context context) {
        Cursor cursor = selectAllDevice(context);
        int currentAllDoorNum = 0;
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String doorCount = cursor.getString(cursor.getColumnIndex(DeviceColumn.COL_DOOR));
                if (doorCount != null) {
                    currentAllDoorNum += Integer.valueOf(doorCount).intValue();
                }
            }
            cursor.close();
        }
        Log.d(TAG, "getAllDoorNumFromDb = " + currentAllDoorNum);
        return currentAllDoorNum;
    }
}
