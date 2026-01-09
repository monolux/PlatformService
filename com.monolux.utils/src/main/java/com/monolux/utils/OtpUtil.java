package com.monolux.utils;

import de.taimos.totp.TOTP;
import org.apache.commons.codec.binary.Base32;
import org.apache.commons.codec.binary.Hex;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

public class OtpUtil {
    // region ▒▒▒▒▒ Constants ▒▒▒▒▒

    private static final int BUFFER_SIZE = 20;

    // endregion

    // region ▒▒▒▒▒ User Define Methods ▒▒▒▒▒

    public static String createOtpSecret() {
        byte[] bytes = new byte[OtpUtil.BUFFER_SIZE];
        new SecureRandom().nextBytes(bytes);
        return new Base32().encodeToString(bytes);
    }

    public static String createOtpSecretUrl(final String issuer, final String account, final String otpSecret) {
        String result = "otpauth://totp/"
                + URLEncoder.encode(issuer + ":" + account, StandardCharsets.UTF_8)
                + "?secret=" + URLEncoder.encode(otpSecret, StandardCharsets.UTF_8)
                + "&issuer=" + URLEncoder.encode(issuer, StandardCharsets.UTF_8);
        return result.replace("+", "%20");
    }

    public static String getOtpValue(final String otpSecret) {
        if (otpSecret != null) {
            return TOTP.getOTP(Hex.encodeHexString(new Base32().decode(otpSecret)));
        } else {
            return null;
        }
    }

    public static boolean checkOtpValue(final String otpSecret, final String otpValue) {
        if (otpSecret != null && otpValue != null) {
            String otpValueFromSecret = OtpUtil.getOtpValue(otpSecret);
            return otpValueFromSecret.equals(otpValue);
        }

        return false;
    }

    // endregion
}