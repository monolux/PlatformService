package com.monolux.utils.converters;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;

import java.time.LocalDate;

@Slf4j
public class ConverterStrToLocalDate implements Converter<String, LocalDate> {
    // region ▒▒▒▒▒ Interface Implements ▒▒▒▒▒

    @Override
    public LocalDate convert(@NonNull final String source) {
        return ConverterDefault.convertDate(source);
    }

    // endregion
}