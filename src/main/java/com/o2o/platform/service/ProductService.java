package com.o2o.platform.service;

import java.util.List;

import com.o2o.platform.dto.ProductDTO;
import com.o2o.platform.request.CreateProductRequest;

public interface ProductService {
    ProductDTO createProduct(CreateProductRequest request);
    ProductDTO getProductById(Long productId);
    List<ProductDTO> getProductsByStore(Long storeId);
}
