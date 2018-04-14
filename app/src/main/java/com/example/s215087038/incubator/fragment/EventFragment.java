package com.example.s215087038.incubator.fragment;

import android.app.ProgressDialog;
import android.database.ContentObserver;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

import com.example.s215087038.incubator.BuildConf;
import com.example.s215087038.incubator.R;
import com.zktechnology.android.zkbiosecurity.R;
import com.zktechnology.android.zkbiosecurity.activity.TabActivity;
import com.zktechnology.android.zkbiosecurity.adapter.EventListAdapter;
import com.zktechnology.android.zkbiosecurity.entity.Device;
import me.zhanghai.android.materialprogressbar.BuildConfig;

public class EventFragment extends ZKBaseFragment implements OnItemClickListener, OnItemLongClickListener {
    public static final String TAG = EventFragment.class.getName();
    private AContentObserver contentObserver = null;
    private Device curSelected;
    private EventListAdapter hierarchyAdapter;
    private ListView hierarchyList = null;

    public class AContentObserver extends ContentObserver {
        public AContentObserver(Handler handler) {
            super(handler);
        }

        public void onChange(boolean selfChange) {
            new RefeshTask().execute(new String[]{BuildConf.FLAVOR});
        }
    }

    class RefeshTask extends AsyncTask<String, Integer, Void> {
        public ProgressDialog dlg = null;

        RefeshTask() {
        }

        protected void onPreExecute() {
            super.onPreExecute();
            this.dlg = EventFragment.this.getGenericProgressDialog(R.string.refeshing, R.string.please_wait);
            this.dlg.show();
        }

        protected Void doInBackground(String... params) {
            EventFragment.this.hierarchyAdapter = new EventListAdapter(EventFragment.this.getActivity(), EventFragment.this.curSelected);
            return null;
        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            this.dlg.dismiss();
            EventFragment.this.hierarchyList.setAdapter(EventFragment.this.hierarchyAdapter);
            EventFragment.this.hierarchyAdapter.notifyDataSetChanged();
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.contentObserver = new AContentObserver(new Handler());
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View partView = inflater.inflate(R.layout.fragment_event, container, false);
        this.hierarchyList = (ListView) partView.findViewById(R.id.list);
        return partView;
    }

    public void onResume() {
        super.onResume();
        loadListAndCurrentSelected();
        Log.d("EventFragment", "onResume");
    }

    public void onStart() {
        super.onStart();
    }

    public void onDestroy() {
        super.onDestroy();
        getActivity().getContentResolver().unregisterContentObserver(this.contentObserver);
    }

    private void setCurDevice() {
        TabActivity activity = (TabActivity) getActivity();
        Log.d(TAG, activity.listener + " get device ");
        this.curSelected = activity.listener.getDevice();
    }

    public void loadListAndCurrentSelected() {
        setCurDevice();
        new RefeshTask().execute(new String[]{BuildConf.FLAVOR});
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
    }

    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
        return false;
    }

    public void onEventDoorStatus(Device device, int index, int ret) {
    }
}
