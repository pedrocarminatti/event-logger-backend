package com.el.eventlogger.mapper;

import com.el.eventlogger.domain.ActionLog;
import com.el.eventlogger.dto.ActionLogResponseDTO;

public class ActionLogMapper {
  public static ActionLogResponseDTO toDTO(ActionLog log) {
    if (log == null) return null;

    Long userId = null;
    String userName = null;
    String userEmail = null;

    if (log.getUser() != null) {
      userId = log.getUser().getId();
      userName = log.getUser().getName();
      userEmail = log.getUser().getEmail();
    }

    return new ActionLogResponseDTO(
        log.getId(),
        log.getAction(),
        log.getDescription(),
        log.getTimestamp(),
        userId,
        userName,
        userEmail);
  }
}
