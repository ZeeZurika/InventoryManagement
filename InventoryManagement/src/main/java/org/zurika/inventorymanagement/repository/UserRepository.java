package org.zurika.inventorymanagement.repository;

import java.util.*;
import org.springframework.data.jpa.repository.*;
import org.zurika.inventorymanagement.model.*;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {

    public Optional<User> findByUsername(String username);

    // Query to find products that are low in stock (less than the threshold)
    @Query("SELECT p FROM Product p WHERE p.quantity < :threshold")
    public List<Product> findLowStockProducts(@Param("threshold") int threshold);

}
