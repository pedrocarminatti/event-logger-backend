package com.el.eventlogger.service.impl;

import com.el.eventlogger.domain.User;
import com.el.eventlogger.dto.UserRequestDTO;
import com.el.eventlogger.dto.UserResponseDTO;
import com.el.eventlogger.repository.UserRepository;
import com.el.eventlogger.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserResponseDTO create(UserRequestDTO dto) {
        User user = new User();
        user.setName(dto.name());
        user.setEmail(dto.email());
        User saved = repository.save(user);
        return mapToDTO(saved);
    }

    @Override
    public List<UserResponseDTO> findAll() {
        return repository.findAll().stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    public UserResponseDTO findById(Long id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + id));
        return mapToDTO(user);
    }

    @Override
    public UserResponseDTO update(Long id, UserRequestDTO dto) {
        User user = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + id));
        user.setName(dto.name());
        user.setEmail(dto.email());
        User updated = repository.save(user);
        return mapToDTO(updated);
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("User not found with ID: " + id);
        }
        repository.deleteById(id);
    }

    private UserResponseDTO mapToDTO(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getCreatedAt()
        );
    }
}
