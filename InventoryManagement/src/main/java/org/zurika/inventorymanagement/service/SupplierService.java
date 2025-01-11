package org.zurika.inventorymanagement.service;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zurika.inventorymanagement.model.Supplier;
import org.zurika.inventorymanagement.repository.SupplierRepository;

@Service
@Transactional
public class SupplierService {

    @Autowired
    private SupplierRepository supplierRepository;

    public Page<Supplier> findAll(Pageable pageable){
        return supplierRepository.findAll(pageable);
    }

    public Optional<Supplier> findById(Long id){
        return supplierRepository.findById(id);
    }

    public Supplier save(Supplier supplier){
        return supplierRepository.save(supplier);
    }

    public void deleteById(Long id){
        supplierRepository.deleteById(id);
    }
}
