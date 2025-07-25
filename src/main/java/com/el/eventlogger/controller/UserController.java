package com.el.eventlogger.controller;

import com.el.eventlogger.dto.UserRequestDTO;
import com.el.eventlogger.dto.UserResponseDTO;
import com.el.eventlogger.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping
    public UserResponseDTO create(@Valid @RequestBody UserRequestDTO dto) {
        return service.create(dto);
    }

    @GetMapping
    public List<UserResponseDTO> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public UserResponseDTO getById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PutMapping("/{id}")
    public UserResponseDTO update(@PathVariable Long id, @Valid @RequestBody UserRequestDTO dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
