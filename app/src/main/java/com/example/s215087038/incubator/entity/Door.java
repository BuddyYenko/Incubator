package com.example.s215087038.incubator.entity;

import java.io.Serializable;

public class Door implements Serializable {
    public static final String DOOR_CLOSED = "1";
    public static final String DOOR_OPENED = "2";
    public static final int MAX_NUM = 12;
    public static final int MAX_OPEN_NUM_SIMULTANEOUS = 4;
    public static final String NO_MAGNETOMETER = "0";
    public static final int OPEN_DOOR_FAIL = -2;
    private static final long serialVersionUID = 1;
    private Device device;
    private int doorNum;
    private String doorPinString;
    private String doorState;
    private int openResult;
    private int openTime;

    public int getOpenResult() {
        return this.openResult;
    }

    public void setOpenResult(int openResult) {
        this.openResult = openResult;
    }

    public int getDoorNum() {
        return this.doorNum;
    }

    public void setDoorNum(int doorNum) {
        this.doorNum = doorNum;
    }

    public Device getDevice() {
        return this.device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public int getOpenTime() {
        return this.openTime;
    }

    public void setOpenTime(int openTime) {
        this.openTime = openTime;
    }

    public String getDoorPinString() {
        return this.doorPinString;
    }

    public void setDoorPinString(String doorPinString) {
        this.doorPinString = doorPinString;
    }

    public String getDoorState() {
        return this.doorState;
    }

    public void setDoorState(String doorState) {
        this.doorState = doorState;
    }

    public boolean isControlOpenSuccess(int ret) {
        if (ret < 0) {
            return false;
        }
        return true;
    }
}
