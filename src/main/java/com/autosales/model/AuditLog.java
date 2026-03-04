package com.autosales.model;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AuditLog {
    private Long id; // BIGINT
    private Integer userId;
    private String action;
    private String tableName;
    private Integer recordId;
    private String oldValues; // можно хранить как JSON-строку
    private String newValues; // можно хранить как JSON-строку
    private String ipAddress;
    private LocalDateTime timestamp;
}