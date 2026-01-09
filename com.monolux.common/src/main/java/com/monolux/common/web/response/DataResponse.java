package com.monolux.common.web.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class DataResponse<T> extends ApiResponse {
    // region ▒▒▒▒▒ Member Variables ▒▒▒▒▒

    private final T data;

    // endregion

    // region ▒▒▒▒▒ Constructors ▒▒▒▒▒

    private DataResponse(final String message) {
        this(message, null);
    }

    private DataResponse(final String message, final T data) {
        super(message);
        this.data = data;
    }

    // endregion

    // region ▒▒▒▒▒ Methods ▒▒▒▒▒

    public static <S> ApiResponse createResponse(final HttpStatus status, final S data) {
        return new DataResponse<>(status.getReasonPhrase(), data);
    }

    public static <S> ApiResponse createResponse(final String message, final S data) {
        return new DataResponse<>(message, data);
    }

    // endregion
}