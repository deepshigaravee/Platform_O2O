package com.o2o.platform.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.o2o.platform.dto.ProductDTO;
import com.o2o.platform.entity.Product;
import com.o2o.platform.entity.Store;
import com.o2o.platform.exception.ResourceNotFoundException;
import com.o2o.platform.repository.ProductRepository;
import com.o2o.platform.repository.StoreRepository;
import com.o2o.platform.request.CreateProductRequest;
import com.o2o.platform.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final StoreRepository storeRepository;

    public ProductServiceImpl(ProductRepository productRepository, StoreRepository storeRepository) {
        this.productRepository = productRepository;
        this.storeRepository = storeRepository;
    }

    @Override
    public ProductDTO createProduct(CreateProductRequest request) {
        Store store = storeRepository.findById(request.getStoreId())
                .orElseThrow(() -> new ResourceNotFoundException("Store not found"));

        if (request.getPrice() == null || request.getPrice().signum() <= 0) {
            throw new IllegalArgumentException("Product price must be greater than 0");
        }
        if (request.getQuantity() == null || request.getQuantity() < 0) {
            throw new IllegalArgumentException("Product quantity cannot be negative");
        }

        Product product = new Product();
        product.setProductName(request.getProductName());
        product.setBrand(request.getBrand());
        product.setCategory(request.getCategory());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setQuantity(request.getQuantity());
        product.setImageUrl(request.getImageUrl());
        product.setActive(request.getActive() == null ? Boolean.TRUE : request.getActive());
        product.setStore(store);
        return toDTO(productRepository.save(product));
    }

    @Override
    public ProductDTO getProductById(Long productId) {
        return toDTO(productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found")));
    }

    @Override
    public List<ProductDTO> getProductsByStore(Long storeId) {
        return productRepository.findByStoreStoreId(storeId).stream().map(this::toDTO).toList();
    }

    private ProductDTO toDTO(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setProductId(product.getProductId());
        dto.setProductName(product.getProductName());
        dto.setBrand(product.getBrand());
        dto.setCategory(product.getCategory());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setQuantity(product.getQuantity());
        dto.setImageUrl(product.getImageUrl());
        dto.setActive(product.getActive());
        dto.setCreatedAt(product.getCreatedAt());
        dto.setUpdatedAt(product.getUpdatedAt());
        dto.setStoreId(product.getStore() != null ? product.getStore().getStoreId() : null);
        return dto;
    }
}
