package com.monolux.common.web.response;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

@Slf4j
@Getter
public class ApiResponse {
    // region ▒▒▒▒▒ Member Variables ▒▒▒▒▒

    private final String message;

    // endregion

    // region ▒▒▒▒▒ Constructors ▒▒▒▒▒

    protected ApiResponse(final String message) {
        this.message = message;
    }

    // endregion

    // region ▒▒▒▒▒ Methods ▒▒▒▒▒

    public static ApiResponse createResponse(final HttpStatus status) {
        return new ApiResponse(status.getReasonPhrase());
    }

    public static ApiResponse createResponse(final String message) {
        return new ApiResponse(message);
    }

    public static <T> ApiResponse createResponse(final HttpStatus status, final T data) {
        return DataResponse.createResponse(status.getReasonPhrase(), data);
    }

    public static <T> ApiResponse createResponse(final String message, final T data) {
        return DataResponse.createResponse(message, data);
    }

    // endregion
}