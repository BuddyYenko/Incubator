package com.example.s215087038.incubator.event;


public interface EventListener {
    void onEvent(Device device, int i);

    void onEventDoorStatus(Device device, int i, int i2);
}
