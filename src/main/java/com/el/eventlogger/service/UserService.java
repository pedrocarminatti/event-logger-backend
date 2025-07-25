package com.el.eventlogger.service;

import com.el.eventlogger.dto.UserRequestDTO;
import com.el.eventlogger.dto.UserResponseDTO;

import java.util.List;

public interface UserService {
    UserResponseDTO create(UserRequestDTO dto);
    List<UserResponseDTO> findAll();
    UserResponseDTO findById(Long id);
    UserResponseDTO update(Long id, UserRequestDTO dto);
    void delete(Long id);
}
