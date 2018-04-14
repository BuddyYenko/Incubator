package com.example.s215087038.incubator.handler;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.zktechnology.android.zkbiosecurity.data.DeviceParam;
import com.zktechnology.android.zkbiosecurity.entity.Device;
import com.zktechnology.android.zkbiosecurity.event.EventListener;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class EventHandler extends Handler {
    private static final String TAG = EventHandler.class.getName();
    private final Map<String, EventListener> listeners = Collections.synchronizedMap(new HashMap());

    public void add(String id, EventListener listener) {
        this.listeners.put(id, listener);
    }

    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        if (msg.what == DeviceParam.DEVICE_CONNECT_CODE) {
            Device device = msg.obj;
            int ret = msg.arg1;
            int index = msg.arg2;
            synchronized (this.listeners) {
                for (Entry<String, EventListener> entry : this.listeners.entrySet()) {
                    EventListener listener = (EventListener) entry.getValue();
                    if (ret < 0) {
                        listener.onEvent(device, ret);
                    } else {
                        listener.onEventDoorStatus(device, index, ret);
                    }
                }
            }
            return;
        }
        Log.d(TAG, "Received non architecture message, dropping...");
    }

    public void remove(String id) {
        if (this.listeners.containsKey(id)) {
            this.listeners.remove(id);
        }
    }
}
