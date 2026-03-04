package com.autosales.model;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class User {
    private Integer id;
    private String email;
    private String passwordHash;
    private String firstName;
    private String lastName;
    private String patronymic;
    private String passportSeries;
    private String passportNumber;
    private String address;
    private String phone;
    private String paymentTypeDefault;
    private Integer roleId;
    private Boolean enabled;
    private BigDecimal balance;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}