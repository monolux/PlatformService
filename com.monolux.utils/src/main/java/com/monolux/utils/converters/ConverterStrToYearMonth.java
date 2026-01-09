package com.monolux.utils.converters;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;

import java.time.YearMonth;

@Slf4j
public class ConverterStrToYearMonth implements Converter<String, YearMonth> {
    // region ▒▒▒▒▒ Interface Implements ▒▒▒▒▒

    @Override
    public YearMonth convert(final String source) {
        return ConverterDefault.convertYearMonth(source);
    }

    // endregion
}