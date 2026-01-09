package com.monolux.domain.exceptions;

import lombok.extern.slf4j.Slf4j;

import javax.persistence.EntityExistsException;

@Slf4j
public class DomainEntityExistsException extends EntityExistsException {
    // region ▒▒▒▒▒ Constructors ▒▒▒▒▒

    public DomainEntityExistsException() {
        super();
    }

    public DomainEntityExistsException(final String message) {
        super(message);
    }

    public DomainEntityExistsException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public DomainEntityExistsException(final Throwable cause) {
        super(cause);
    }

    // endregion
}