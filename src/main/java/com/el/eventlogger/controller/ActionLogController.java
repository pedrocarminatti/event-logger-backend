package com.el.eventlogger.controller;

import com.el.eventlogger.dto.ActionLogRequestDTO;
import com.el.eventlogger.dto.ActionLogResponseDTO;
import com.el.eventlogger.service.ActionLogService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/action-logs")
@Validated
public class ActionLogController {

  private final ActionLogService service;

  public ActionLogController(ActionLogService service) {
    this.service = service;
  }

  @PostMapping
  public ResponseEntity<ActionLogResponseDTO> create(@Valid @RequestBody ActionLogRequestDTO dto) {
    ActionLogResponseDTO createdLog = service.create(dto);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdLog);
  }

  @GetMapping
  public ResponseEntity<List<ActionLogResponseDTO>> getAll() {
    List<ActionLogResponseDTO> logs = service.findAll();
    return ResponseEntity.ok(logs);
  }

  @GetMapping("/{id}")
  public ResponseEntity<ActionLogResponseDTO> getById(@PathVariable @Positive Long id) {
    ActionLogResponseDTO log = service.findById(id);
    return ResponseEntity.ok(log);
  }

  @PutMapping("/{id}")
  public ResponseEntity<ActionLogResponseDTO> update(
      @PathVariable @Positive Long id, @Valid @RequestBody ActionLogRequestDTO dto) {
    ActionLogResponseDTO updatedLog = service.update(id, dto);
    return ResponseEntity.ok(updatedLog);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    service.delete(id);
    return ResponseEntity.noContent().build();
  }
}
