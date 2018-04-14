package com.example.s215087038.incubator.fragment;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.s215087038.incubator.BuildConf;
import com.example.s215087038.incubator.R;
import com.zktechnology.android.zkbiosecurity.R;
import com.zktechnology.android.zkbiosecurity.activity.SettingActivity;
import com.zktechnology.android.zkbiosecurity.activity.TabActivity.GetCurDeviceListener;
import com.zktechnology.android.zkbiosecurity.data.DeviceDao;
import com.zktechnology.android.zkbiosecurity.data.EventDao;
import com.zktechnology.android.zkbiosecurity.entity.Device;
import com.zktechnology.android.zkbiosecurity.entity.Door;
import com.zktechnology.android.zkbiosecurity.list.DeviceList;
import com.zktechnology.android.zkbiosecurity.list.DeviceList.OnDeviceClickedListener;
import com.zktechnology.android.zkbiosecurity.util.StringUtil;
import me.zhanghai.android.materialprogressbar.BuildConfig;

public class DeviceFragment extends ZKBaseFragment implements OnClickListener, GetCurDeviceListener, OnDeviceClickedListener {
    private static final String CLOSE_TIME = "0";
    private static final String OPEN_NORMAL_TIME = "255";
    private static final String OPEN_TIME = "5";
    public static final String TAG = DeviceFragment.class.getName();
    protected ActionBar actionBar = null;
    private DeviceList fragmentList = null;
    private int handler = 0;
    private EditText ipEditText;
    private EditText nameEditText;
    private EditText portEditText;

    class ConnectTask extends AsyncTask<String, Integer, Integer> {
        public ProgressDialog dlg = null;
        private String ipTemp;
        private String nameTemp;
        private String portTemp;

        public ConnectTask(String ipTemp, String portTemp, String nameTemp) {
            this.ipTemp = ipTemp;
            this.portTemp = portTemp;
            this.nameTemp = nameTemp;
        }

        protected void onPreExecute() {
            super.onPreExecute();
            this.dlg = DeviceFragment.this.getGenericProgressDialog(R.string.loading, R.string.please_wait);
            this.dlg.show();
        }

