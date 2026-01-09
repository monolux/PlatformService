package com.monolux.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.regex.Pattern;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ValidationUtil {
    // region ▒▒▒▒▒ Constants ▒▒▒▒▒

    public static final String REGEXP_PASSWORD = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–{}:;',?/*~$^=<>]).{8,20}$";

    public static final String REGEXP_MAIL_ADDRESS = "^(?=.{1,64}@)[\\w-]+(\\.[\\w-]+)*@[^-][A-Za-z\\d-]+(\\.[A-Za-z\\d-]+)*(\\.[A-Za-z]{2,})$";

    // endregion

    // region ▒▒▒▒▒ Methods ▒▒▒▒▒

    public static boolean isValidPassword(final String password) {
        return Pattern.compile(ValidationUtil.REGEXP_PASSWORD)
                .matcher(password)
                .matches();
    }

    public static boolean isValidMailAddress(final String mailAddress) {
        return Pattern.compile(ValidationUtil.REGEXP_MAIL_ADDRESS)
                .matcher(mailAddress)
                .matches();
    }

    public static boolean isValidIpAddress(final String ipAddress) {
        return IpV4Util.isValidIpAddress(ipAddress);
    }

    public static boolean isValidYearMonthDay(final Integer year, final Byte month, final Byte day) {
        try {
            LocalDate date = LocalDate.of(year, month, day);
        } catch (Exception ignore) {
            return false;
        }

        return true;
    }

    // endregion
}