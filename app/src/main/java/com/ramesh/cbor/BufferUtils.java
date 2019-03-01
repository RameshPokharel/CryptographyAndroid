package com.ramesh.cbor;

import java.nio.ByteBuffer;

public class BufferUtils {

    public static byte[] concatBuff(byte[]... buffers) {
        int size = 0;
        for (byte[] b : buffers) {
            size += b.length;
        }
        ByteBuffer combinedBuff = ByteBuffer.allocate(size);

        for (byte[] b : buffers) {
            combinedBuff.put(b);
        }
        return combinedBuff.array();
    }

}
