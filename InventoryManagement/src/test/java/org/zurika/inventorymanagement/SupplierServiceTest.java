package org.zurika.inventorymanagement;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.util.HashSet;
import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.zurika.inventorymanagement.exception.ResourceNotFoundException;
import org.zurika.inventorymanagement.model.*;
import org.zurika.inventorymanagement.model.Supplier;
import org.zurika.inventorymanagement.repository.*;
import org.zurika.inventorymanagement.service.SupplierService;


@ExtendWith(MockitoExtension.class)
class SupplierServiceTest {

    @Mock
    private SupplierRepository supplierRepository;

    @InjectMocks
    private SupplierService supplierService;

    private Supplier supplier;

    @BeforeEach
    void setUp() {
        supplier = new Supplier();
        supplier.setId(1L);
        supplier.setName("Test Supplier");
        supplier.setContactDetails("test@supplier.com");

        Product product1 = new Product();
        product1.setId(1L);
        product1.setName("Product 1");

        Product product2 = new Product();
        product2.setId(2L);
        product2.setName("Product 2");

        Set<Product> products = new HashSet<>();
        products.add(product1);
        products.add(product2);

        supplier.setProducts(products);
    }

    @Test
    void getProductsBySupplierId_ShouldReturnProducts() {
        when(supplierRepository.findById(1L)).thenReturn(Optional.of(supplier));

        Set<Product> products = supplierService.getProductsBySupplierId(1L);

        assertNotNull(products);
        assertEquals(2, products.size());
        verify(supplierRepository).findById(1L);
    }

    @Test
    void getProductsBySupplierId_ShouldThrowExceptionWhenSupplierNotFound() {
        when(supplierRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> 
                supplierService.getProductsBySupplierId(1L));

        assertEquals("Supplier not found with id 1", exception.getMessage());
        verify(supplierRepository).findById(1L);
    }
} 
