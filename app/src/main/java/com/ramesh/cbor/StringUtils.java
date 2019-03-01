package com.ramesh.cbor;

import android.util.Base64;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

public class StringUtils {

    //converting to Hex String
    public static String toHex(String arg) {
        return String.format("%x", new BigInteger(1, arg.getBytes(/*YOUR_CHARSET?*/)));
    }


    public static String byteArrayToHexString(byte[] num) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < num.length; i++) {
            char[] hexDigits = new char[2];
            hexDigits[0] = Character.forDigit((num[i] >> 4) & 0xF, 16);
            hexDigits[1] = Character.forDigit((num[i] & 0xF), 16);
            sb.append(new String(hexDigits));
        }

        return sb.toString();
    }

    public static String hexStringToString(String stirng) {
        byte[] b = hexStringToByteArray(stirng);
        return byteToString(b);
    }

    public static String byteToString(byte[] bytes) {
        return new String(bytes, StandardCharsets.UTF_8);
    }

    public static byte[] hexStringToByteArray(String hex) {
        byte[] bts = new byte[hex.length() / 2];
        for (int i = 0; i < bts.length; i++) {
            bts[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }
        return bts;
    }

    public static RandomByte randomId(int len) {
        char[] text = new char[len];
        String possible = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        for (int i = 0; i < len; i++)
            text[i] = possible.charAt((int) Math.floor(Math.random() * possible.length()));
        String randomString = String.valueOf(text);
        SecureRandom random = new SecureRandom();
        byte bytes[] = randomString.getBytes();
        random.nextBytes(bytes);
        String s = android.util.Base64.encodeToString(bytes, Base64.CRLF | Base64.NO_CLOSE | Base64.NO_PADDING | Base64.NO_WRAP);
        return new RandomByte(random, s);

    }

/*
    public static SecureRandom randomId(int len) {
        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[len];
        random.nextBytes(bytes);
        String s=android.util.Base64.encodeToString(bytes, Base64.URL_SAFE);

        return random;
    }
*/

}
