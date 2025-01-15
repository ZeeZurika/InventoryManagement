package org.zurika.inventorymanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.zurika.inventorymanagement.model.Order;
import org.zurika.inventorymanagement.model.OrderStatus;
import org.zurika.inventorymanagement.service.OrderService;

import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    // Place an Order
    @PostMapping
    public ResponseEntity<String> placeOrder(@RequestBody Order order) {
        try {
            Order savedOrder = orderService.placeOrder(order);
            return ResponseEntity.ok("Order placed successfully with ID: " + savedOrder.getId());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to place order: " + e.getMessage());
        }
    }

    // Get All Orders with Pagination
    @GetMapping
    public Page<Order> getAllOrders(Pageable pageable) {
        return orderService.findAll(pageable);
    }

    // Get Order by ID
    @GetMapping("/{id}")
    public ResponseEntity<Object> getOrderById(@PathVariable Long id) {
    Optional<Order> order = orderService.getOrderById(id);
    if (order.isPresent()) {
        return ResponseEntity.ok(order.get());
    } else {
        return ResponseEntity.status(404).body("Order not found");
    }
}

    // Update Order Status
    @PatchMapping("/{id}/status")
    public ResponseEntity<String> updateOrderStatus(@PathVariable Long id, @RequestParam OrderStatus status) {
        try {
            Order updatedOrder = orderService.updateOrderStatus(id, status);
            return ResponseEntity.ok("Order status updated to " + updatedOrder.getStatus());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to update order status: " + e.getMessage());
        }
    }

    // Cancel an Order
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<String> cancelOrder(@PathVariable Long id) {
        try {
            orderService.cancelOrder(id);
            return ResponseEntity.ok("Order canceled successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to cancel order: " + e.getMessage());
        }
    }
}
