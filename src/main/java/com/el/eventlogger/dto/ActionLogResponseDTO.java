package com.el.eventlogger.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;

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
