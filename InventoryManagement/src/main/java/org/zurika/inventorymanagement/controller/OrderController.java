package org.zurika.inventorymanagement.controller;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.zurika.inventorymanagement.model.Order;
import org.zurika.inventorymanagement.service.OrderService;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping
    public List<Order> getAllOrders(){
        return orderService.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Order> findOrderById(@PathVariable Long id){
        return orderService.findById(id);
    }

    public Order createOrder(@RequestBody Order order){
        return orderService.save(order);
    }

    @DeleteMapping("/{id}")
    public void deleteOrder(@PathVariable Long id){
        orderService.deleteById(id);
    }
}
