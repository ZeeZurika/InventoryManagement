package org.zurika.inventorymanagement.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.zurika.inventorymanagement.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    public Page<Product> findByCategoryAndNameContaining(
        String category, String name, Pageable pageable
        );

    public List<Product> findLowStockProducts(int threshold);
} 
