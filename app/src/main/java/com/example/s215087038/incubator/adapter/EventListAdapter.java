package com.example.s215087038.incubator.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.s215087038.incubator.BuildConf;
import com.example.s215087038.incubator.R;
import com.zktechnology.android.zkbiosecurity.R;
import com.zktechnology.android.zkbiosecurity.data.EventDao;
import com.zktechnology.android.zkbiosecurity.data.EventParms;
import com.zktechnology.android.zkbiosecurity.entity.Device;
import com.zktechnology.android.zkbiosecurity.entity.Event;
import java.util.List;
import me.zhanghai.android.materialprogressbar.BuildConfig;

public class EventListAdapter extends BaseAdapter {
    private Context context;
    private Device curDevice = null;
    private List<Event> data = null;

    class EventHolder {
        public ImageView eventImage = null;
        public TextView eventName = null;
        public TextView num = null;
        public TextView pin = null;
        public TextView time = null;

        protected EventHolder() {
        }
    }

    public EventListAdapter(Context context) {
        this.context = context;
        loadInfo();
    }

    public EventListAdapter(Context context, Device device) {
        this.context = context;
        this.curDevice = device;
        loadInfo();
    }

    private void loadInfo() {
        this.data = EventDao.loadEvent(this.context, this.curDevice);
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
        EventHolder holder;
        View ret = convertView;
        if (ret == null) {
            ret = LayoutInflater.from(this.context).inflate(R.layout.list_item_event, null);
            holder = new EventHolder();
            holder.eventName = (TextView) ret.findViewById(R.id.list_event_name_tv);
            holder.pin = (TextView) ret.findViewById(R.id.list_event_pin_tv);
            holder.eventImage = (ImageView) ret.findViewById(R.id.list_event_image_tv);
            holder.num = (TextView) ret.findViewById(R.id.list_event_number_tv);
            holder.time = (TextView) ret.findViewById(R.id.list_event_time_tv);
            ret.setTag(holder);
        } else {
            holder = (EventHolder) ret.getTag();
        }
        Event current = (Event) getItem(position);
        if (holder.eventName.equals(EventParms.EVENT_OPEN_UNABLE)) {
            holder.eventName.setText(String.format(this.context.getResources().getString(R.string.text_event_description_info), new Object[]{this.context.getResources().getString(R.string.text_event_description_unable)}));
        } else if (holder.eventName.equals(EventParms.EVENT_OPEN_ALWAYS)) {
            holder.eventName.setText(String.format(this.context.getResources().getString(R.string.text_event_description_info), new Object[]{this.context.getResources().getString(R.string.text_event_always_open)}));
        } else if (holder.eventName.equals(EventParms.EVENT_OPEN_ROMOTE)) {
            holder.eventName.setText(String.format(this.context.getResources().getString(R.string.text_event_description_info), new Object[]{this.context.getResources().getString(R.string.text_event_remote_open)}));
        } else if (holder.eventName.equals(EventParms.EVENT_CLOSE_ROMOTE)) {
            holder.eventName.setText(String.format(this.context.getResources().getString(R.string.text_event_description_info), new Object[]{this.context.getResources().getString(R.string.text_event_remote_close)}));
        } else {
            holder.eventName.setText(String.format(this.context.getResources().getString(R.string.text_event_description_info), new Object[]{BuildConf.FLAVOR}));
        }
        holder.pin.setText(String.format(this.context.getResources().getString(R.string.text_event_pin), new Object[]{current.getPin()}));
        holder.num.setText(current.getNumber());
        holder.time.setText(current.getEventTime());
        return ret;
    }
}
