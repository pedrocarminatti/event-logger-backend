package com.el.eventlogger.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ActionLogRequestDTO {

    @NotBlank
    private String action;

    @NotBlank
    private String description;

    @NotNull
    private Long userId;
}
