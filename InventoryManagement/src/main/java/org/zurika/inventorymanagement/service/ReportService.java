package org.zurika.inventorymanagement.service;

import java.io.*;
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

    public ByteArrayInputStream generateLowStockCsv(List<Product> products) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(out);

        writer.println("ID,Name,Quantity,Price");

        for(Product product : products) {
            writer.println(
                product.getId() + "," +
                product.getName() + "," +
                product.getQuantity() + "," +
                product.getPrice()
            );
        }

        writer.flush();

        return new ByteArrayInputStream(out.toByteArray());
    }
}
