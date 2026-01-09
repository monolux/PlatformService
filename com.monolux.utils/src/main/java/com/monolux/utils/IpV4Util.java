package com.monolux.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.regex.Pattern;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class IpV4Util {
    // region ▒▒▒▒▒ Constants ▒▒▒▒▒

    public static final String REGEXP_IP_ADDRESS = "^(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)$";

    // endregion

    // region ▒▒▒▒▒ Methods ▒▒▒▒▒

    public static boolean isValidIpAddress(final String ipAddress) {
        if (ipAddress == null) {
            return false;
        }

        if (Pattern.compile(IpV4Util.REGEXP_IP_ADDRESS).matcher(ipAddress).matches()) {
            try {
                return Arrays.stream(ipAddress.split("\\."))
                        .map(Integer::parseInt)
                        .filter(num -> num >= 0 && num <= 255)
                        .count() == 4;
            } catch (NumberFormatException ignore) {
                return false;
            }
        }

        return false;
    }

    public static long ipToLong(final String ipAddress) {
        if (!IpV4Util.isValidIpAddress(ipAddress)) {
            throw new IllegalArgumentException("IP address is not valid.");
        }

        String[] parts = ipAddress.split("\\.");
        long result = 0;

        for (int i = 0; i < 4; i++) {
            result += Long.parseLong(parts[i]) << (24 - (8 * i));
        }

        return result;
    }

    public static String longToIp(final long ipValue) {
        if (ipValue < 0L || ipValue > (2^32 - 1)) {
            throw new IllegalArgumentException("IP address value is not valid.");
        }

        StringBuilder sb = new StringBuilder(15);
        long ipValueCopy = ipValue;

        for (int i = 0; i < 4; i++) {
            sb.insert(0, ipValueCopy & 0xff);

            if (i < 3) {
                sb.insert(0, '.');
            }

            ipValueCopy >>= 8;
        }

        return sb.toString();
    }

    public static String ipWithoutPort(final String ip) {
        if (ip != null && ip.contains(".") && ip.split("[.]").length >= 4) {
            String part1 = ip.split("[.]")[0];
            String part2 = ip.split("[.]")[1];
            String part3 = ip.split("[.]")[2];
            String part4 = ip.split("[.]")[3];

            StringBuilder sb = new StringBuilder();

            char[] arrIp = part4.toCharArray();

            for (char c : arrIp) {
                if (Character.isDigit(c)) {
                    sb.append(c);

                    if (sb.length() >= 3) { break; }
                } else {
                    break;
                }
            }

            part4 = sb.toString();

            String result = String.format("%s.%s.%s.%s", part1, part2, part3, part4);

            if (!IpV4Util.isValidIpAddress(result)) {
                throw new IllegalArgumentException("IP address is not valid.");
            }

            return result;
        }

        return null;
    }

    // endregion
}