package org.zurika.inventorymanagement.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;
    
    @Column(nullable = false)
    private String category;
    
    @Column(nullable = false)
    private BigDecimal price;
    
    @Column(nullable = false)
    private int quantity;
    
    @Column(nullable = false)
    private LocalDateTime dateAdded;

    @ManyToOne
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;
}
