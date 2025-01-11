package org.zurika.inventorymanagement.model;

import java.util.Set;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

@Entity
@Data
public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    @NotNull(message = "Supplier name is required")
    @Size(min = 2, max = 100, message = "Supplier name must be between 2 and 100 characters")
    private String name;

    @Column(nullable = false)
    @NotNull(message = "Contact details are required")
    private String contactDetails;

    @OneToMany(mappedBy = "supplier")
    private Set<Product> products;
}
