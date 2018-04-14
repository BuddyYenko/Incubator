package com.example.s215087038.incubator.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.s215087038.incubator.R;
import com.zktechnology.android.zkbiosecurity.R;
import com.zktechnology.android.zkbiosecurity.entity.Device;
import java.util.ArrayList;
import java.util.List;

public class LanDeviceListAdapter extends BaseAdapter {
    private List<Integer> Selected = new ArrayList();
    private Context context;
    private List<Device> data = new ArrayList();

    class LanHolder {
        public TextView lanDeviceName = null;
        public ImageView lanImage = null;
        public TextView lanIp = null;

        protected LanHolder() {
        }
    }

    public LanDeviceListAdapter(Context context, List<Device> data) {
        this.context = context;
        this.data = data;
    }

    public LanDeviceListAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<Device> devices) {
        this.data = devices;
    }

    public void addSelected(int position) {
        if (this.Selected == null) {
            return;
        }
        if (this.Selected.contains(Integer.valueOf(position))) {
            this.Selected.remove(Integer.valueOf(position));
        } else {
            this.Selected.add(Integer.valueOf(position));
        }
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
            ret = LayoutInflater.from(this.context).inflate(R.layout.list_item_lan_device, null);
            holder = new LanHolder();
            holder.lanDeviceName = (TextView) ret.findViewById(R.id.list_lan_name_tv);
            holder.lanIp = (TextView) ret.findViewById(R.id.list_lan_ip_tv);
            holder.lanImage = (ImageView) ret.findViewById(R.id.list_lan_btnCheck);
            ret.setTag(holder);
        } else {
            holder = (LanHolder) ret.getTag();
        }
        Device current = (Device) getItem(position);
        holder.lanDeviceName.setText(current.getDeviceName());
        holder.lanIp.setText(current.getiP());
        return ret;
    }
}
