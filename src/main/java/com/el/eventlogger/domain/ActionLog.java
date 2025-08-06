package com.el.eventlogger.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;

@Entity
@Table(name = "action_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActionLog {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String action;

  private String description;

  private LocalDateTime timestamp;

  @ManyToOne(fetch = FetchType.LAZY, optional = true)
  @JoinColumn(name = "user_id", nullable = true)
  private User user;
}
