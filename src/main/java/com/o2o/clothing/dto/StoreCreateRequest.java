package com.o2o.clothing.dto;

import lombok.Data;

@Data
public class StoreCreateRequest {
    private String name;
    private String address;
    private Double latitude;
    private Double longitude;
}
