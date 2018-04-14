package com.example.s215087038.incubator.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import me.zhanghai.android.materialprogressbar.BuildConfig;

public class Device implements Serializable {
    public static final int MAX_NUM = 10;
    public static final String PORT = "4370";
    public static final String key = "ComPwd";
    private static final long serialVersionUID = 1;
    private String connKey = BuildConfig.FLAVOR;
    private String deviceName;
    private String deviceParam;
    private List<Door> doors = new ArrayList();
    private long handle = 0;
    private String iP;
    private boolean isEquals = false;
    private boolean isTestedConn = false;
    private String passWord;
    private String port;
    private String rtLog;

    public List<Door> getDoors() {
        if (this.doors == null) {
            this.doors = new ArrayList();
        }
        return this.doors;
    }

    public void setDoors(List<Door> curDoors) {
        this.doors = curDoors;
    }

    public void addDoors(Door door) {
        if (door != null) {
            this.doors = new ArrayList();
            if (!this.doors.contains(door)) {
                this.doors.add(door);
            }
        }
    }

    public String getPassWord() {
        return this.passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getRtLog() {
        return this.rtLog;
    }

    public void setRtLog(String rtLog) {
        this.rtLog = rtLog;
    }

    public String getDeviceParam() {
        return this.deviceParam;
    }

    public void setDeviceParam(String deviceParam) {
        this.deviceParam = deviceParam;
    }

    public String getPort() {
        return this.port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public long getHandle() {
        return this.handle;
    }

    public void setHandle(long handle) {
        this.handle = handle;
    }

    public String getDeviceName() {
        return this.deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getiP() {
        return this.iP;
    }

    public void setiP(String iP) {
        this.iP = iP;
    }

    public String getDeviceIp() {
        return this.iP + ":" + this.port;
    }

    public String getConnKey() {
        return this.connKey;
    }

    public void setConnKey(String connKey) {
        this.connKey = connKey;
    }

    public boolean isTestedConn() {
        return this.isTestedConn;
    }

    public void setTestedConn(boolean isTestedConn) {
        this.isTestedConn = isTestedConn;
    }

    public boolean isConnect() {
        if (this.handle != 0) {
            return true;
        }
        return false;
    }

    public boolean isEquals() {
        return this.isEquals;
    }

    public void setEquals(boolean isEquals) {
        this.isEquals = isEquals;
    }

    public int hashCode() {
        return super.hashCode();
    }

    public boolean equals(Object o) {
        if (o.hashCode() == hashCode()) {
            return true;
        }
        return false;
    }
}
