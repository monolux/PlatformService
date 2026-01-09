package com.monolux.domain.exceptions;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DomainRuntimeException extends RuntimeException {
    // region ▒▒▒▒▒ Constructors ▒▒▒▒▒

    public DomainRuntimeException() {
        super();
    }

    public DomainRuntimeException(final String message) {
        super(message);
    }

    public DomainRuntimeException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public DomainRuntimeException(final Throwable cause) {
        super(cause);
    }

    public DomainRuntimeException(final String message,
                                  final Throwable cause,
                                  final boolean enableSuppression,
                                  final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    // endregion
}