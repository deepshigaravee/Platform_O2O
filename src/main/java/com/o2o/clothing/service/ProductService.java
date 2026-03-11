package com.o2o.clothing.service;

import com.o2o.clothing.dto.ProductCreateRequest;
import com.o2o.clothing.entity.Product;
import com.o2o.clothing.entity.Store;
import com.o2o.clothing.repository.ProductRepository;
import com.o2o.clothing.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final StoreRepository storeRepository;

    @Autowired
    public ProductService(ProductRepository productRepository, StoreRepository storeRepository) {
        this.productRepository = productRepository;
        this.storeRepository = storeRepository;
    }

    public Product addProduct(ProductCreateRequest request) {
        Store store = storeRepository.findById(request.getStoreId())
                .orElseThrow(() -> new RuntimeException("Store not found!"));

        Product product = new Product();
        product.setStore(store);
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStockQuantity(request.getStock());

        return productRepository.save(product);
    }

    public Product updateProduct(Long id, ProductCreateRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found!"));

        if(request.getName() != null) product.setName(request.getName());
        if(request.getDescription() != null) product.setDescription(request.getDescription());
        if(request.getPrice() != null) product.setPrice(request.getPrice());
        if(request.getStock() != null) product.setStockQuantity(request.getStock());

        return productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        if(!productRepository.existsById(id)) {
             throw new RuntimeException("Product not found!");
        }
        productRepository.deleteById(id);
    }

    public List<Product> getInventoryByStore(Long storeId) {
        return productRepository.findByStoreId(storeId);
    }
    
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
}
