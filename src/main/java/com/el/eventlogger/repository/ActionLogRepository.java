package com.el.eventlogger.repository;

import com.el.eventlogger.domain.ActionLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActionLogRepository extends JpaRepository<ActionLog, Long> {
}
