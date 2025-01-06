package org.zurika.inventorymanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zurika.inventorymanagement.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
