package org.zurika.inventorymanagement.controller;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
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

    @PostMapping("/import")
    public ResponseEntity<String> importProducts(
        @RequestParam("file") MultipartFile file) {
        try {
            List<Product> products = productService.parseCsvFile(file);
            productService.saveAll(products);
            return ResponseEntity.ok("Products imported successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest()
            .body("Failed to import products: " + e.getMessage());
        }
    }

    @PatchMapping("/{id}/adjust-stock")
    public ResponseEntity<String> adjustStock(
        @PathVariable("id") Long id,
        @RequestParam("quantity") int quantity,
        @RequestParam("reason") String reason
    ){
        try {
            Product updatedProduct = productService.adjustStocks(id, quantity, reason);
            return ResponseEntity.ok("Stock adjusted successfully for product: " + updatedProduct.getName());
        } catch (Exception e) {
            return ResponseEntity.badRequest()
            .body("Failed to adjust stock: " + e.getMessage());
        }
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
