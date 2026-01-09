package com.monolux.utils.converters;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;

import java.time.LocalDateTime;

@Slf4j
public class ConverterStrToLocalDateTime implements Converter<String, LocalDateTime> {
    // region ▒▒▒▒▒ Interface Implements ▒▒▒▒▒

    @Override
    public LocalDateTime convert(@NonNull final String source) {
        return ConverterDefault.convertDateTime(source);
    }

    // endregion
}