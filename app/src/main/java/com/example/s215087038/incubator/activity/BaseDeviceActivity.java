package com.example.s215087038.incubator.activity;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnShowListener;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.s215087038.incubator.R;
import com.example.s215087038.incubator.fragment.ZKBaseFragment;
import com.example.s215087038.incubator.device.DeviceManager;

import java.util.EventListener;


public abstract class BaseDeviceActivity extends ZKBaseActivity implements EventListener {
    public static final String TAG = ZKBaseFragment.class.getName();
    protected DeviceManager deviceManager = null;
    protected ResultCode resultCode = null;

    public interface OnOpenListener {
        void OnOpenClickListener(String str);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.deviceManager = new DeviceManager();
        this.deviceManager.addEventListen(TAG, this);
        this.resultCode = new ResultCode(this);
    }

    public void onDestroy() {
        super.onDestroy();
        this.deviceManager.removeEventListen(TAG);
    }

    protected void showWarnAlertDialog(String pMessage) {
        Builder builder = new Builder(this);
        View rootView = ((LayoutInflater) getSystemService("layout_inflater")).inflate(R.layout.dialog_text_value, null);
        builder.setView(rootView);
        final AlertDialog simpleDialog = builder.create();
        ((TextView) rootView.findViewById(R.id.value_text)).setText(pMessage);
        simpleDialog.setCancelable(true);
        ((ImageButton) rootView.findViewById(R.id.ok_dialog_btn)).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                simpleDialog.dismiss();
            }
        });
        simpleDialog.show();
    }

    protected AlertDialog showDeviceWranAlertDialog(int pMessage, int pPositiveButtonLabel, int pNegativeButtonLabel, OnClickListener pOkClickListener, OnClickListener pCancelClickListener) {
        Builder builder = new Builder(this);
        View rootView = ((LayoutInflater) getSystemService("layout_inflater")).inflate(R.layout.dialog_device_warn_value, null);
        builder.setView(rootView);
        AlertDialog simpleDialog = builder.create();
        TextView dialogtext = (TextView) rootView.findViewById(R.id.value_text);
        TextView cancletext = (TextView) rootView.findViewById(R.id.dialog_cancle_tv);
        TextView confirmtext = (TextView) rootView.findViewById(R.id.dialog_confirm_tv);
        cancletext.setText(pNegativeButtonLabel);
        confirmtext.setText(pPositiveButtonLabel);
        dialogtext.setText(pMessage);
        simpleDialog.setCancelable(false);
        cancletext.setOnClickListener(pCancelClickListener);
        confirmtext.setOnClickListener(pOkClickListener);
        return simpleDialog;
    }

    protected AlertDialog showDeleteOrEditAlertDialog(String pMessage, OnClickListener pEditClickListener, OnClickListener pDeleteClickListener) {
        Builder builder = new Builder(this);
        View rootView = ((LayoutInflater) getSystemService("layout_inflater")).inflate(R.layout.dialog_delete_or_edit, null);
        builder.setView(rootView);
        AlertDialog simpleDialog = builder.create();
        TextView editText = (TextView) rootView.findViewById(R.id.edit_tv);
        TextView deleteText = (TextView) rootView.findViewById(R.id.delete_tv);
        ((TextView) rootView.findViewById(R.id.dialog_title_tv)).setText(pMessage);
        simpleDialog.setCancelable(true);
        editText.setOnClickListener(pEditClickListener);
        deleteText.setOnClickListener(pDeleteClickListener);
        return simpleDialog;
    }

    protected AlertDialog showDeviceLoadAlertDialog(int pMessage) {
        View rootView = ((LayoutInflater) getSystemService("layout_inflater")).inflate(R.layout.dialog_device_load_value, null);
        Builder builder = new Builder(this);
        builder.setView(rootView);
        AlertDialog simpleDialog = builder.create();
        ImageView loadingBar = (ImageView) rootView.findViewById(R.id.warn_image_loading);
        ((TextView) rootView.findViewById(R.id.wran_value_text)).setText(pMessage);
        setLoadingAnim(loadingBar);
        simpleDialog.setCancelable(false);
        return simpleDialog;
    }

    protected void showDeviceInformAlertDialog(List<Door> data) {
        View rootView = ((LayoutInflater) getSystemService("layout_inflater")).inflate(R.layout.dialog_device_text_value, null);
        Builder builder = new Builder(this);
        builder.setView(rootView);
        final AlertDialog simpleDialog = builder.create();
        ListView list = (ListView) rootView.findViewById(R.id.dialog_result_list);
        DoorResultListAdapter adapter = new DoorResultListAdapter(this);
        adapter.setData(data);
        list.setAdapter(adapter);
        ((ImageButton) rootView.findViewById(R.id.ok_dialog_btn)).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                simpleDialog.dismiss();
            }
        });
        simpleDialog.show();
    }

    protected void showDeviceListAlertDialog(List<Device> data) {
        View rootView = ((LayoutInflater) getSystemService("layout_inflater")).inflate(R.layout.dialog_device_list, null);
        Builder builder = new Builder(this);
        builder.setView(rootView);
        final AlertDialog simpleDialog = builder.create();
        ListView list = (ListView) rootView.findViewById(R.id.dialog_list);
        ConnectedListAdapter adapter = new ConnectedListAdapter(this);
        adapter.setData(data);
        list.setAdapter(adapter);
        View emptyViewll = rootView.findViewById(R.id.empty_view);
        if (data.isEmpty()) {
            emptyViewll.setVisibility(0);
        } else {
            emptyViewll.setVisibility(8);
        }
        ((ImageButton) rootView.findViewById(R.id.ok_dialog_btn)).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                simpleDialog.dismiss();
            }
        });
        simpleDialog.show();
    }

    protected void showOpenDoorAlertDialog(final OnOpenListener pOkClickListener) {
        View rootView = ((LayoutInflater) getSystemService("layout_inflater")).inflate(R.layout.dialog_device_edit_value, null);
        Builder builder = new Builder(this);
        builder.setView(rootView);
        final AlertDialog simpleDialog = builder.create();
        final EditText timeEdit = (EditText) rootView.findViewById(R.id.value_time_edit);
        timeEdit.setSelection(timeEdit.getText().length());
        ((ImageButton) rootView.findViewById(R.id.open_dialog_btn)).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                String value = timeEdit.getText().toString().trim();
                if (StringUtil.isEmpty(value)) {
                    Toast.makeText(BaseDeviceActivity.this, BaseDeviceActivity.this.getResources().getString(R.string.text_setting_open_time_null), 0).show();
                    return;
                }
                int valueTemp = Integer.parseInt(value);
                if (valueTemp < 1 || valueTemp > 254) {
                    Toast.makeText(BaseDeviceActivity.this, BaseDeviceActivity.this.getResources().getString(R.string.text_setting_open_time_error), 0).show();
                    return;
                }
                pOkClickListener.OnOpenClickListener(value);
                simpleDialog.dismiss();
            }
        });
        simpleDialog.show();
    }

    protected AlertDialog showEditOrResetAlertDialog(Device device, Door door, OnClickListener pEditClickListener, OnClickListener pCancelClickListener, OnDismissListener pDismissListener) {
        Builder builder = new Builder(this);
        View rootView = ((LayoutInflater) getSystemService("layout_inflater")).inflate(R.layout.dialog_edit_or_reset, null);
        builder.setView(rootView);
        AlertDialog simpleDialog = builder.create();
        final EditText doorNameEdit = (EditText) rootView.findViewById(R.id.door_name_edit);
        TextView cancelText = (TextView) rootView.findViewById(R.id.edit_tv);
        TextView sureText = (TextView) rootView.findViewById(R.id.reset_tv);
        simpleDialog.setCancelable(true);
        ((OnClickListenerBase) pEditClickListener).setEditText(doorNameEdit);
        sureText.setOnClickListener(pEditClickListener);
        cancelText.setOnClickListener(pCancelClickListener);
        simpleDialog.setOnDismissListener(pDismissListener);
        simpleDialog.setOnShowListener(new OnShowListener() {
            public void onShow(DialogInterface dialog) {
                ((InputMethodManager) BaseDeviceActivity.this.getSystemService("input_method")).showSoftInput(doorNameEdit, 0);
            }
        });
        return simpleDialog;
    }

    public void onEvent(Device device, int ret) {
        if (device.getHandle() != 0) {
            Log.d(TAG, "onEvent().listen to device------------------------------------offline");
            Toast.makeText(this, device.getDeviceName() + " " + getResources().getString(R.string.no_device_connect), 0).show();
        }
    }

    public void onEventDoorStatus(Device device, int index, int ret) {
    }

    public void backBtnMethod(View view) {
        finish();
    }
}
