package com.autosales.model;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class SaleDetail {
    private Integer id;
    private Integer saleId;
    private Integer carId;
    private Integer quantity; // обычно 1
    private BigDecimal priceAtSale; // цена на момент продажи
}