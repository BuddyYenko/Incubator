package com.example.s215087038.incubator.activity.device;

import android.util.Log;

import com.example.s215087038.incubator.activity.BuildConf;
import com.example.s215087038.incubator.activity.UdkService;
import com.example.s215087038.incubator.activity.entity.Device;


public abstract class AbstractDevice {
    private static final String COMMON_ADRESS = "255.255.255.255";
    private static final String COMMON_COMMUNICATION_TYPE_TCP = "TCP";
    private static final String COMMON_COMMUNICATION_TYPE_UDP = "UDP";
    protected static final String COMMON_DEVICE_DEVICE = "Device=";
    protected static final String COMMON_DEVICE_IP = "IP=";
    protected static final String COMMON_DEVICE_TYPE = "Protype=push";
    public static final int REALIZATE_UDK_CORRERT = -100;
    public static final String TAG = AbstractDevice.class.getName();
    protected static final Object am10Lock = new Object();

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

    public int getErrorCode(long handle) {
        return UdkService.UdkGetLastError(handle);
    }

    public void disDonnectDevice(long handle) throws Exception {
        UdkService.UdkDisconnect(handle);
    }

    public int getLockCount(long handle) throws Exception {
        if (handle == 0) {
            return 0;
        }
        int count = UdkService.UdkGetLockCount(handle);
        if (count > 0) {
            return count;
        }
        Log.d(TAG, "device getLockCount fail,return " + count);
        return 0;
    }

    public int getDoorState(long handle, int doorNo) throws Exception {
        if (handle == 0) {
            return REALIZATE_UDK_CORRERT;
        }
        int state = UdkService.UdkGetLockState(handle, doorNo);
        Log.d(TAG, "first listen to door get state  ret " + state);
        return state;
    }

    public int setLockState(Device curDevice, int doorNo, int time) throws Exception {
        long handle = curDevice.getHandle();
        if (handle == 0) {
            return REALIZATE_UDK_CORRERT;
        }
        int ret = UdkService.UdkSetLockState(handle, doorNo, time);
        Log.d(TAG, "first listen to door open state ret = " + ret);
        return ret;
    }

    public int searchDevices() throws Exception {
        int countInLan = UdkService.UdkSearchDevices(COMMON_COMMUNICATION_TYPE_UDP, COMMON_ADRESS, new byte[10240], new int[]{0});
        if (countInLan <= 0) {
            Log.d(TAG, "device SearchDevices fail,return " + countInLan);
        }
        return countInLan;
    }

    public String searchDevicesBuffer() throws Exception {
        String retString = BuildConf.FLAVOR;
        byte[] buffer = new byte[10240];
        int[] bufferlength = new int[]{10240};
        int countInLan = UdkService.UdkSearchDevices(COMMON_COMMUNICATION_TYPE_UDP, COMMON_ADRESS, buffer, bufferlength);
        if (countInLan <= 0) {
            Log.d(TAG, "device SearchDevices fail,return " + countInLan);
            return null;
        }
        int length = bufferlength[0];
        Log.d(TAG, "device SearchDevices length,return " + length);
        for (int i = 0; i < length; i++) {
            retString = retString + ((char) buffer[i]);
        }
        Log.d(TAG, "device SearchDevices success,return " + retString);
        return retString;
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
