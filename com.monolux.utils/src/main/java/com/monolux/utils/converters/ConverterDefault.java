package com.monolux.utils.converters;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

import static java.time.format.DateTimeFormatter.ofPattern;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ConverterDefault {
    // region ▒▒▒▒▒ Constants ▒▒▒▒▒

    public static final DateTimeFormatter DATE_FORMAT       = ofPattern("yyyy-MM-dd");

    public static final DateTimeFormatter TIME_FORMAT       = ofPattern("HH:mm:ss");

    public static final DateTimeFormatter DATE_TIME_FORMAT  = ofPattern("yyyy-MM-dd HH:mm:ss");

    public static final DateTimeFormatter YEAR_MONTH_FORMAT = ofPattern("yyyy-MM");

    // endregion

    // region ▒▒▒▒▒ Methods ▒▒▒▒▒

    public static LocalDate convertDate(final String date) {
        if (date == null) return null;
        return LocalDate.parse(date, ConverterDefault.DATE_FORMAT);
    }

    public static LocalTime convertTime(final String time) {
        if (time == null) return null;
        return LocalTime.parse(time, ConverterDefault.TIME_FORMAT);
    }

    public static LocalDateTime convertDateTime(final String dateTime) {
        if (dateTime == null) return null;
        return LocalDateTime.parse(dateTime, ConverterDefault.DATE_TIME_FORMAT);
    }

    public static YearMonth convertYearMonth(final String date) {
        if (!StringUtils.hasText(date)) return null;
        return YearMonth.parse(date, ConverterDefault.YEAR_MONTH_FORMAT);
    }

    public static String convertDate(final LocalDate localDate) {
        if (localDate == null) return null;
        return localDate.format(ConverterDefault.DATE_FORMAT);
    }

    public static String convertTime(final LocalTime localTime) {
        if (localTime == null) return null;
        return localTime.format(ConverterDefault.TIME_FORMAT);
    }

    public static String convertDateTime(final LocalDateTime localDateTime) {
        if (localDateTime == null) return null;
        return localDateTime.format(ConverterDefault.DATE_TIME_FORMAT);
    }

    public static String convertYearMonth(final YearMonth yearMonth) {
        if (yearMonth == null) return null;
        return yearMonth.format(ConverterDefault.YEAR_MONTH_FORMAT);
    }

    // endregion
}