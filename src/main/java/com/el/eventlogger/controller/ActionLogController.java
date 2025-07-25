package com.el.eventlogger.controller;

import com.el.eventlogger.dto.ActionLogRequestDTO;
import com.el.eventlogger.dto.ActionLogResponseDTO;
import com.el.eventlogger.service.ActionLogService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/action-logs")
public class ActionLogController {

    private final ActionLogService service;

    public ActionLogController(ActionLogService service) {
        this.service = service;
    }

    @PostMapping
    public ActionLogResponseDTO create(@Valid @RequestBody ActionLogRequestDTO dto) {
        return service.create(dto);
    }

    @GetMapping
    public List<ActionLogResponseDTO> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ActionLogResponseDTO getById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PutMapping("/{id}")
    public ActionLogResponseDTO update(@PathVariable Long id, @Valid @RequestBody ActionLogRequestDTO dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
