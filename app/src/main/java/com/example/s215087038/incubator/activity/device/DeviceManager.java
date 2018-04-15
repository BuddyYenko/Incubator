package com.example.s215087038.incubator.activity.device;

import android.util.Log;

import com.example.s215087038.incubator.activity.StringUtil;
import com.example.s215087038.incubator.activity.activity.EventHandler;
import com.example.s215087038.incubator.activity.activity.EventListener;
import com.example.s215087038.incubator.activity.data.DeviceParam;
import com.example.s215087038.incubator.activity.entity.Device;
import com.example.s215087038.incubator.activity.entity.Door;
import com.example.s215087038.incubator.activity.entity.ResultCode;
import com.example.s215087038.incubator.activity.helpers.ThreadFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DeviceManager extends AbstractDevice {
    public static final String TAG = DeviceManager.class.getName();
    private List<Device> devices;
    private EventHandler eventHandler;
    private boolean loop;

    public static Object getAM10Lock() {
        return am10Lock;
    }

    public DeviceManager() {
        this.devices = null;
        this.eventHandler = null;
        this.devices = new ArrayList();
        this.eventHandler = new EventHandler();
    }

    public void addEventListen(String idString, EventListener listener) {
        this.eventHandler.add(idString, listener);
    }

    public void removeEventListen(String idString) {
        this.eventHandler.remove(idString);
    }

    public void addDevice(Device device) {
        if (!this.devices.contains(device)) {
            this.devices.add(device);
        }
    }

    public void removeDevice(Device device) {
        if (this.devices.contains(device)) {
            this.devices.remove(device);
            device.setHandle(0);
        }
    }

    public List<Device> getDevices() {
        return this.devices;
    }

    public void disConnectDevices() {
        if (!this.devices.isEmpty()) {
            for (Device device : this.devices) {
                if (device.getHandle() != 0) {
                    try {
                        device.setHandle(0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public List<Device> getSearchDevices() {
        List<Device> deviceFromLan = new ArrayList();
        try {
            String buffer = searchDevicesBuffer();
            if (!StringUtil.isEmpty(buffer)) {
                String[] bufferArray = buffer.split("\r\n");
                Log.d(TAG, "device buffer -- " + Arrays.toString(bufferArray));
                List<String> bufferListTemp = Arrays.asList(bufferArray);
                List<String> bufferList = new ArrayList();
                for (String temp : bufferListTemp) {
                    if (!temp.contains("Protype=push")) {
                        bufferList.add(temp);
                    }
                }
                for (String temp2 : bufferList) {
                    String ipStr = null;
                    String deviceName = null;
                    String[] macStr = temp2.split(",");
                    for (int i = 0; i < macStr.length; i++) {
                        if (macStr[i].startsWith("IP=")) {
                            ipStr = macStr[i].substring("IP=".length());
                            Log.d(TAG, "device ip -- " + ipStr);
                        }
                        if (macStr[i].startsWith("Device=")) {
                            deviceName = macStr[i].substring("Device=".length());
                            Log.d(TAG, "device name -- " + deviceName);
                        }
                    }
                    if (!(StringUtil.isEmpty(ipStr) || StringUtil.isEmpty(deviceName))) {
                        Device device = new Device();
                        device.setDeviceName(deviceName);
                        device.setiP(ipStr);
                        deviceFromLan.add(device);
                    }
                }
                Log.d(TAG, "deviceFromLan size -- " + deviceFromLan.size());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return deviceFromLan;
    }

    public void setDoorState(Device device) {
        try {
            List<Door> curDoors = device.getDoors();
            int length = curDoors.size();
            for (int i = 0; i < length; i++) {
                ((Door) curDoors.get(i)).setDevice(device);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Door> setDeviceDoorState(long handle, int doorCount) {
        List<Door> doors = new ArrayList();
        int i = 1;
        while (i <= doorCount) {
            try {
                Door door = new Door();
                int state = getDoorState(handle, i);
                door.setDoorState(String.valueOf(state));
                door.setDoorPinString("00" + String.valueOf(i));
                door.setDoorNum(i);
                Log.d(TAG, "device setDeviceDoors --state = " + state);
                doors.add(door);
                i++;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return doors;
    }

    public List<Door> setDeviceDoorState(List<Door> deviceDoors, long handle) {
        if (!(deviceDoors == null || deviceDoors.isEmpty())) {
            try {
                for (Door door : deviceDoors) {
                    Log.d(TAG, "device getDeviceDoorNum --" + door.getDoorNum());
                    door.setDoorState(String.valueOf(getDoorState(handle, door.getDoorNum())));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return deviceDoors;
    }

    public void listenDeviceStatus(final Device device) {
        ThreadFactory.startNewThread(TAG, new Runnable() {
            public void run() {
                DeviceManager.this.loop = device.isConnect();
                boolean curLoop = device.isConnect();
                int doorCount = 0;
                if (DeviceManager.this.loop) {
                    do {
                        String numStr = device.getDeviceParam();
                        if (!StringUtil.isEmpty(numStr)) {
                            doorCount = Integer.parseInt(numStr);
                        }
                        if (DeviceManager.this.reTryLongConnect(device, doorCount, 0) < 0) {
                            curLoop = false;
                            continue;
                        } else {
                            try {
                                Thread.sleep(5000);
                                continue;
                            } catch (InterruptedException e) {
                                Log.w(DeviceManager.TAG, Log.getStackTraceString(e));
                                continue;
                            }
                        }
                    } while (curLoop);
                }
            }
        });
    }

    private long reTryLongConnect(long handle, int i, String ip, String port, String paw) {
        long ret;
        try {
            ret = (long) getDoorState(handle, 1);
        } catch (Exception e) {
            e.printStackTrace();
            ret = -203;
        }
        Log.d(TAG, "GET LOCK STATE result= " + ret);
        if (ret < 0) {
            Log.d(TAG, "retry count = " + i);
            if (i == 1) {
                return ret;
            }
            i++;
            try {
                ret = connectDevice(ip, port, paw);
            } catch (Exception e2) {
                e2.printStackTrace();
                ret = -203;
            }
        }
        return ret;
    }

    private int reTryLongConnect(long handle, int i) {
        int ret;
        try {
            ret = getDoorState(handle, 1);
        } catch (Exception e) {
            e.printStackTrace();
            ret = ResultCode.COMM_INIT_FAILED;
        }
        Log.d(TAG, "GET LOCK STATE result= " + ret);
        if (ret < 0) {
            Log.d(TAG, "retry count = " + i);
            if (i == 1) {
                return ret;
            }
            i++;
            try {
                ret = getDoorState(handle, 1);
            } catch (Exception e2) {
                e2.printStackTrace();
                ret = ResultCode.COMM_INIT_FAILED;
            }
        }
        return ret;
    }

    private long reTryLongConnect(Device device, int doorCount, int i) {
        long handle = device.getHandle();
        int ret = 4;
        for (int index = 1; index <= doorCount; index++) {
            try {
                ret = getDoorState(handle, index);
                Log.d(TAG, "first  listen to Device " + device.getDeviceName() + " doorNo." + index + " state ret = " + ret);
            } catch (Exception e) {
                e.printStackTrace();
                ret = ResultCode.COMM_INIT_FAILED;
            }
            this.eventHandler.obtainMessage(DeviceParam.DEVICE_CONNECT_CODE, ret, index, device).sendToTarget();
            if (ret < 0) {
                return (long) ret;
            }
        }
        return (long) ret;
    }

    public void stop() {
        Log.d(TAG, "Stopping");
        this.loop = false;
    }

    public int openLockState(Device device, int doorNo, int time) {
        int ret = 0;
        try {
            ret = setLockState(device, doorNo, time);
            if (-2 == ret) {
                Log.d(TAG, "listen to door open state ret = " + ret);
                this.eventHandler.obtainMessage(DeviceParam.DEVICE_CONNECT_CODE, ret, 0, device).sendToTarget();
            }
            return ret;
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
            if (-2 == ret) {
                Log.d(TAG, "listen to door open state ret = " + ret);
                this.eventHandler.obtainMessage(DeviceParam.DEVICE_CONNECT_CODE, ret, 0, device).sendToTarget();
            }
            return ret;
        } catch (Throwable th) {
            if (-2 == ret) {
                Log.d(TAG, "listen to door open state ret = " + ret);
                this.eventHandler.obtainMessage(DeviceParam.DEVICE_CONNECT_CODE, ret, 0, device).sendToTarget();
            }
            return ret;
        }
    }
}
