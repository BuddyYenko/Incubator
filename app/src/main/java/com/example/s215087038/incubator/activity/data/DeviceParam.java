package com.example.s215087038.incubator.activity.data;

public class DeviceParam {
    public static int DEVICE_CONNECT_CODE = 0;
    public static String Device_P2P_ADRESS = "protocol=P4P,Address=PPCS-013051-FLLXN";
    public static String Device_PARAM = "LockCount";
    private String Protocol;
    private String ipAddress;
    private String passWd;
    private String timeOut;

    public String getProtocol() {
        return this.Protocol;
    }

    public void setProtocol(String protocol) {
        this.Protocol = protocol;
    }

    public String getIpAddress() {
        return this.ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getTimeOut() {
        return this.timeOut;
    }

    public void setTimeOut(String timeOut) {
        this.timeOut = timeOut;
    }

    public String getPassWd() {
        return this.passWd;
    }

    public void setPassWd(String passWd) {
        this.passWd = passWd;
    }
}
