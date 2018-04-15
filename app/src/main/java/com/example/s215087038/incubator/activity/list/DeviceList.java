package com.example.s215087038.incubator.activity.list;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;

import com.example.s215087038.incubator.activity.DeviceListAdapter;
import com.example.s215087038.incubator.activity.data.DeviceDao;
import com.example.s215087038.incubator.activity.entity.Device;
import com.example.s215087038.incubator.activity.entity.Door;
import java.util.ArrayList;
import java.util.List;

public class DeviceList extends ExpandableListView implements OnItemClickListener, OnItemLongClickListener, OnItemSelectedListener, OnChildClickListener, OnGroupClickListener {
    private static final String TAG = DeviceList.class.getName();
    private OnDeviceClickedListener callback = null;
    private Context context;
    public Device curSelected;
    private DeviceListAdapter hierarchyAdapter;
    private boolean isDelete = false;
    private int selectedItemPosition = -1;

    public interface OnDeviceClickedListener {
        void onDeviceConnectClicked(int i);

        void onDeviceLongClicked(int i);

        void onDoorClicked(Door door);

        void onDoorLongClicked(int i, int i2);

        void onEmptyHierarchy();
    }

    public DeviceList(Context context) {
        super(context);
        this.context = context;
        initWithContext();
    }

    public DeviceList(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initWithContext();
    }

    private void initWithContext() {
    }

    public List<Device> getDevices() {
        return this.hierarchyAdapter.getDevices();
    }

    public int getSelectedItemPosition() {
        return this.selectedItemPosition;
    }

    public Device getCurSelected() {
        Log.d(TAG, "selectedItemPosition = " + this.selectedItemPosition);
        if (this.hierarchyAdapter.getGroupCount() > 0) {
            this.curSelected = (Device) this.hierarchyAdapter.getGroup(this.selectedItemPosition);
        }
        return this.curSelected;
    }

    public void collapseGroup(Device device) {
        int position = this.hierarchyAdapter.getDevicePosition(device);
        if (position >= 0) {
            collapseGroup(position);
        }
    }

    public Boolean isSameDevice(Device device, boolean isMove) {
        int i = 0;
        while (i < this.hierarchyAdapter.getDevices().size()) {
            if (((Device) this.hierarchyAdapter.getDevices().get(i)).getiP().equals(device.getiP()) && ((Device) this.hierarchyAdapter.getDevices().get(i)).getPort().equals(device.getPort())) {
                this.selectedItemPosition = i;
                if (isMove) {
                    for (Door door : ((Device) this.hierarchyAdapter.getDevices().get(i)).getDoors()) {
                        this.hierarchyAdapter.removeDoors(door);
                    }
                }
                return Boolean.valueOf(true);
            }
            i++;
        }
        return Boolean.valueOf(false);
    }

    public List<Device> getConnectedDevices() {
        List<Device> data = new ArrayList();
        for (Device device : this.hierarchyAdapter.getDevices()) {
            if (device.isConnect()) {
                data.add(device);
            }
        }
        return data;
    }

    public DeviceListAdapter getDeviceListAdapter() {
        return this.hierarchyAdapter;
    }

    public void loadListAndCurrentSelected() {
        setOnItemClickListener(this);
        setOnGroupClickListener(this);
        setOnChildClickListener(this);
        setOnItemLongClickListener(this);
        this.hierarchyAdapter = new DeviceListAdapter(this.context);
        if (this.hierarchyAdapter.getGroupCount() == 0) {
            this.callback.onEmptyHierarchy();
            return;
        }
        this.selectedItemPosition = this.hierarchyAdapter.getGroupCount() - 1;
        this.curSelected = (Device) this.hierarchyAdapter.getGroup(0);
        if (this.hierarchyAdapter.getGroupCount() <= 0 || this.curSelected == null) {
            this.callback.onEmptyHierarchy();
            return;
        }
        setAdapter(this.hierarchyAdapter);
        setChoiceMode(1);
        setItemChecked(0, true);
        setGroupIndicator(null);
    }

