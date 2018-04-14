package com.example.s215087038.incubator.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.s215087038.incubator.R;
import com.zktechnology.android.zkbiosecurity.R;
import com.zktechnology.android.zkbiosecurity.entity.Door;
import java.util.ArrayList;
import java.util.List;

public class DoorResultListAdapter extends BaseAdapter {
    private Context context;
    private List<Door> data = new ArrayList();

    class LanHolder {
        public TextView DeviceName = null;
        public TextView doorName = null;
        public TextView result = null;

        protected LanHolder() {
        }
    }

    public DoorResultListAdapter(Context context, List<Door> data) {
        this.context = context;
        this.data = data;
    }

    public DoorResultListAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<Door> doors) {
        this.data = doors;
    }

    public int getCount() {
        return this.data.size();
    }

    public Object getItem(int pos) {
        if (this.data.size() == 0) {
            return null;
        }
        return this.data.get(pos);
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LanHolder holder;
        View ret = convertView;
        if (ret == null) {
            ret = LayoutInflater.from(this.context).inflate(R.layout.list_item_dialog_door_result, null);
            holder = new LanHolder();
            holder.DeviceName = (TextView) ret.findViewById(R.id.list_dialog_device_tv);
            holder.doorName = (TextView) ret.findViewById(R.id.list_dialog_door_tv);
            holder.result = (TextView) ret.findViewById(R.id.list_dialog_door_result_tv);
            ret.setTag(holder);
        } else {
            holder = (LanHolder) ret.getTag();
        }
        Door current = (Door) getItem(position);
        holder.DeviceName.setText(current.getDevice().getDeviceName());
        holder.doorName.setText(current.getDoorPinString());
        if (current.getOpenResult() >= 0) {
            holder.result.setText(this.context.getResources().getString(R.string.open_success));
            holder.result.setTextColor(this.context.getResources().getColor(R.color.open_door_successful));
        } else {
            holder.result.setText(this.context.getResources().getString(R.string.open_fail));
            holder.result.setTextColor(this.context.getResources().getColor(R.color.open_door_fail));
        }
        return ret;
    }
}
