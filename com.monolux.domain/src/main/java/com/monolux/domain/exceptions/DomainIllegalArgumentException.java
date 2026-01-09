package com.monolux.domain.exceptions;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DomainIllegalArgumentException extends IllegalArgumentException {
    // region ▒▒▒▒▒ Constructors ▒▒▒▒▒

    public DomainIllegalArgumentException() {
        super();
    }

    public DomainIllegalArgumentException(final String s) {
        super(s);
    }

    public DomainIllegalArgumentException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public DomainIllegalArgumentException(final Throwable cause) {
        super(cause);
    }

    // endregion
}