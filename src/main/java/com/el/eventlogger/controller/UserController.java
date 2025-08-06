package com.el.eventlogger.controller;

import com.el.eventlogger.dto.ApiResponse;
import com.el.eventlogger.dto.UserRequestDTO;
import com.el.eventlogger.dto.UserResponseDTO;
import com.el.eventlogger.service.UserService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

  private final UserService service;

  public UserController(UserService service) {
    this.service = service;
  }

  @PostMapping
  public ResponseEntity<ApiResponse<UserResponseDTO>> create(
      @Valid @RequestBody UserRequestDTO dto) {
    UserResponseDTO createdUser = service.create(dto);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(ApiResponse.success("User created successfully", createdUser));
  }

  @GetMapping
  public ResponseEntity<ApiResponse<List<UserResponseDTO>>> getAll() {
    List<UserResponseDTO> users = service.findAll();
    return ResponseEntity.ok(ApiResponse.success("Users fetched successfully", users));
  }

  @GetMapping("/{id}")
  public ResponseEntity<ApiResponse<UserResponseDTO>> getById(@PathVariable Long id) {
    UserResponseDTO user = service.findById(id);
    return ResponseEntity.ok(ApiResponse.success("User fetched successfully", user));
  }

  @PutMapping("/{id}")
  public ResponseEntity<ApiResponse<UserResponseDTO>> update(
      @PathVariable Long id, @Valid @RequestBody UserRequestDTO dto) {
    UserResponseDTO updatedUser = service.update(id, dto);
    return ResponseEntity.ok(ApiResponse.success("User updated successfully", updatedUser));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
    service.delete(id);
    return ResponseEntity.ok(ApiResponse.success("User deleted successfully", null));
  }
}
