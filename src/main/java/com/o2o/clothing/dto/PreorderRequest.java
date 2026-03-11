package com.o2o.clothing.dto;

import lombok.Data;

@Data
public class PreorderRequest {
    private Long userId;
    private Long productId;
}
