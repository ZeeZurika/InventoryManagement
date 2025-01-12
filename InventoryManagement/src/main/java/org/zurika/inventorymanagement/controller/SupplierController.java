package org.zurika.inventorymanagement.controller;

import org.springframework.web.bind.annotation.*;
import org.zurika.inventorymanagement.exception.ResourceNotFoundException;
import org.zurika.inventorymanagement.model.Supplier;
import org.zurika.inventorymanagement.service.SupplierService;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;

@RestController
@RequestMapping("/api/suppliers")
public class SupplierController {

    @Autowired
    private SupplierService supplierService;

    @GetMapping
    public Page<Supplier> getAllSuppliers(Pageable pageable) {
        return supplierService.findAll(pageable);
    }

    @GetMapping("/{id}")
    public Optional<Supplier> getSupplierById(@PathVariable Long id) {
        return Optional.of(supplierService.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException
        ("Supplier not found with id " + id)
        ));
    }

    @PostMapping
    public Supplier createSupplier(@RequestBody Supplier supplier) {
        return supplierService.save(supplier);
    }

    @DeleteMapping("/{id}")
    public void deleteSupplier(@PathVariable Long id) {
        if(!supplierService.findById(id).isPresent()){
            throw new ResourceNotFoundException(
                "Supplier not found with id: " + id);
        }
        supplierService.deleteById(id);
    }
}
