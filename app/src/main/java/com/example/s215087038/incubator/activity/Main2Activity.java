package com.example.s215087038.incubator.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.s215087038.incubator.BuildConf;
import com.example.s215087038.incubator.R;

public class Main2Activity extends AppCompatActivity  {

    Button btnOpen;
    String ip, port, password, connectionKey;


    private static final String COMMON_ADRESS = "255.255.255.255";
    private static final String COMMON_COMMUNICATION_TYPE_TCP = "TCP";
    private static final String COMMON_COMMUNICATION_TYPE_UDP = "UDP";
    protected static final String COMMON_DEVICE_DEVICE = "Device=";
    protected static final String COMMON_DEVICE_IP = "IP=";
    protected static final String COMMON_DEVICE_TYPE = "Protype=push";
    public static final int REALIZATE_UDK_CORRERT = -100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        btnOpen = (Button) findViewById(R.id.btnOpen);

        btnOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long ret = 0;
                try {
                    ret = connectDevice(ip, port, password);
                    if (ret != 0) {
                        //this.mDevice.setConnKey(DeviceActivity.this.deviceManager.getConnPwd(ret));
                       connectionKey =  getConnPwd(ret);
                    }
                }
                catch(Exception e){}
            }
        });

    }


    public long connectDevice(String ip, String port, String passWord) throws Exception {
        int[] prot = new int[100];
        Log.d(TAG, "device---start UdkConnect");
        long ret = UdkService.UdkConnect(prot, praConnectBuffer(ip, port, passWord, COMMON_COMMUNICATION_TYPE_TCP));
        Log.d(TAG, "device---end UdkConnect" + ret);
        if (ret != 0) {
            return ret;
        }
        Log.d(TAG, "connectDevice TCP  fail--" + ret);
        ret = UdkService.UdkConnect(prot, praConnectBuffer(ip, port, passWord, COMMON_COMMUNICATION_TYPE_UDP));
        Log.d(TAG, "device---UdkConnect" + ret);
        return ret;
    }

    public String praConnectBuffer(String ipAdress, String port, String passWord, String commType) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("protocol=");
        stringBuffer.append(commType + ",");
        stringBuffer.append("address=");
        stringBuffer.append(ipAdress + ",");
        stringBuffer.append("port=");
        stringBuffer.append(port + ",");
        stringBuffer.append("timeout=4000,");
        stringBuffer.append("passwd=");
        stringBuffer.append(passWord);
        return stringBuffer.toString().trim();
    }
    public String getConnPwd(long handle) {
        byte[] buffer = new byte[10240];
        int[] bufferlength = new int[]{10240};
        String connKey = BuildConf.FLAVOR;
        Log.d(TAG, "device---start UdkGetComPwd");
        int ret = UdkService.UdkGetComPwd(handle, buffer, bufferlength);
        Log.d(TAG, "device---end UdkGetComPwd " + connKey);
        int length = bufferlength[0];
        if (ret >= 0 && length > 0) {
            for (int i = 0; i < length; i++) {
                connKey = connKey + ((char) buffer[i]);
            }
        }
        Log.d(TAG, "device---connKey " + connKey);
        return connKey;
    }
}