    public void moveToNextDevice(Device device) {
        this.hierarchyAdapter.addDevice(device);
        int employeeNum = this.hierarchyAdapter.getGroupCount();
        if (employeeNum > 0) {
            this.selectedItemPosition = employeeNum - 1;
            if (this.selectedItemPosition <= -1 || this.selectedItemPosition > employeeNum - 1) {
                this.selectedItemPosition = 0;
            }
            this.curSelected = (Device) this.hierarchyAdapter.getGroup(this.selectedItemPosition);
        } else {
            this.curSelected = null;
        }
        setAdapter(this.hierarchyAdapter);
        setGroupIndicator(null);
    }

    public void moveToNextDevice() {
        setOnItemClickListener(this);
        setOnGroupClickListener(this);
        setOnChildClickListener(this);
        setOnItemLongClickListener(this);
        setOnItemSelectedListener(this);
        this.hierarchyAdapter = new DeviceListAdapter(this.context);
        int employeeNum = this.hierarchyAdapter.getGroupCount();
        if (employeeNum > 0) {
            this.selectedItemPosition = employeeNum - 1;
            if (this.selectedItemPosition <= -1 || this.selectedItemPosition > employeeNum - 1) {
                this.selectedItemPosition = 0;
            }
            this.curSelected = (Device) this.hierarchyAdapter.getGroup(this.selectedItemPosition);
        } else {
            this.curSelected = null;
        }
        setAdapter(this.hierarchyAdapter);
        setGroupIndicator(null);
    }

    public void setDelete(boolean isDetele) {
        this.isDelete = isDetele;
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Log.d(TAG, "onItemClick");
    }

    public void setOnDeviceClickedListener(OnDeviceClickedListener callback) {
        this.callback = callback;
    }

    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d(TAG, "------------------------------->onItemLongClick:" + position + ',' + id);
        int groupPosition = 0;
        long packPos;
        int childPosition;
        if (ExpandableListView.getPackedPositionType(id) == 0) {
            packPos = ((ExpandableListView) parent).getExpandableListPosition(position);
            groupPosition = ExpandableListView.getPackedPositionGroup(packPos);
            childPosition = ExpandableListView.getPackedPositionChild(packPos);
            Log.d(TAG, "------------------------------->" + groupPosition);
            Log.d(TAG, "--------------------------------------->" + childPosition);
            this.callback.onDeviceLongClicked(groupPosition);
        } else if (ExpandableListView.getPackedPositionType(id) == 1) {
            packPos = ((ExpandableListView) parent).getExpandableListPosition(position);
            groupPosition = ExpandableListView.getPackedPositionGroup(packPos);
            childPosition = ExpandableListView.getPackedPositionChild(packPos);
            Log.d(TAG, "------------------------------->" + groupPosition);
            Log.d(TAG, "--------------------------------------->" + childPosition);
            this.callback.onDoorLongClicked(groupPosition, childPosition);
        }
        this.selectedItemPosition = groupPosition;
        this.isDelete = true;
        return false;
    }

    public long getSelectDeviceHandle(String ip) {
        Device temp = DeviceDao.selectedDevice(this.context, ip);
        if (temp != null) {
            return temp.getHandle();
        }
        return 0;
    }

    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        this.callback.onDoorClicked((Door) ((Device) this.hierarchyAdapter.getGroup(groupPosition)).getDoors().get(childPosition));
        return true;
    }

    public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
        Log.d(TAG, "onGroupClick " + groupPosition);
        if (this.isDelete) {
            return true;
        }
        this.selectedItemPosition = groupPosition;
        this.callback.onDeviceConnectClicked(groupPosition);
        if (this.curSelected.isConnect()) {
            return false;
        }
        return true;
    }

    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        Log.d(TAG, "onItemSelected " + position);
        this.selectedItemPosition = position;
        this.callback.onDeviceLongClicked(position);
        this.isDelete = true;
    }

    public void onNothingSelected(AdapterView<?> adapterView) {
    }
}
