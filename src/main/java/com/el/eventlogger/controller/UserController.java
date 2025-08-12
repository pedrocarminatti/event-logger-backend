package com.el.eventlogger.controller;

import com.el.eventlogger.dto.UserRequestDTO;
import com.el.eventlogger.dto.UserResponseDTO;
import com.el.eventlogger.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@Validated
public class UserController {

  private final UserService service;

  public UserController(UserService service) {
    this.service = service;
  }

  @PostMapping
  public ResponseEntity<UserResponseDTO> create(@Valid @RequestBody UserRequestDTO dto) {
    UserResponseDTO createdUser = service.create(dto);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
  }

  @GetMapping
  public ResponseEntity<List<UserResponseDTO>> getAll() {
    List<UserResponseDTO> users = service.findAll();
    return ResponseEntity.ok(users);
  }

  @GetMapping("/{id}")
  public ResponseEntity<UserResponseDTO> getById(@PathVariable @Positive Long id) {
    UserResponseDTO user = service.findById(id);
    return ResponseEntity.ok(user);
  }

  @PutMapping("/{id}")
  public ResponseEntity<UserResponseDTO> update(
      @PathVariable @Positive Long id, @Valid @RequestBody UserRequestDTO dto) {
    UserResponseDTO updatedUser = service.update(id, dto);
    return ResponseEntity.ok(updatedUser);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable @Positive Long id) {
    service.delete(id);
    return ResponseEntity.noContent().build();
  }
}
