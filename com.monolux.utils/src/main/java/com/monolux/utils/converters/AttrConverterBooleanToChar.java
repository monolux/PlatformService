package com.monolux.utils.converters;

import lombok.extern.slf4j.Slf4j;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Slf4j
@Converter
public class AttrConverterBooleanToChar implements AttributeConverter<Boolean, Character> {
    // region ▒▒▒▒▒ Constants ▒▒▒▒▒

    private final static Character Y = 'Y';
    private final static Character N = 'N';

    // endregion

    // region ▒▒▒▒▒ Interface Implements ▒▒▒▒▒

    @Override
    public Character convertToDatabaseColumn(final Boolean attribute) {
        return attribute != null && attribute ? AttrConverterBooleanToChar.Y : AttrConverterBooleanToChar.N;
    }

    @Override
    public Boolean convertToEntityAttribute(final Character dbData) {
        return AttrConverterBooleanToChar.Y.equals(dbData);
    }

    // endregion
}