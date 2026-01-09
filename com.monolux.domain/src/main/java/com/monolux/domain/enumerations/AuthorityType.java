package com.monolux.domain.enumerations;

import lombok.Getter;

@Getter
public enum AuthorityType {
    // region ▒▒▒▒▒ Enumerations ▒▒▒▒▒

    USER    ("USER", "USER"),

    ADMIN   ("ADMIN", "ADMIN"),

    CLIENT  ("CLIENT", "CLIENT");

    // endregion

    // region ▒▒▒▒▒ Member Variables ▒▒▒▒▒

    private final String code;

    private final String desc;

    // endregion

    // region ▒▒▒▒▒ Constructors ▒▒▒▒▒

    AuthorityType(final String code, final String desc) {
        this.code = code;
        this.desc = desc;
    }

    // endregion
}