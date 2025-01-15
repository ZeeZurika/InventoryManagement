package org.zurika.inventorymanagement.service;

import java.time.*;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zurika.inventorymanagement.model.*;
import org.zurika.inventorymanagement.repository.*;

@Service
@Transactional
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    public Page<Order> findAll(Pageable pageable){
        return orderRepository.findAll(pageable);
    }

    public Optional<Order> findById(Long id){
        return orderRepository.findById(id);
    }

    public Order save(Order order){
        return orderRepository.save(order);
    }

    public void deleteById(Long id){
        orderRepository.deleteById(id);
    }

    // place order and update product stock
    public Order placeOrder(Order order) throws Exception {
        for(OrderItem item: order.getOrderItems()){
            Product product = productRepository.findById(item.getProduct().getId())
                .orElseThrow(() -> new Exception("Product not found"));
            if(product.getQuantity() < item.getQuantity()){
                throw new Exception("Insufficient stock for product: " + product.getName());
            }
            product.setQuantity(product.getQuantity() - item.getQuantity());
            productRepository.save(product);
        }
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);
        return orderRepository.save(order);
    }

    public Page<Order> getAllOrders(Pageable pageable){
        return orderRepository.findAll(pageable);
    }

    public Optional<Order> getOrderById(Long id){
        return orderRepository.findById(id);
    }

    public Order updateOrderStatus(Long id, OrderStatus status) throws Exception {
        Order order = orderRepository.findById(id)
        .orElseThrow(() -> new Exception("Order not found"));
        order.setStatus(status);
        return orderRepository.save(order);
    }

    public void cancelOrder(Long id) throws Exception {
        Order order = orderRepository.findById(id)
        .orElseThrow(() -> new Exception("Order not found"));

        if(order.getStatus() == OrderStatus.COMPLETED){
            throw new Exception("Completed orders cannot be cancelled");
        }

        // restore product stock
        for(OrderItem item: order.getOrderItems()){
            Product product = item.getProduct();
            product.setQuantity(product.getQuantity() + item.getQuantity());
            productRepository.save(product);
        }

        // cancel order
        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
    }

}
