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

public class ConnectedListAdapter extends BaseAdapter {
    private Context context;
    private List<Device> data = new ArrayList();

    private class LanHolder {
        public TextView lanDeviceName = null;
        public ImageView lanImage = null;
        public TextView lanIp = null;

        protected LanHolder() {
        }
    }

    public ConnectedListAdapter(Context context, List<Device> data) {
        this.context = context;
        this.data = data;
    }

    public ConnectedListAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<Device> devices) {
        this.data = devices;
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
            ret = LayoutInflater.from(this.context).inflate(R.layout.list_item_connected_device, null);
            holder = new LanHolder();
            holder.lanDeviceName = (TextView) ret.findViewById(R.id.list_lan_name_tv);
            holder.lanIp = (TextView) ret.findViewById(R.id.list_lan_ip_tv);
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
