package com.autosales.model;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class VerificationToken {
    private Long id;
    private String token;
    private Integer userId;
    private LocalDateTime expiryDate;
}