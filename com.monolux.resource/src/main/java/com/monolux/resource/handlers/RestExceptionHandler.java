package com.monolux.resource.handlers;

import com.monolux.common.web.response.ApiResponse;
import com.monolux.domain.exceptions.DomainEntityExistsException;
import com.monolux.domain.exceptions.DomainEntityNotFoundException;
import com.monolux.domain.exceptions.DomainIllegalArgumentException;
import com.monolux.domain.exceptions.DomainRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

@Slf4j
@Profile({"local", "dev"})
@RestControllerAdvice(basePackages = "com.monolux.resource.controllers")
public class RestExceptionHandler {
    // region ▒▒▒▒▒ Handlers ▒▒▒▒▒

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> exceptionHandling(final MethodArgumentNotValidException ex) {
        String errorMessage = String.format("%s -> %s", Objects.requireNonNull(ex.getFieldError()).getField(), ex.getFieldError().getDefaultMessage());
        log.error(errorMessage, ex);
        return new ResponseEntity<>(ApiResponse.createResponse(errorMessage), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DomainEntityExistsException.class)
    public ResponseEntity<ApiResponse> exceptionHandling(final DomainEntityExistsException domainEntityExistsException) {
        log.error(domainEntityExistsException.getMessage(), domainEntityExistsException);
        return new ResponseEntity<>(ApiResponse.createResponse(domainEntityExistsException.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DomainEntityNotFoundException.class)
    public ResponseEntity<ApiResponse>exceptionHandling(final DomainEntityNotFoundException domainEntityNotFoundException) {
        log.error(domainEntityNotFoundException.getMessage(), domainEntityNotFoundException);
        return new ResponseEntity<>(ApiResponse.createResponse(domainEntityNotFoundException.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DomainIllegalArgumentException.class)
    public ResponseEntity<ApiResponse> exceptionHandling(final DomainIllegalArgumentException domainIllegalArgumentException) {
        log.error(domainIllegalArgumentException.getMessage(), domainIllegalArgumentException);
        return new ResponseEntity<>(ApiResponse.createResponse(domainIllegalArgumentException.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DomainRuntimeException.class)
    public ResponseEntity<ApiResponse>exceptionHandling(final DomainRuntimeException domainRuntimeException) {
        log.error(domainRuntimeException.getMessage(), domainRuntimeException);
        return new ResponseEntity<>(ApiResponse.createResponse(domainRuntimeException.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> exceptionHandling(final Exception ex) {
        log.error(ex.getMessage(), ex);
        return new ResponseEntity<>(ApiResponse.createResponse(ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // endregion
}