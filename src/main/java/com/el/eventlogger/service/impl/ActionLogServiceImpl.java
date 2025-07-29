package com.el.eventlogger.service.impl;

import com.el.eventlogger.domain.ActionLog;
import com.el.eventlogger.domain.User;
import com.el.eventlogger.dto.ActionLogRequestDTO;
import com.el.eventlogger.dto.ActionLogResponseDTO;
import com.el.eventlogger.repository.ActionLogRepository;
import com.el.eventlogger.repository.UserRepository;
import com.el.eventlogger.service.ActionLogService;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class ActionLogServiceImpl implements ActionLogService {

    private static final String USER_NOT_FOUND = "User not found with ID: ";
    private static final String LOG_NOT_FOUND = "ActionLog not found with ID: ";

    private final ActionLogRepository actionLogRepository;
    private final UserRepository userRepository;

    public ActionLogServiceImpl(ActionLogRepository actionLogRepository, UserRepository userRepository) {
        this.actionLogRepository = actionLogRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ActionLogResponseDTO create(ActionLogRequestDTO dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND + dto.getUserId()));

        ActionLog logEntry = ActionLog.builder()
                .action(dto.getAction())
                .description(dto.getDescription())
                .timestamp(LocalDateTime.now())
                .user(user)
                .build();

        ActionLog saved = actionLogRepository.save(logEntry);
        log.info("[ACTION_LOG] Created log for user {}: {}", user.getEmail(), dto.getAction());
        return mapToDTO(saved);
    }

    @Override
    public List<ActionLogResponseDTO> findAll() {
        List<ActionLogResponseDTO> logs = actionLogRepository.findAll().stream()
                .map(this::mapToDTO)
                .toList();

        log.info("[ACTION_LOG] Retrieved {} logs", logs.size());
        return logs;
    }

    @Override
    public ActionLogResponseDTO findById(Long id) {
        ActionLog logEntry = actionLogRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(LOG_NOT_FOUND + id));

        log.info("[ACTION_LOG] Retrieved log ID {}", id);
        return mapToDTO(logEntry);
    }

    @Override
    public ActionLogResponseDTO update(Long id, ActionLogRequestDTO dto) {
        ActionLog logEntry = actionLogRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(LOG_NOT_FOUND + id));

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND + dto.getUserId()));

        logEntry.setAction(dto.getAction());
        logEntry.setDescription(dto.getDescription());
        logEntry.setUser(user);
        logEntry.setTimestamp(LocalDateTime.now());

        ActionLog updated = actionLogRepository.save(logEntry);
        log.info("[ACTION_LOG] Updated log ID {} for user {}", id, user.getEmail());
        return mapToDTO(updated);
    }

    @Override
    public void delete(Long id) {
        if (!actionLogRepository.existsById(id)) {
            throw new EntityNotFoundException(LOG_NOT_FOUND + id);
        }
        actionLogRepository.deleteById(id);
        log.info("[ACTION_LOG] Deleted log ID {}", id);
    }

    @Override
    public void log(String action, String description, User user) {
        ActionLog actionLog = ActionLog.builder()
                .action(action)
                .description(description)
                .timestamp(LocalDateTime.now())
                .user(user)
                .build();

        actionLogRepository.save(actionLog);
        log.info("[ACTION_LOG] {} - {}", action, description);
    }

    private ActionLogResponseDTO mapToDTO(ActionLog log) {
        return new ActionLogResponseDTO(
                log.getId(),
                log.getAction(),
                log.getDescription(),
                log.getTimestamp(),
                log.getUser().getId(),
                log.getUser().getName(),
                log.getUser().getEmail()
        );
    }
}
