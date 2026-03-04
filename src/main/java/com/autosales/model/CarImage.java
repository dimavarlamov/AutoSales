package com.autosales.model;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CarImage {
    private Integer id;
    private Integer carId;
    private String imagePath;
    private LocalDateTime createdAt;
}