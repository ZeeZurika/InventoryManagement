package org.zurika.inventorymanagement.service;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.zurika.inventorymanagement.model.Product;
import org.zurika.inventorymanagement.repository.ProductRepository;

@Service
@Transactional
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public Page<Product> findAll(Pageable pageable){
        return productRepository.findAll(pageable);
    }

    public Optional<Product> findById(Long id){
            return productRepository.findById(id);

    }

    public Product save(Product product) {
        return productRepository.save(product);
    }

    public void saveAll(List<Product> products) {
        productRepository.saveAll(products);
    }

    public void deleteById(Long id){
        productRepository.deleteById(id);
    }

    public Page<Product> search(String category, String name, Pageable pageable) {
        return productRepository.findByCategoryAndNameContaining(category, name, pageable);
    }

    // Accept bulk entry from cvs or excel file
    public List<Product> parseCsvFile(MultipartFile file) throws Exception {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            return reader.lines()
                    .skip(1) // Skip header line
                    .map(line -> {
                        String[] fields = line.split(",");
                        Product product = new Product();
                        if (fields.length >= 4) {
                            product.setName(fields[1].trim());
                            product.setQuantity(Integer.parseInt(fields[2].trim()));
                            product.setPrice(Double.parseDouble(fields[3].trim()));
                        }
                        return product;
                    })
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new Exception("Error parsing CSV file: " + e.getMessage(), e);
        }
    }

    // Make adjustments to stock
    public Product adjustStocks(Long productId, int adjustment, String reason) 
    throws Exception {
        Product product = productRepository.findById(productId)
        .orElseThrow(() -> new Exception("Product not found."));

        int newQuantity = product.getQuantity() + adjustment;
        if (newQuantity < 0) { // ensure quantity is not a negative value
            throw new Exception("Stock adjustment cannot result in negative quantity");
        }
        product.setQuantity(newQuantity);
        productRepository.save(product);

        return product;
    }
}
