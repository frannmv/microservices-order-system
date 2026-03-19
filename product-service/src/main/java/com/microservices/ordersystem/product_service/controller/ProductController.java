package com.microservices.ordersystem.product_service.controller;

import com.microservices.ordersystem.product_service.dto.ProductDTO;
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
    public ResponseEntity<List<ProductDTO>> getProducts(@RequestParam(required = false) String name,
                                                        @RequestParam(required = false) String category) {

        return ResponseEntity.ok(this.service.searchProducts(name, category).stream().map(Mapper::toDto).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(Mapper.toDto(this.service.getProductById(id)));
    }

    @GetMapping("/{id}/available")
    public ResponseEntity<Void> isValid(@PathVariable Long id) {
        this.service.isValid(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@RequestBody Product p) {
        ProductDTO created = Mapper.toDto(this.service.create(p));
        return ResponseEntity.created(URI.create("/products/" + created.getId())).body(created);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        this.service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updatedProduct(@PathVariable Long id, @RequestBody Product p) {
        return ResponseEntity.ok(Mapper.toDto(this.service.update(id, p)));
    }
}
