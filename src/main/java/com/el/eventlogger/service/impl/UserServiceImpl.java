package com.el.eventlogger.service.impl;

import com.el.eventlogger.domain.ActionLog;
import com.el.eventlogger.domain.User;
import com.el.eventlogger.dto.UserRequestDTO;
import com.el.eventlogger.dto.UserResponseDTO;
import com.el.eventlogger.repository.ActionLogRepository;
import com.el.eventlogger.repository.UserRepository;
import com.el.eventlogger.service.ActionLogService;
import com.el.eventlogger.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private static final String USER_NOT_FOUND = "User not found with ID: ";

    private final UserRepository userRepository;
    private final ActionLogRepository actionLogRepository;
    private final ActionLogService actionLogService;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, ActionLogRepository actionLogRepository, ActionLogService actionLogService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.actionLogRepository = actionLogRepository;
        this.actionLogService = actionLogService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserResponseDTO create(UserRequestDTO dto) {

        User user = User.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .createdAt(LocalDateTime.now())
                .build();

        User saved = userRepository.save(user);
        log.info("[USER] Created user: {}", saved.getEmail());
        actionLogService.log("CREATE_USER", "User created: " + saved.getEmail(), saved);

        return mapToDTO(saved);
    }

    @Override
    public List<UserResponseDTO> findAll() {
        List<User> users = userRepository.findAll();

        log.info("[USER] Retrieved {} users", users.size());
        if (!users.isEmpty()) {
            actionLogService.log("READ_USERS", "Retrieved all users", null);
        }

        return users.stream().map(this::mapToDTO).toList();
    }

    @Override
    public UserResponseDTO findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND + id));

        log.info("[USER] Retrieved user by ID {}", id);
        actionLogService.log("READ_USER", "Retrieved user ID: " + id, user);

        return mapToDTO(user);
    }

    @Override
    public UserResponseDTO update(Long id, UserRequestDTO dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND + id));

        user.setName(dto.getName());
        user.setEmail(dto.getEmail());

        User updated = userRepository.save(user);
        log.info("[USER] Updated user: {}", updated.getEmail());
        actionLogService.log("UPDATE_USER", "Updated user: " + updated.getEmail(), updated);

        return mapToDTO(updated);
    }

    @Override
    public void delete(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND + id));

        actionLogService.log("DELETE_USER", "Deleted user with ID: " + id + " (Name: " + user.getName() + ")", user);

        List<ActionLog> logs = actionLogRepository.findByUser(user);
        if (!logs.isEmpty()) {
            actionLogRepository.deleteAll(logs);
            actionLogRepository.flush();
        }

        userRepository.delete(user);

        log.info("Deleted user: {}", id);
    }

    private UserResponseDTO mapToDTO(User user) {
        return new UserResponseDTO(user.getId(), user.getName(), user.getEmail(), user.getCreatedAt());
    }
}
