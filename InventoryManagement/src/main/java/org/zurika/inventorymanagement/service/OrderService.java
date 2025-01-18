package org.zurika.inventorymanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.zurika.inventorymanagement.model.*;
import org.zurika.inventorymanagement.repository.*;

import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private JavaMailSender mailSender;

    public Page<Order> findAll(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }

    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    public Order placeOrder(Order order) throws Exception {
        if (order.getOrderItems() == null || order.getOrderItems().isEmpty()) {
            throw new Exception("Order must contain at least one item.");
        }

        for (OrderItem item : order.getOrderItems()) {
            if (item.getQuantity() <= 0) {
                throw new Exception("Order item quantity must be greater than zero.");
            }

            Product product = productRepository.findById(item.getProduct().getId())
                    .orElseThrow(() -> new Exception("Product not found: " + item.getProduct().getId()));

            if (product.getQuantity() < item.getQuantity()) {
                throw new Exception("Insufficient stock for product: " + product.getName());
            }

            product.setQuantity(product.getQuantity() - item.getQuantity());
            productRepository.save(product);
        }

        order.setStatus(OrderStatus.PENDING);
        sendMail("Order Placed", "Your order with ID " 
            + order.getId() + " has been placed successfully.");
        return orderRepository.save(order);
    }

    public Order updateOrderStatus(Long id, OrderStatus status) throws Exception {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new Exception("Order not found"));
        order.setStatus(status);
        sendMail("Order Status Updated", "Your order with ID " 
            + order.getId() + " status is now " + status);
        return orderRepository.save(order);
    }

    public void cancelOrder(Long id) throws Exception {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new Exception("Order not found"));

        if (order.getStatus() == OrderStatus.COMPLETED) {
            throw new Exception("Completed orders cannot be canceled.");
        }

        for (OrderItem item : order.getOrderItems()) {
            Product product = item.getProduct();
            product.setQuantity(product.getQuantity() + item.getQuantity());
            productRepository.save(product);
        }

        order.setStatus(OrderStatus.CANCELLED);
        sendMail("Order Canceled", "Your order with ID " 
            + order.getId() + " has been canceled.");
        orderRepository.save(order);
    }

    public List<Order> findOrdersByDateRange(LocalDate startDate, LocalDate endDate) {
        return orderRepository.findAllByOrderDateBetween(startDate.atStartOfDay(), 
            endDate.atTime(23, 59, 59));
    }

    private void sendMail(String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(System.getenv("NOTIFICATION_EMAIL"));
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }
}
