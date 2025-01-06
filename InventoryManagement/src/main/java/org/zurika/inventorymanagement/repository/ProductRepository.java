package org.zurika.inventorymanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.zurika.inventorymanagement.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
} 
