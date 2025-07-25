package com.el.eventlogger.service.impl;

import com.el.eventlogger.domain.ActionLog;
import com.el.eventlogger.domain.User;
import com.el.eventlogger.dto.ActionLogRequestDTO;
import com.el.eventlogger.dto.ActionLogResponseDTO;
import com.el.eventlogger.repository.ActionLogRepository;
import com.el.eventlogger.repository.UserRepository;
import com.el.eventlogger.service.ActionLogService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ActionLogServiceImpl implements ActionLogService {

    private final ActionLogRepository actionLogRepository;
    private final UserRepository userRepository;

    public ActionLogServiceImpl(ActionLogRepository actionLogRepository, UserRepository userRepository) {
        this.actionLogRepository = actionLogRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ActionLogResponseDTO create(ActionLogRequestDTO dto) {
        User user = userRepository.findById(dto.userId())
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + dto.userId()));

        ActionLog log = ActionLog.builder()
                .action(dto.action())
                .description(dto.description())
                .timestamp(LocalDateTime.now())
                .user(user)
                .build();

        ActionLog saved = actionLogRepository.save(log);
        return mapToDTO(saved);
    }

    @Override
    public List<ActionLogResponseDTO> findAll() {
        return actionLogRepository.findAll().stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    public ActionLogResponseDTO findById(Long id) {
        ActionLog log = actionLogRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ActionLog not found with ID: " + id));
        return mapToDTO(log);
    }

    @Override
    public ActionLogResponseDTO update(Long id, ActionLogRequestDTO dto) {
        ActionLog log = actionLogRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ActionLog not found with ID: " + id));

        User user = userRepository.findById(dto.userId())
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + dto.userId()));

        log.setAction(dto.action());
        log.setDescription(dto.description());
        log.setUser(user);
        log.setTimestamp(LocalDateTime.now());

        ActionLog updated = actionLogRepository.save(log);
        return mapToDTO(updated);
    }

    @Override
    public void delete(Long id) {
        if (!actionLogRepository.existsById(id)) {
            throw new EntityNotFoundException("ActionLog not found with ID: " + id);
        }
        actionLogRepository.deleteById(id);
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
