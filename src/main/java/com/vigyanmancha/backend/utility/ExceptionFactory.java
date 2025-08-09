package com.vigyanmancha.backend.utility;

import com.vigyanmancha.backend.dto.response.ErrorResponse;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ExceptionFactory {
    public ErrorResponse create(Throwable throwable, int statusCode) {
        return ErrorResponse.builder()
                .code(statusCode)
                .message(throwable.getMessage())
                .build();
    }
}
