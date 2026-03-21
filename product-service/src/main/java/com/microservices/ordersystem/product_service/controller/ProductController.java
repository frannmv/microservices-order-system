package com.microservices.ordersystem.product_service.controller;

import com.microservices.ordersystem.product_service.dto.ProductDto;
import com.microservices.ordersystem.product_service.mapper.Mapper;
import com.microservices.ordersystem.product_service.model.Product;
import com.microservices.ordersystem.product_service.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<ProductDto>> getProducts(@RequestParam(required = false) String name,
                                                        @RequestParam(required = false) String category) {

        return ResponseEntity.ok(this.service.searchProducts(name, category).stream().map(Mapper::toDto).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(Mapper.toDto(this.service.getProductById(id)));
    }

    @GetMapping("/{id}/available")
    public ResponseEntity<ProductDto> validateAndRetrieve(@PathVariable Long id) {
        Product product = this.service.getAvailableProduct(id);
        return ResponseEntity.ok(Mapper.toDto(product));
    }
    
    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@RequestBody Product p) {
        ProductDto created = Mapper.toDto(this.service.create(p));
        return ResponseEntity.created(URI.create("/products/" + created.getId())).body(created);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        this.service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updatedProduct(@PathVariable Long id, @RequestBody Product p) {
        return ResponseEntity.ok(Mapper.toDto(this.service.update(id, p)));
    }
}
