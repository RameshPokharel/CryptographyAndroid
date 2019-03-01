package com.ramesh.cbor;

import java.security.SecureRandom;

public class RandomByte {
    SecureRandom random;
    String hexString;

    public RandomByte(SecureRandom random, String hexString) {
        this.random = random;
        this.hexString = hexString;
    }

    public SecureRandom getRandom() {
        return random;
    }

    public void setRandom(SecureRandom random) {
        this.random = random;
    }

    public String getHexString() {
        return hexString;
    }

    public void setHexString(String hexString) {
        this.hexString = hexString;
    }
}
