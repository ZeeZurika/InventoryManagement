package org.zurika.inventorymanagement.controller;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.zurika.inventorymanagement.exception.ResourceNotFoundException;
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
        return Optional.of(orderService.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException
        ("Order not found with id: " + id)
        ));
    }

    public Order createOrder(@RequestBody Order order){
        return orderService.save(order);
    }

    @DeleteMapping("/{id}")
    public void deleteOrder(@PathVariable Long id){
        if(!orderService.findById(id).isPresent()){
            throw new ResourceNotFoundException(
                "Order not found with id: " + id);
        }
        orderService.deleteById(id);
    }
}
