package com.monolux.encryption.block;

import java.security.SecureRandom;
import java.util.Date;
import java.util.Objects;

class EncryptionUtil {
    // region ▒▒▒▒▒ Methods ▒▒▒▒▒

    public static String bytesToHex(byte[] bytes) {
        StringBuilder builder = new StringBuilder();

        for (byte b : bytes) {
            builder.append(String.format("%02X", b));
        }

        return builder.toString();
    }

    public static byte[] getRandomBytes(final int size) {
        byte[] result = new byte[size];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(result);
        return result;
    }

    public static String getRandomString(final int length) {
        char[] charSet = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
                'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
                '!', '@', '#', '$', '%', '^', '&', '*', '(', ')', '-', '+' };

        StringBuilder sb = new StringBuilder();
        SecureRandom sr = new SecureRandom();
        sr.setSeed(new Date().getTime());

        for (int i = 0; i < length; i++) {
            sb.append(charSet[sr.nextInt(charSet.length)]);
        }

        return sb.toString();
    }

    public static byte[] getMergeBytes(final byte[] bytes1, final byte[] bytes2) {
        if (bytes1 == null && bytes2 == null) {
            return null;
        } else if (bytes1 != null && bytes2 != null) {
            byte[] result = new byte[bytes1.length + bytes2.length];
            System.arraycopy(bytes1, 0, result, 0, bytes1.length);
            System.arraycopy(bytes2, 0, result, bytes1.length, bytes2.length);
            return result;
        } else {
            return Objects.requireNonNullElse(bytes1, bytes2);
        }
    }

    public static byte[] getSliceBytes(final byte[] bytes, final int startPos, final int length) {
        if (bytes == null || startPos < 0 || length < 0 || bytes.length < startPos + length) {
            return null;
        }

        byte[] result = new byte[length];
        System.arraycopy(bytes, startPos, result, 0, length);
        return result;
    }

    // endregion
}