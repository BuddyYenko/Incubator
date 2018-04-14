package com.example.s215087038.incubator.event;

import android.os.IBinder;
import android.os.Messenger;
import com.zktechnology.android.zkbiosecurity.handler.EventHandler;

public class EventManager {
    public static final String TAG = EventManager.class.getName();
    private final Messenger messenger = new Messenger(new EventHandler());

    public IBinder getBinder() {
        return this.messenger.getBinder();
    }
}
