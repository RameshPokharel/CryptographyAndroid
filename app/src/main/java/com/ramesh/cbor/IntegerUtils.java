package com.ramesh.cbor;

import java.nio.ByteBuffer;

public class IntegerUtils {

    public static byte[] toByteArray(int value) {
        return ByteBuffer.allocate(4).putInt(value).array();
    }
    public static byte[] toByteArray(short value) {
        return ByteBuffer.allocate(2).putShort(value).array();
    }

/*
    byte[] toByteArray(int value) {
        return new byte[] {
                (byte)(value >> 24),
                (byte)(value >> 16),
                (byte)(value >> 8),
                (byte)value };
    }
*/

    public static int fromByteArrayInt(byte[] bytes) {
        return ByteBuffer.wrap(bytes).getInt();
    }
    public static int fromByteArrayShort(byte[] bytes) {
        return ByteBuffer.wrap(bytes).getShort();
    }
    // packing an array of 4 bytes to an int, big endian
/*
    int fromByteArray(byte[] bytes) {
        return bytes[0] << 24 | (bytes[1] & 0xFF) << 16 | (bytes[2] & 0xFF) << 8 | (bytes[3] & 0xFF);
    }
*/
}
