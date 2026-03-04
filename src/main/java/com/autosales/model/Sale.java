package com.autosales.model;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Sale {
    private Integer id;
    private Integer userId;
    private LocalDateTime saleDate;
    private BigDecimal totalAmount;
    private LocalDateTime createdAt;
}