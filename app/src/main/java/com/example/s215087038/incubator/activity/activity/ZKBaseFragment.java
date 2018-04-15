package com.example.s215087038.incubator.activity.activity;

import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.s215087038.incubator.R;

public abstract class ZKBaseFragment extends Fragment implements EventListener {
//    public static final String TAG = ZKBaseFragment.class.getName();
//    protected DeviceManager deviceManager = null;
//
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        this.deviceManager = new DeviceManager();
//        this.deviceManager.addEventListen(TAG, this);
//    }
//
//    public void onDestroy() {
//        super.onDestroy();
//        this.deviceManager.removeEventListen(TAG);
//    }
//
//    protected void showWarnAlertDialog(int pMessage, int pPositiveButtonLabel, int pNegativeButtonLabel, OnClickListener pOkClickListener, OnClickListener pCancelClickListener) {
//        Builder builder = new Builder(getActivity());
//        View rootView = ((LayoutInflater) getActivity().getSystemService("layout_inflater")).inflate(R.layout.dialog_text_value, null);
//        ((TextView) rootView.findViewById(R.id.value_text)).setText(pMessage);
//        builder.setView(rootView);
//        builder.setPositiveButton(pPositiveButtonLabel, pOkClickListener);
//        builder.setNegativeButton(pNegativeButtonLabel, pCancelClickListener);
//        builder.show();
//    }
//
//    public ProgressDialog getGenericProgressDialog(int titleId, int messageId) {
//        ProgressDialog pD = new ProgressDialog(getActivity(), R.style.ProgressDialog);
//        pD.setCancelable(true);
//        pD.setIndeterminate(true);
//        pD.setMessage(getString(messageId));
//        return pD;
//    }
//
//    public void onEvent(Device device, int ret) {
//        Toast.makeText(getActivity(), getResources().getString(R.string.no_device_connect), 0).show();
//    }
}
