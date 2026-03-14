package com.o2o.platform.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.o2o.platform.dto.ProductDTO;
import com.o2o.platform.request.CreateProductRequest;
import com.o2o.platform.service.ProductService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@Valid @RequestBody CreateProductRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getById(@PathVariable("id") Long productId) {
        return ResponseEntity.ok(productService.getProductById(productId));
    }

    @GetMapping("/store/{storeId}")
    public ResponseEntity<List<ProductDTO>> getByStore(@PathVariable Long storeId) {
        return ResponseEntity.ok(productService.getProductsByStore(storeId));
    }
}
