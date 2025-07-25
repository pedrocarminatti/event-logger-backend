package com.el.eventlogger.dto;

import java.time.LocalDateTime;

public record ActionLogResponseDTO(
        Long id,
        String action,
        String description,
        LocalDateTime timestamp,
        Long userId,
        String userName,
        String userEmail
) {}
