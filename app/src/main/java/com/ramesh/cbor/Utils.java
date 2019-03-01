package com.ramesh.cbor;

import android.util.Base64;
import android.util.Log;
import co.nstant.in.cbor.CborBuilder;
import co.nstant.in.cbor.CborDecoder;
import co.nstant.in.cbor.CborEncoder;
import co.nstant.in.cbor.model.DataItem;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.Signature;
import java.util.Arrays;
import java.util.List;

public class Utils {



    public static void log(String value) {

        Log.d("UTILS", value);
    }

    public static byte[] Base64Encode(String data) {
        byte[] clientDataJSONBase64 = Base64.encode(data.getBytes(), Base64.URL_SAFE);
        return clientDataJSONBase64;

    }

    public static byte[] Base64Encode(byte[] data) {
        byte[] clientDataJSONBase64 = Base64.encode(data, Base64.URL_SAFE);
        return clientDataJSONBase64;

    }

    public static byte[] Base64Decode(byte[] data) {
        byte[] clientData = Base64.decode(data, Base64.CRLF | Base64.NO_CLOSE | Base64.NO_PADDING | Base64.NO_WRAP);
        return clientData;
    }

    public static byte[] Base64Decode(String data) {
        byte[] clientData = Base64.decode(data, Base64.CRLF | Base64.NO_CLOSE | Base64.NO_PADDING | Base64.NO_WRAP);
        return clientData;

    }

    public static List<DataItem> CBORDecoder(byte[] data/*CBOR encodedBytes*/) throws Exception {
        List<DataItem> decodeByte = CborDecoder.decode(data);
        return decodeByte;

    }




    public static void check() throws Exception {
        JSONObject obj = new JSONObject();
        obj.put("name", "Acme");

        ByteArrayOutputStream b = new ByteArrayOutputStream();

        new CborEncoder(b).encode(
                new CborBuilder()
                        .add(obj.toString())
                        .build()
        );
        byte[] authDataStructEncoded = b.toByteArray();
        Log.d("Encodec ", "" + Arrays.toString(authDataStructEncoded));// [111, 123, 34, 110, 97, 109, 101, 34, 58, 34, 65, 99, 109, 101, 34, 125]

        List<DataItem> decodeByte = CborDecoder.decode(authDataStructEncoded);

        Log.d("DecodeByte", "" + decodeByte);//[{"name":"Acme"}]

    }

    public static void cborEcoding() throws Exception {


        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        new CborEncoder(baos).encode(
                new CborBuilder()
                        .addMap()
                        .put(1, 2)
                        .put(3, -7)
                        .put(-1, 1)
                        .put(-2, "")
                        .put(-3, "")
                        .end()
                        .build()
        );

        byte[] encodedBytes = baos.toByteArray();

    }


}
