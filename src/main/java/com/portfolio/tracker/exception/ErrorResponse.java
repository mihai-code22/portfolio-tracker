package com.portfolio.tracker.exception;

import java.time.LocalDateTime;

public record ErrorResponse(
        String errorCause,
        LocalDateTime timestamp,
        Integer errorCode,
        String path
) {}
