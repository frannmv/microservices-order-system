package com.microservices.ordersystem.product_service.service;

import com.microservices.ordersystem.product_service.dto.ProductDTO;
import com.microservices.ordersystem.product_service.exceptions.NotFoundException;
import com.microservices.ordersystem.product_service.mapper.Mapper;
import com.microservices.ordersystem.product_service.model.Category;
import com.microservices.ordersystem.product_service.model.Product;
import com.microservices.ordersystem.product_service.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    public List<Product> searchProducts(String name, String category) {

        boolean hasName = name != null && !name.isBlank();
        boolean hasCategory = category != null && !category.isBlank();

        Category cat = null;

        if(hasCategory) {
            cat = Category.valueOf(category.toUpperCase());
        }

        if(hasName && hasCategory) {
            return this.repository.findByNameContainingIgnoreCaseAndCategory(name,cat);
        }

        if(hasName) {
            return this.repository.findByNameContainingIgnoreCase(name);
        }

        if(hasCategory) {
            return this.repository.findByCategory(cat);
        }

        return this.repository.findAll();
    }

    public Product getProductById(Long id) {
        return this.repository.findById(id).orElseThrow(() -> new NotFoundException("Product not found"));
    }

    public Product create(Product p) {
        return this.repository.save(p);
    }

    public void delete(Long id) {
        this.repository.delete(this.getProductById(id));
    }

    @Transactional
    public Product update(Long id, Product p) {

        if(p == null) return null;

        Product old = this.getProductById(id);

        old.setName(p.getName());
        old.setCategory(p.getCategory());
        old.setPrice(p.getPrice());
        old.setStatus(p.getStatus());

        return old;
    }
}
