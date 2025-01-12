package org.zurika.inventorymanagement.controller;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.zurika.inventorymanagement.model.Product;
import org.zurika.inventorymanagement.service.ReportService;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/reports")
public class ReportController {
    @Autowired
    private ReportService reportService;

    @GetMapping("/low-stock")
    public List<Product> getLowStockReport() {
        int threshold = 10; // the value can be changed
        return reportService.findLowStockProducts(threshold);
    }
    
}