        protected Integer doInBackground(String... params) {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("protocol=TCP,");
            stringBuffer.append("ipaddress=");
            stringBuffer.append(this.ipTemp + ",");
            stringBuffer.append("port=");
            stringBuffer.append(this.portTemp + ",");
            stringBuffer.append("timeout=4000,");
            stringBuffer.append("passwd=");
            return Integer.valueOf(DeviceFragment.this.handler);
        }

        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            this.dlg.dismiss();
            if (result.intValue() > 0) {
                Device device = new Device();
                device.setDeviceName(this.nameTemp);
                device.setiP(this.ipTemp);
                device.setPort(this.portTemp);
                if (DeviceDao.insertOrUpdate(DeviceFragment.this.getActivity(), device)) {
                    Toast.makeText(DeviceFragment.this.getActivity(), DeviceFragment.this.getResources().getString(R.string.add_device_success), 0).show();
                    DeviceFragment.this.fragmentList.moveToNextDevice();
                    DeviceFragment.this.fragmentList.getCurSelected().setHandle((long) result.intValue());
                    DeviceFragment.this.fragmentList.getDeviceListAdapter().notifyDataSetChanged();
                    DeviceFragment.this.deviceManager.addDevice(DeviceFragment.this.fragmentList.getCurSelected());
                    DeviceFragment.this.deviceManager.listenDeviceStatus(DeviceFragment.this.fragmentList.getCurSelected());
                    return;
                }
                return;
            }
            Toast.makeText(DeviceFragment.this.getActivity(), DeviceFragment.this.getResources().getString(R.string.add_device_fail), 0).show();
        }
    }

    class ControlDeviceTask extends AsyncTask<String, Integer, Integer> {
        int controlTime = 0;
        int curInt = 0;

        public ControlDeviceTask(int curInt) {
            this.curInt = curInt;
        }

        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected Integer doInBackground(String... params) {
            this.controlTime = Integer.parseInt(params[0]);
            return null;
        }

        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            if (result.intValue() >= 0) {
                Toast.makeText(DeviceFragment.this.getActivity(), String.format(DeviceFragment.this.getResources().getString(R.string.open_success), new Object[]{String.valueOf(this.curInt)}), 0).show();
                EventDao.insertOrUpdateEvent(DeviceFragment.this.getActivity(), DeviceFragment.this.getDevice());
                return;
            }
            Toast.makeText(DeviceFragment.this.getActivity(), String.format(DeviceFragment.this.getResources().getString(R.string.open_fail), new Object[]{String.valueOf(this.curInt)}), 0).show();
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint({"WrongViewCast"})
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View partView = inflater.inflate(R.layout.fragment_device, container, false);
        this.fragmentList = (DeviceList) partView.findViewById(R.id.generic_list);
        this.fragmentList.setOnDeviceClickedListener(this);
        TextView openView = (TextView) partView.findViewById(R.id.open);
        TextView closeView = (TextView) partView.findViewById(R.id.close);
        ImageButton settingBtn = (ImageButton) partView.findViewById(R.id.btn_setting);
        ((LinearLayout) partView.findViewById(R.id.add_container)).setOnClickListener(this);
        openView.setOnClickListener(this);
        closeView.setOnClickListener(this);
        settingBtn.setOnClickListener(this);
        doHandleView();
        return partView;
    }

    private void doHandleView() {
        this.fragmentList.loadListAndCurrentSelected();
    }

    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
    }

    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        if (this.handler > 0) {
            this.handler = 0;
            this.deviceManager.stop();
        } else {
            this.handler = 0;
            this.deviceManager.stop();
        }
    }

    public void onEmptyHierarchy() {
        showAddDeviceHelp();
    }

    private void showAddDeviceHelp() {
        showWarnAlertDialog(R.string.no_device_found, R.string.add, R.string.exit, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                DeviceFragment.this.launchDeviceWizard();
                dialog.cancel();
            }
        }, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
    }

    protected void launchDeviceWizard() {
        showEditDialog(R.string.dialog_cancel, R.string.dialog_ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                dialog.cancel();
                String nameTemp = DeviceFragment.this.nameEditText.getText().toString();
                String ipTemp = DeviceFragment.this.ipEditText.getText().toString();
                String portTemp = DeviceFragment.this.portEditText.getText().toString();
                if (StringUtil.isEmpty(nameTemp)) {
                    Toast.makeText(DeviceFragment.this.getActivity(), DeviceFragment.this.getResources().getString(R.string.name_empty), 0).show();
                } else if (StringUtil.isEmpty(ipTemp)) {
                    Toast.makeText(DeviceFragment.this.getActivity(), DeviceFragment.this.getResources().getString(R.string.ip_empty), 0).show();
                } else if (StringUtil.isEmpty(portTemp)) {
                    Toast.makeText(DeviceFragment.this.getActivity(), DeviceFragment.this.getResources().getString(R.string.port_empty), 0).show();
                } else {
                    new ConnectTask(ipTemp, portTemp, nameTemp).execute(new String[]{BuildConf.FLAVOR});
                }
            }
        });
    }

    protected void showEditDialog(int pPositiveButtonLabel, int pNegativeButtonLabel, DialogInterface.OnClickListener pOkClickListener, DialogInterface.OnClickListener pCancelClickListener) {
        Builder builder = new Builder(getActivity());
        View rootView = ((LayoutInflater) getActivity().getSystemService("layout_inflater")).inflate(R.layout.dialog_edit_value, null);
        this.nameEditText = (EditText) rootView.findViewById(R.id.value_name_edit);
        this.ipEditText = (EditText) rootView.findViewById(R.id.value_ip_edit);
        this.portEditText = (EditText) rootView.findViewById(R.id.value_port_edit);
        builder.setView(rootView);
        builder.setPositiveButton(pPositiveButtonLabel, pOkClickListener);
        builder.setNegativeButton(pNegativeButtonLabel, pCancelClickListener);
        builder.show();
    }

    public void onClick(View v) {
        int doorCount;
        int i;
        switch (v.getId()) {
            case R.id.add_container /*2131427438*/:
                launchDeviceWizard();
                return;
            case R.id.open /*2131427441*/:
                if (this.fragmentList.getDeviceListAdapter().getDeletedList().isEmpty()) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.select_device), 0).show();
                    return;
                }
                doorCount = this.fragmentList.getDeviceListAdapter().getDeletedList().size();
                for (i = 0; i < doorCount; i++) {
                    new ControlDeviceTask(((Integer) this.fragmentList.getDeviceListAdapter().getDeletedList().get(i)).intValue()).execute(new String[]{OPEN_TIME});
                }
                return;
            case R.id.close /*2131427442*/:
                if (this.fragmentList.getDeviceListAdapter().getDeletedList().isEmpty()) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.select_device), 0).show();
                    return;
                }
                doorCount = this.fragmentList.getDeviceListAdapter().getDeletedList().size();
                for (i = 0; i < doorCount; i++) {
                    new ControlDeviceTask(((Integer) this.fragmentList.getDeviceListAdapter().getDeletedList().get(i)).intValue()).execute(new String[]{CLOSE_TIME});
                }
                return;
            case R.id.btn_setting /*2131427518*/:
                startActivity(new Intent(getActivity(), SettingActivity.class));
                return;
            default:
                return;
        }
    }

    public void onDeviceConnectClicked(int position) {
        reConnectDevice(position);
    }

    public void onDeviceLongClicked(final int position) {
        showWarnAlertDialog(R.string.delete_device, R.string.dialog_ok, R.string.dialog_cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (DeviceDao.deleteDevice(DeviceFragment.this.getActivity(), ((Device) DeviceFragment.this.fragmentList.getDeviceListAdapter().getGroup(position)).getDeviceIp())) {
                    DeviceFragment.this.fragmentList.moveToNextDevice();
                    if (DeviceFragment.this.fragmentList.getDeviceListAdapter().getGroupCount() == 0) {
                        DeviceFragment.this.showAddDeviceHelp();
                    }
                    Toast.makeText(DeviceFragment.this.getActivity(), DeviceFragment.this.getResources().getString(R.string.delete_device_success), 0).show();
                } else {
                    Toast.makeText(DeviceFragment.this.getActivity(), DeviceFragment.this.getResources().getString(R.string.delete_device_fail), 0).show();
                }
                dialog.cancel();
            }
        }, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
    }

    public void onDoorLongClicked(int groupPosition, int childPosition) {
    }

    private void reConnectDevice(int positon) {
        if (this.fragmentList.getCurSelected().getHandle() <= 0) {
            Device currentDevice = (Device) this.fragmentList.getDeviceListAdapter().getGroup(positon);
            new ConnectTask(currentDevice.getiP(), currentDevice.getPort(), currentDevice.getDeviceName()).execute(new String[]{BuildConf.FLAVOR});
        }
    }

    public Device getDevice() {
        return this.fragmentList.getCurSelected();
    }

    public void onEvent(Device device, int ret) {
        super.onEvent(device, ret);
        device.setHandle(0);
        this.fragmentList.getDeviceListAdapter().notifyDataSetChanged();
    }

    public void onDoorClicked(Door door) {
    }

    public void onEventDoorStatus(Device device, int index, int ret) {
    }
}
