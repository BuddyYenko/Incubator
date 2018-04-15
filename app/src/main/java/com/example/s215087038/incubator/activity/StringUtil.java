package com.example.s215087038.incubator.activity;

import java.util.regex.Pattern;

public class StringUtil {
    public static boolean isEmpty(String paramString) {
        return paramString == null || paramString.trim().isEmpty();
    }

    public static boolean isIpAddress(String address) {
        return Pattern.compile("^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\.(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\.(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\.(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$").matcher(address).matches() || Pattern.compile("[a-zA-Z0-9][-a-zA-Z0-9]{0,62}(.[a-zA-Z0-9][-a-zA-Z0-9]{0,62})+.?").matcher(address).matches();
    }
}
