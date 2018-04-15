package com.example.s215087038.incubator.activity.entity;

import android.content.Context;
import android.util.Log;

import com.example.s215087038.incubator.activity.UdkService;


public class ResultCode {
    private static final int COMMAND_EXECUTION_ERROR = -12;
    private static final int COMMAND_FAILED = -1;
    private static final int COMMAND_NOT_RESPOND = -2;
    private static final int COMMAND_NO_COMMAND = -13;
    public static final int COMM_INIT_FAILED = -203;
    private static final int DATA_ERROR = -10;
    private static final int DATA_PARAMETER_ERROR = -11;
    private static final int NEED_CACHE = -3;
    public static final int PASSWORD_COMMUNICATION_ERROR = -14;
    private String TAG = ResultCode.class.getName();
    private Context context;

    public ResultCode(Context context) {
        this.context = context;
    }

    public String pairCode(String deviceName, long ret) {
        int currentRet = 0;
        if (ret != 0) {
            return deviceName + " " + this.context.getResources().getString(com.example.s215087038.incubator.activity.R.string.add_device_success);
        }
        String result;
        if (ret == 0) {
            currentRet = UdkService.UdkGetLastError(ret);
        }
        Log.d(this.TAG, "error =" + currentRet);
        String failString = deviceName;
        switch (currentRet) {
            case COMM_INIT_FAILED /*-203*/:
                result = failString + " " + this.context.getResources().getString(com.example.s215087038.incubator.activity.R.string.communication_initialization_failed);
                break;
            case COMMAND_NO_COMMAND /*-13*/:
                result = failString + " " + this.context.getResources().getString(com.example.s215087038.incubator.activity.R.string.command_error_no_this_command);
                break;
            case COMMAND_EXECUTION_ERROR /*-12*/:
                result = failString + " " + this.context.getResources().getString(com.example.s215087038.incubator.activity.R.string.command_execution_error);
                break;
            case DATA_PARAMETER_ERROR /*-11*/:
                result = failString + " " + this.context.getResources().getString(com.example.s215087038.incubator.activity.R.string.data_parameter_error);
                break;
            case DATA_ERROR /*-10*/:
                result = failString + " " + this.context.getResources().getString(com.example.s215087038.incubator.activity.R.string.data_error_cannot_resolve);
                break;
            case NEED_CACHE /*-3*/:
                result = failString + " " + this.context.getResources().getString(com.example.s215087038.incubator.activity.R.string.need_cache);
                break;
            case COMMAND_NOT_RESPOND /*-2*/:
                result = failString + " " + this.context.getResources().getString(com.example.s215087038.incubator.activity.R.string.command_no_respond);
                break;
            case COMMAND_FAILED /*-1*/:
                result = failString + " " + this.context.getResources().getString(com.example.s215087038.incubator.activity.R.string.command_failed);
                break;
            default:
                result = failString + " " + this.context.getResources().getString(com.example.s215087038.incubator.activity.R.string.add_device_fail);
                break;
        }
        return result;
    }
}
