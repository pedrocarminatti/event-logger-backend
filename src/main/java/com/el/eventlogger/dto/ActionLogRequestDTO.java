package com.el.eventlogger.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ActionLogRequestDTO(
        @NotBlank String action,
        @NotBlank String description,
        @NotNull Long userId
) {}
