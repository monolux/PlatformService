package com.monolux.domain.exceptions;

import lombok.extern.slf4j.Slf4j;

import javax.persistence.EntityNotFoundException;

@Slf4j
public class DomainEntityNotFoundException extends EntityNotFoundException {
    // region ▒▒▒▒▒ Constructors ▒▒▒▒▒

    public DomainEntityNotFoundException() {
        super();
    }

    public DomainEntityNotFoundException(final String message) {
        super(message);
    }

    // endregion
}