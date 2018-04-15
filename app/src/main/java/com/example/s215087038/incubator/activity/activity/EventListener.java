package com.example.s215087038.incubator.activity.activity;


import com.example.s215087038.incubator.activity.entity.Device;

public interface EventListener {
    void onEvent(Device device, int i);

    void onEventDoorStatus(Device device, int i, int i2);
}
