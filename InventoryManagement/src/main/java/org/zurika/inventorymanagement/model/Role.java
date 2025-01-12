package org.zurika.inventorymanagement.model;

import java.util.Set;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @ManyToMany(mappedBy = "roles")
    private Set<User> users;
}
