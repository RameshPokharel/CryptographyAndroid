package com.ramesh.cbor;

import android.util.Log;

public class Utils {


    public static String byteToHexString(byte[] num) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < num.length; i++) {
            char[] hexDigits = new char[2];
            hexDigits[0] = Character.forDigit((num[i] >> 4) & 0xF, 16);
            hexDigits[1] = Character.forDigit((num[i] & 0xF), 16);
            sb.append(new String(hexDigits));
        }

        return sb.toString();
    }

    public static void log(String value) {

        Log.d("UTILS", value);
    }



}
