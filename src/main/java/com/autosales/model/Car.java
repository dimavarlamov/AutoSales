package com.autosales.model;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class Car {
    private Integer id;
    private Integer modelId;
    private BigDecimal price;
    private Integer stockQuantity;
    private LocalDate expectedDate;
    private String description;
    private String image;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private String brandName;
    private String modelName;
    private String brandCountry;
}