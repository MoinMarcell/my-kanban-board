package com.github.moinmarcell.backend.exception;

import java.time.LocalDateTime;

public record CustomErrorMessage(
        String message,
        LocalDateTime timestamp
) {
}
