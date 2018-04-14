package com.example.s215087038.incubator.adapter;

import android.content.Context;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.s215087038.incubator.R;
import com.zktechnology.android.zkbiosecurity.R;
import com.zktechnology.android.zkbiosecurity.data.DeviceDao;
import com.zktechnology.android.zkbiosecurity.entity.Device;
import com.zktechnology.android.zkbiosecurity.entity.Door;
import com.zktechnology.android.zkbiosecurity.util.SharedPreferencesManager;
import com.zktechnology.android.zkbiosecurity.util.StringUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeviceListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<Device> data = new ArrayList();
    private Map<Device, List<Integer>> delectMap = new HashMap();
    private List<Integer> delectSelected = new ArrayList();
    private List<Door> doorSelected = new ArrayList();
    private SparseBooleanArray isExpands = new SparseBooleanArray();
    private boolean selected = false;

    class DeviceChildHolder {
        public ImageView checkBox = null;
        public ImageView doorImage = null;
        public TextView pin = null;

        protected DeviceChildHolder() {
        }
    }

    class DeviceHolder {
        public ImageView arrowImage = null;
        public CheckBox checkBox = null;
        public ImageView deviceImage = null;
        public TextView ip = null;
        public TextView name = null;
        public TextView status = null;
        public ImageView statusImage = null;

        protected DeviceHolder() {
        }
    }

    public DeviceListAdapter(Context context) {
        this.context = context;
        loadInfo();
    }

    public List<Device> getDevices() {
        return this.data;
    }

    public void addDevice(Device device) {
        if (!this.data.contains(device)) {
            this.data.add(device);
        }
    }

    public void setDevices(List<Device> devices) {
        for (Device device : devices) {
            if (this.data.contains(device)) {
                this.data.remove(device);
            }
        }
        this.data.addAll(devices);
    }

    private void loadInfo() {
        this.data = DeviceDao.loadDevice(this.context);
        SharedPreferencesManager.loadDoorName(this.context, this.data);
    }

    public int getDevicePosition(Device device) {
        int positon = -1;
        for (int i = 0; i < getGroupCount(); i++) {
            if (this.data.get(i) == device) {
                positon = i;
            }
        }
        return positon;
    }

    public void addDoors(Door door) {
        if (this.doorSelected.contains(door)) {
            this.doorSelected.remove(door);
        } else {
            this.doorSelected.add(door);
        }
    }

    public void removeDevice(Device device) {
        if (this.data.contains(device)) {
            for (Door door : device.getDoors()) {
                removeDoors(door);
            }
            this.data.remove(device);
        }
        notifyDataSetChanged();
    }

    public void removeDoors(Door door) {
        if (this.doorSelected.contains(door)) {
            this.doorSelected.remove(door);
        }
    }

    public List<Door> getDoorList() {
        return this.doorSelected;
    }

    public int getDoorsNum() {
        return this.doorSelected.size();
    }

    public void addDelected(Device device, int position) {
        if (this.delectMap.containsKey(device)) {
            List<Integer> curDelectSelected = (List) this.delectMap.get(device);
            if (curDelectSelected != null) {
                if (curDelectSelected.contains(Integer.valueOf(position))) {
                    curDelectSelected.remove(Integer.valueOf(position));
                } else {
                    curDelectSelected.add(Integer.valueOf(position));
                }
            }
            this.delectMap.put(device, curDelectSelected);
        } else {
            this.delectSelected.clear();
            if (this.delectSelected.contains(Integer.valueOf(position))) {
                this.delectSelected.remove(Integer.valueOf(position));
            } else {
                this.delectSelected.add(Integer.valueOf(position));
            }
            this.delectMap.put(device, this.delectSelected);
        }
        if (((List) this.delectMap.get(device)).isEmpty()) {
            this.delectMap.remove(device);
        }
    }

    public void removeDelected() {
        this.delectSelected.clear();
    }

    public List<Integer> getDeletedList() {
        return this.delectSelected;
    }

    public Map<Device, List<Integer>> getDelectDeviceMap() {
        return this.delectMap;
    }

    public int getDeltectNum(Device device) {
        if (this.delectMap.containsKey(device)) {
            return ((List) this.delectMap.get(device)).size();
        }
        return 0;
    }

    public boolean getSelected() {
        return this.selected;
    }

    public void setSelected() {
        this.selected = !this.selected;
    }

    public int getGroupCount() {
        Log.d("kenvin", "GroupCount() =" + this.data.size());
        return this.data.size();
    }

    public int getChildrenCount(int groupPosition) {
        return ((Device) getGroup(groupPosition)).getDoors().size();
    }

    public Object getGroup(int groupPosition) {
        if (this.data.size() == 0) {
            return null;
        }
        Object object = null;
        try {
            return this.data.get(groupPosition);
        } catch (Exception e) {
            e.printStackTrace();
            return object;
        }
    }

    public Object getChild(int groupPosition, int childPosition) {
        return null;
    }

    public long getGroupId(int groupPosition) {
        return 0;
    }

    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    public boolean hasStableIds() {
        return false;
    }

    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        DeviceHolder holder;
        View ret = convertView;
        if (ret == null) {
            ret = LayoutInflater.from(this.context).inflate(R.layout.list_item_device, null);
            holder = new DeviceHolder();
            holder.name = (TextView) ret.findViewById(R.id.list_device_tv);
            holder.ip = (TextView) ret.findViewById(R.id.list_opendoor_name_tv);
            holder.status = (TextView) ret.findViewById(R.id.list_device_status_tv);
            holder.statusImage = (ImageView) ret.findViewById(R.id.list_device_status_image);
            holder.deviceImage = (ImageView) ret.findViewById(R.id.list_device_image_tv);
            holder.arrowImage = (ImageView) ret.findViewById(R.id.exlist_group_arrow);
            ret.setTag(R.id.list_device_tv, Integer.valueOf(groupPosition));
            ret.setTag(R.id.list_opendoor_name_tv, Integer.valueOf(-1));
            ret.setTag(holder);
        } else {
            holder = (DeviceHolder) ret.getTag();
        }
        Device current = (Device) getGroup(groupPosition);
        holder.name.setText(current.getDeviceName());
        holder.ip.setText(current.getDeviceIp());
        if (current.isConnect()) {
            holder.deviceImage.setImageResource(R.drawable.ic_device);
            holder.statusImage.setImageResource(R.drawable.ic_online);
            holder.status.setText(R.string.device_status_on);
        } else {
            holder.deviceImage.setImageResource(R.drawable.ic_device_no);
            holder.statusImage.setImageResource(R.drawable.ic_online_no);
            holder.status.setText(R.string.device_status_off);
        }
        if (this.isExpands.get(groupPosition)) {
            if (current.isConnect()) {
                holder.arrowImage.setImageResource(R.drawable.expand_arrow_down);
            } else {
                holder.arrowImage.setImageResource(R.drawable.expand_arrow_normal);
            }
        } else if (current.isConnect()) {
            holder.arrowImage.setImageResource(R.drawable.expand_arrow);
        } else {
            holder.arrowImage.setImageResource(R.drawable.arrow_up);
        }
        return ret;
    }

    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        DeviceChildHolder holder;
        View ret = convertView;
        if (ret == null) {
            ret = LayoutInflater.from(this.context).inflate(R.layout.list_expand_item_device, null);
            holder = new DeviceChildHolder();
            holder.pin = (TextView) ret.findViewById(R.id.list_door_tv);
            holder.doorImage = (ImageView) ret.findViewById(R.id.list_door_image_tv);
            holder.checkBox = (ImageView) ret.findViewById(R.id.list_door_btnCheck);
            ret.setTag(holder);
        } else {
            holder = (DeviceChildHolder) ret.getTag();
        }
        Device current = (Device) getGroup(groupPosition);
        if (current != null) {
            Door door = (Door) current.getDoors().get(childPosition);
            holder.pin.setText(door.getDoorPinString());
            if (!StringUtil.isEmpty(door.getDoorState())) {
                if (door.getDoorState().equals(Door.NO_MAGNETOMETER) || door.getDoorState().equals(Door.DOOR_CLOSED)) {
                    holder.doorImage.setImageResource(R.drawable.ic_door_no);
                } else if (door.getDoorState().equals(Door.DOOR_OPENED)) {
                    holder.doorImage.setImageResource(R.drawable.ic_door);
                }
            }
            holder.checkBox.setVisibility(0);
            if (this.doorSelected != null) {
                if (this.doorSelected.contains(door)) {
                    holder.checkBox.setBackgroundResource(R.drawable.drw_check_bg);
                } else {
                    holder.checkBox.setBackgroundResource(R.drawable.drw_checkbox_off);
                }
            }
        }
        return ret;
    }

    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public void onGroupCollapsed(int groupPosition) {
        this.isExpands.put(groupPosition, false);
        super.onGroupCollapsed(groupPosition);
    }

    public void onGroupExpanded(int groupPosition) {
        this.isExpands.put(groupPosition, true);
        super.onGroupExpanded(groupPosition);
    }
}
