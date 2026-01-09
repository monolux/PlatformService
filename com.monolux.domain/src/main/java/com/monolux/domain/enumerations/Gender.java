package com.monolux.domain.enumerations;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.monolux.domain.exceptions.DomainIllegalArgumentException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Arrays;
import java.util.Locale;

@Slf4j
@Getter
public enum Gender {
    // region ▒▒▒▒▒ Enumerations ▒▒▒▒▒

    Male    ("Male", LocaleContextHolder.getLocale().getLanguage().equals(Locale.KOREAN.getLanguage()) ? "남" : "Male"),

    Female  ("Female", LocaleContextHolder.getLocale().getLanguage().equals(Locale.KOREAN.getLanguage()) ? "여" : "Female");

    // endregion

    // region ▒▒▒▒▒ Member Variables ▒▒▒▒▒

    private final String code;

    private final String desc;

    // endregion

    // region ▒▒▒▒▒ Constructors ▒▒▒▒▒

    Gender(final String code, final String desc) {
        this.code = code;
        this.desc = desc;
    }

    // endregion

    // region ▒▒▒▒▒ Serialization ▒▒▒▒▒

    @JsonCreator
    public static Gender fromValue(final String value) {
        return Arrays.stream(Gender.values())
                .filter(gender -> gender.toValue().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new DomainIllegalArgumentException("value is not valid."));
    }

    @JsonValue
    public String toValue() {
        return this.code;
    }

    // endregion
}