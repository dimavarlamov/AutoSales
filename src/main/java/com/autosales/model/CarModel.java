package com.autosales.model;

import lombok.Data;

@Data
public class CarModel {
    private Integer id;
    private Integer brandId;
    private String name;
    private String brandName;
    private String steeringType;
    private String transmission;
    private String engineType;
    private String steeringWheelSide;
    private String bodyType;
    private Integer doorsCount;
    private Integer seatsCount;
}