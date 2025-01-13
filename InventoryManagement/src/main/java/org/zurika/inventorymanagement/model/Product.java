package org.zurika.inventorymanagement.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

@Entity
@Data
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    @NotNull(message = "Product name is required")
    @Size(min = 2, max = 100, message = "Product name must be between 2 and 100 characters")
    private String name;
    
    @Column(nullable = false)
    @NotNull(message = "Category is required")
    private String category;
    
    @Column(nullable = false)
    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than zero")
    private double price;
    
    @Column(nullable = false)
    @NotNull(message = "Quantity is required")
    @Min(value = 0, message = "Quantity cannot be negative")
    private int quantity;
    
    @Column(nullable = false)
    private LocalDateTime dateAdded;

    @ManyToOne
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;
}
