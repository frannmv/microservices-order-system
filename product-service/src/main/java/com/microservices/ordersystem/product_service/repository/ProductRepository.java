package com.microservices.ordersystem.product_service.repository;

import com.microservices.ordersystem.product_service.model.Category;
import com.microservices.ordersystem.product_service.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
    List<Product> findByNameContainingIgnoreCase(String name);
    List<Product> findByCategory(Category category);
    List<Product> findByNameContainingIgnoreCaseAndCategory(String name, Category category);
}
