package com.el.eventlogger.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ActionLogResponseDTO {
    private Long id;
    private String action;
    private String description;
    private LocalDateTime timestamp;
    private Long userId;
    private String userName;
    private String userEmail;
}
