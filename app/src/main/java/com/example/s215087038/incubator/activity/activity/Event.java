package com.example.s215087038.incubator.activity.activity;

import com.example.s215087038.incubator.activity.entity.Device;

import java.io.Serializable;

public class Event implements Serializable {
    private static final long serialVersionUID = 1;
    private Device device;
    private String eventName;
    private String eventTime;
    private String number;
    private String pin;

    public Device getDevice() {
        return this.device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public String getNumber() {
        return this.number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getEventName() {
        return this.eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getPin() {
        return this.pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getEventTime() {
        return this.eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }
}
