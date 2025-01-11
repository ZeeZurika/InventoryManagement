package org.zurika.inventorymanagement.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.zurika.inventorymanagement.model.Supplier;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {
    
}
