package org.zurika.inventorymanagement;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.SimpleMailMessage;
import org.zurika.inventorymanagement.model.*;
import org.zurika.inventorymanagement.repository.*;
import org.zurika.inventorymanagement.service.OrderService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private OrderService orderService;

    private Order order;
    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setId(1L);
        product.setName("Test Product");
        product.setQuantity(10);
        product.setPrice(100.0);

        OrderItem item = new OrderItem();
        item.setProduct(product);
        item.setQuantity(5);

        order = new Order();
        order.setId(1L);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);
        order.setOrderItems(Set.of(item));
    }

    @Test
    void placeOrder_ShouldPlaceOrderSuccessfully() throws Exception {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        Order savedOrder = orderService.placeOrder(order);

        assertNotNull(savedOrder);
        assertEquals(OrderStatus.PENDING, savedOrder.getStatus());
        verify(productRepository).save(product);
        verify(orderRepository).save(order);
        verify(mailSender).send(any(SimpleMailMessage.class));
    }

    @Test
    void placeOrder_ShouldThrowExceptionForInsufficientStock() {
        product.setQuantity(4); // Insufficient stock
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        Exception exception = assertThrows(Exception.class, () -> orderService.placeOrder(order));
        assertEquals("Insufficient stock for product: Test Product", exception.getMessage());

        verify(productRepository, never()).save(product);
        verify(orderRepository, never()).save(order);
    }

    @Test
    void updateOrderStatus_ShouldUpdateSuccessfully() throws Exception {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        Order updatedOrder = orderService.updateOrderStatus(1L, OrderStatus.COMPLETED);

        assertNotNull(updatedOrder);
        assertEquals(OrderStatus.COMPLETED, updatedOrder.getStatus());
        verify(orderRepository).save(order);
        verify(mailSender).send(any(SimpleMailMessage.class));
    }

    @Test
void cancelOrder_ShouldRestoreStockAndCancelOrder() throws Exception {
    // Mock order repository to return the test order
    when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
    when(orderRepository.save(any(Order.class))).thenReturn(order);

    // Capture the product to verify stock restoration
    ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);

    // Simulate the ProductRepository save behavior to update product quantity
    doAnswer(invocation -> {
        Product updatedProduct = invocation.getArgument(0);
        product.setQuantity(updatedProduct.getQuantity()); // Reflect updated quantity
        return updatedProduct;
    }).when(productRepository).save(any(Product.class));

    // Perform cancel order
    orderService.cancelOrder(1L);

    // Assert the order's status
    assertEquals(OrderStatus.CANCELLED, order.getStatus());

    // Assert the product quantity was restored
    verify(productRepository).save(productCaptor.capture());
    Product savedProduct = productCaptor.getValue();
    assertEquals(15, savedProduct.getQuantity()); // Restored stock (10 initial + 5 from canceled order)

    // Verify other interactions
    verify(orderRepository).save(order);
    verify(mailSender).send(any(SimpleMailMessage.class));
}


    @Test
    void cancelOrder_ShouldThrowExceptionForCompletedOrder() {
        order.setStatus(OrderStatus.COMPLETED);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        Exception exception = assertThrows(Exception.class, () -> orderService.cancelOrder(1L));
        assertEquals("Completed orders cannot be canceled.", exception.getMessage());

        verify(productRepository, never()).save(product);
        verify(orderRepository, never()).save(order);
    }

    @Test
    void findOrdersByDateRange_ShouldReturnOrders() {
        LocalDate startDate = LocalDate.now().minusDays(7);
        LocalDate endDate = LocalDate.now();
        when(orderRepository.findAllByOrderDateBetween(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(List.of(order));

        List<Order> orders = orderService.findOrdersByDateRange(startDate, endDate);

        assertNotNull(orders);
        assertEquals(1, orders.size());
        assertEquals(order.getId(), orders.get(0).getId());
    }
}