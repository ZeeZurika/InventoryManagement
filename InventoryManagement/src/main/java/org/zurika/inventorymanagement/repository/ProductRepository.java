package org.zurika.inventorymanagement.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.zurika.inventorymanagement.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    public Page<Product> findByCategoryAndNameContaining(
        String category, String name, Pageable pageable
        );

    @Query("SELECT p FROM Product p WHERE p.quantity < :threshold")
    public List<Product> findLowStockProducts(@Param("threshold") int threshold);

    public boolean existsByNameAndCategory(String name, String category);
} 
