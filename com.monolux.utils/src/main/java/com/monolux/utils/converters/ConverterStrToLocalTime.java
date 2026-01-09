package com.monolux.utils.converters;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;

import java.time.LocalTime;

@Slf4j
public class ConverterStrToLocalTime implements Converter<String, LocalTime> {
    // region ▒▒▒▒▒ Interface Implements ▒▒▒▒▒

    @Override
    public LocalTime convert(@NonNull final String source) {
        return ConverterDefault.convertTime(source);
    }

    // endregion
}