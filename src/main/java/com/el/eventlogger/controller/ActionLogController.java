package com.el.eventlogger.controller;

import com.el.eventlogger.dto.ActionLogRequestDTO;
import com.el.eventlogger.dto.ActionLogResponseDTO;
import com.el.eventlogger.dto.ApiResponse;
import com.el.eventlogger.service.ActionLogService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/action-logs")
public class ActionLogController {

  private final ActionLogService service;

  public ActionLogController(ActionLogService service) {
    this.service = service;
  }

  @PostMapping
  public ResponseEntity<ApiResponse<ActionLogResponseDTO>> create(
      @Valid @RequestBody ActionLogRequestDTO dto) {
    ActionLogResponseDTO createdLog = service.create(dto);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(ApiResponse.success("Action log created successfully", createdLog));
  }

  @GetMapping
  public ResponseEntity<ApiResponse<List<ActionLogResponseDTO>>> getAll() {
    List<ActionLogResponseDTO> logs = service.findAll();
    return ResponseEntity.ok(ApiResponse.success("Action logs fetched successfully", logs));
  }

  @GetMapping("/{id}")
  public ResponseEntity<ApiResponse<ActionLogResponseDTO>> getById(@PathVariable Long id) {
    ActionLogResponseDTO log = service.findById(id);
    return ResponseEntity.ok(ApiResponse.success("Action log fetched successfully", log));
  }

  @PutMapping("/{id}")
  public ResponseEntity<ApiResponse<ActionLogResponseDTO>> update(
      @PathVariable Long id, @Valid @RequestBody ActionLogRequestDTO dto) {
    ActionLogResponseDTO updatedLog = service.update(id, dto);
    return ResponseEntity.ok(ApiResponse.success("Action log updated successfully", updatedLog));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
    service.delete(id);
    return ResponseEntity.ok(ApiResponse.success("Action log deleted successfully", null));
  }
}
