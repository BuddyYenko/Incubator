package com.example.s215087038.incubator.activity;

public class UdkService {
    public static int UDK_PROT_DEFAULT = 0;
    public static int UDK_PROT_PULL = 1;
    public static int UDK_PROT_STANDALONE = 0;

    public interface CBInterface {
        void DownloadProcess(int i, int i2);
    }

    public static native int UdkBase64WriteToBMP(long j, String str, byte[] bArr);

    public static native long UdkConnect(int[] iArr, String str);

    public static native int UdkControlDevice(long j, long j2, long j3, long j4, long j5, long j6, String str);

    public static native int UdkDeleteDeviceData(long j, String str, String str2, String str3);

    public static native void UdkDisconnect(long j);

    public static native int UdkGetComPwd(long j, byte[] bArr, int[] iArr);

    public static native int UdkGetDeviceData(long j, byte[] bArr, int i, String str, String str2, String str3, String str4);

    public static native int UdkGetDeviceDataCount(long j, String str, String str2, String str3);

    public static native int UdkGetDeviceFileData(long j, byte[] bArr, int[] iArr, String str, String str2);

    public static native int UdkGetDeviceParam(long j, byte[] bArr, int i, String str);

    public static native int UdkGetLastError(long j);

    public static native int UdkGetLockCount(long j);

    public static native int UdkGetLockState(long j, int i);

    public static native int UdkGetRTLog(long j, byte[] bArr, int i);

    public static native int UdkGetTableStruct(long j, byte[] bArr, int i);

    public static native int UdkLastError(long j);

    public static native int UdkMobileAtt(long j, int i, String str, byte[] bArr, int[] iArr);

    public static native int UdkModifyIPAddress(long j, String str, String str2, byte[] bArr, int i);

    public static native int UdkResetCallBack(long j, CBInterface cBInterface);

    public static native int UdkResetDeviceExt(long j, String str);

    public static native int UdkSearchDevice(long j, String str, String str2, byte[] bArr, int[] iArr);

    public static native int UdkSearchDevices(String str, String str2, byte[] bArr, int[] iArr);

    public static native int UdkSetAppid(long j, String str);

    public static native int UdkSetCallBack(long j, CBInterface cBInterface);

    public static native int UdkSetDeviceData(long j, String str, String str2, String str3);

    public static native int UdkSetDeviceFileData(long j, String str, String str2, int i, String str3);

    public static native int UdkSetDeviceParam(long j, String str);

    public static native int UdkSetLockState(long j, int i, int i2);

    public static native int UdkSetTableStruct(long j, String str);

    static {
        System.loadLibrary("udk");
    }
}
