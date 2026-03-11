package com.o2o.clothing.dto;

import lombok.Data;

@Data
public class ProductCreateRequest {
    private String name;
    private Double price;
    private Integer stock;
    private Long storeId;
    private String description;
}
