package com.autosales.model;

import lombok.Data;

@Data
public class Role {
    private Integer id;
    private String name; // например, ROLE_CUSTOMER, ROLE_ADMIN
}