package com.autosales.model;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AuditLog {
    private Long id;
    private Integer userId;
    private String action;
    private String tableName;
    private Integer recordId;
    private String oldValues;
    private String newValues;
    private String ipAddress;
    private LocalDateTime timestamp;
}