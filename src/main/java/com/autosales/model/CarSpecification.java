package com.autosales.model;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class CarSpecification {
    private Integer carId;
    private BigDecimal engineVolume;
    private Integer horsepower;
    private String color;
    private Boolean airConditioning;
    private Integer year;
    private Integer mileage;
}