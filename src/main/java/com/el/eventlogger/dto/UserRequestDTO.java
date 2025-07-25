package com.el.eventlogger.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserRequestDTO(
        @NotBlank String name,
        @NotBlank @Email(message = "Invalid email format") String email
) {}
