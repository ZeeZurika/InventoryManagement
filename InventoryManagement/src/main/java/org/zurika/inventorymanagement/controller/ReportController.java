package org.zurika.inventorymanagement.controller;

import java.io.ByteArrayInputStream;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.*;
import org.springframework.http.*;
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

    @GetMapping("/low-stock/export")
    public ResponseEntity<InputStreamResource> exportLowStockReport(
        @RequestParam(value = "threshold", defaultValue = "10") int threshold
    ) {
        List<Product> lowStockProducts = reportService.findLowStockProducts(threshold);
        ByteArrayInputStream csvStream = reportService.generateLowStockCsv(lowStockProducts);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=low-stock-report.csv");

        return ResponseEntity
            .ok()
            .headers(headers)
            .contentType(MediaType.parseMediaType("text/csv"))
            .body(new InputStreamResource(csvStream));
    }
    
}
