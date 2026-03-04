package com.autosales.model;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Favorite {
    private Integer userId;
    private Integer carId;
    private LocalDateTime createdAt;
}