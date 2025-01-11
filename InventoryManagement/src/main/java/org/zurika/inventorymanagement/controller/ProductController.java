package org.zurika.inventorymanagement.controller;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.web.bind.annotation.*;
import org.zurika.inventorymanagement.exception.ResourceNotFoundException;
import org.zurika.inventorymanagement.model.Product;
import org.zurika.inventorymanagement.service.ProductService;


@RestController
@RequestMapping("/api/supplier")
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping
    public Page<Product> getAllProducts(Pageable pageable) {
        return productService.findAll(pageable);
    }

    @GetMapping("/{id}")
    public Optional<Product> getProductByid(@PathVariable Long id) {
        return Optional.of(productService.findById(id).orElseThrow(
            () -> new ResourceNotFoundException(
                "Product not found with id: " + id)
        ));
    }

    // searching and filtering products using pagination
    @GetMapping("/search")
    public Page<Product> searchProduct(
        @RequestParam(required = false) String category,
        @RequestParam(required = false) String name,
        Pageable pageable
    ){
        return productService.search(category, name, pageable);
    }
    
    @PostMapping
    public Product createProduct(@RequestBody Product product){
        return productService.save(product);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id){
        if(!productService.findById(id).isPresent()){
            throw new ResourceNotFoundException(
                "Product not found with id: " + id);
        }
        productService.deleteById(id);
    }    
}
