package com.el.eventlogger.service;

import com.el.eventlogger.domain.User;
import com.el.eventlogger.dto.ActionLogRequestDTO;
import com.el.eventlogger.dto.ActionLogResponseDTO;
import java.util.List;

public interface ActionLogService {
  ActionLogResponseDTO create(ActionLogRequestDTO dto);

  List<ActionLogResponseDTO> findAll();

  ActionLogResponseDTO findById(Long id);

  ActionLogResponseDTO update(Long id, ActionLogRequestDTO dto);

  void delete(Long id);

  void log(String action, String description, User user);
}
