package com.microservices.ordersystem.product_service.controller;

import com.microservices.ordersystem.product_service.dto.ProductDTO;
import com.microservices.ordersystem.product_service.mapper.Mapper;
import com.microservices.ordersystem.product_service.model.Product;
import com.microservices.ordersystem.product_service.service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @GetMapping
    public List<ProductDTO> getProducts(@RequestParam(required = false) String name,
                                        @RequestParam(required = false) String category) {

        return this.service.searchProducts(name, category).stream().map(Mapper::toDto).toList();
    }

    @GetMapping("/{id}")
    public ProductDTO getProductById(@PathVariable Long id) {
        return Mapper.toDto(this.service.getProductById(id));
    }

    @PostMapping
    public ProductDTO createProduct(@RequestBody Product p) {
        return Mapper.toDto(this.service.create(p));
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        this.service.delete(id);
    }

    @PutMapping("/{id}")
    public ProductDTO updatedProduct(@PathVariable Long id, @RequestBody Product p) {
        return Mapper.toDto(this.service.update(id, p));
    }
}
