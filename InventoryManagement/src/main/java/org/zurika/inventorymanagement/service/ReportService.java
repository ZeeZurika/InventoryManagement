package org.zurika.inventorymanagement.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zurika.inventorymanagement.model.Product;
import org.zurika.inventorymanagement.repository.ProductRepository;

@Service
public class ReportService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private EmailService emailService;

    public List<Product> findLowStockProducts(int threshold) {
        List<Product> lowStockProducts = productRepository.findLowStockProducts(threshold);
        if (!lowStockProducts.isEmpty()) {
            emailService.sendEmail(
                "admin@example.com",
                "Low Stock Alert",
                "The following products are low in stock: " + lowStockProducts.toString()
            );
        }
        return lowStockProducts;
    }
}
