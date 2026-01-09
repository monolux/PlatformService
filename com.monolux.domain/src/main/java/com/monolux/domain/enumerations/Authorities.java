package com.monolux.domain.enumerations;

import lombok.Getter;

@Getter
public enum Authorities {
    // region ▒▒▒▒▒ Enumerations ▒▒▒▒▒

    ADMIN_SYSTEM            (AuthorityType.ADMIN, "ADMIN_SYSTEM", "ADMIN SYSTEM"),

    ADMIN_MANAGER           (AuthorityType.ADMIN, "ADMIN_MANAGER", "ADMIN MANAGER"),

    ADMIN_OPERATOR          (AuthorityType.ADMIN, "ADMIN_OPERATOR", "ADMIN OPERATOR"),

    ADMIN_PARTNER           (AuthorityType.ADMIN, "ADMIN_PARTNER", "ADMIN PARTNER"),

    USER                    (AuthorityType.USER, "USER", "USER"),

    PLATFORM_SERVICE_CLIENT (AuthorityType.CLIENT, "PLATFORM_SERVICE_CLIENT", "PLATFORM SERVICE CLIENT");

    // endregion

    // region ▒▒▒▒▒ Member Variables ▒▒▒▒▒

    private final AuthorityType authorityType;

    private final String code;

    private final String desc;

    // endregion

    // region ▒▒▒▒▒ Constructors ▒▒▒▒▒

    Authorities(final AuthorityType authorityType, final String code, final String desc) {
        this.authorityType = authorityType;
        this.code = code;
        this.desc = desc;
    }

    // endregion
}