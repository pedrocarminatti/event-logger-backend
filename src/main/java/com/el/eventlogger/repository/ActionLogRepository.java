package com.el.eventlogger.repository;

import com.el.eventlogger.domain.ActionLog;
import com.el.eventlogger.domain.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActionLogRepository extends JpaRepository<ActionLog, Long> {
  List<ActionLog> findByUser(User user);
}
