package org.zurika.inventorymanagement.model;

import java.util.Set;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String contactDetails;

    @OneToMany(mappedBy = "supplier")
    private Set<Product> products;
}
