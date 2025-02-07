package org.zurika.inventorymanagement;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.zurika.inventorymanagement.repository.ProductRepository;
import org.zurika.inventorymanagement.service.SupplierService;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false) // Disable security filters
class SupplierControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SupplierService supplierService;

    @MockBean
    private ProductRepository productRepository;

    @Test
    void createSupplier_ShouldReturn201() throws Exception {
        String supplierJson = """
                {
                    "name": "New Supplier",
                    "contactDetails": "newsupplier@example.com"
                }
                """;

        mockMvc.perform(post("/api/suppliers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(supplierJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("New Supplier"))
                .andExpect(jsonPath("$.contactDetails").value("newsupplier@example.com"));
    }

    @Test
    void createSupplier_ShouldReturn400ForDuplicateName() throws Exception {
        String supplierJson = """
                {
                    "name": "Existing Supplier",
                    "contactDetails": "existing@example.com"
                }
                """;

        mockMvc.perform(post("/api/suppliers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(supplierJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllSuppliers_ShouldReturn200() throws Exception {
        mockMvc.perform(get("/api/suppliers")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getSupplierById_ShouldReturn200() throws Exception {
        mockMvc.perform(get("/api/suppliers/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Supplier"))
                .andExpect(jsonPath("$.contactDetails").value("test@example.com"));
    }

    @Test
    void getSupplierById_ShouldReturn404() throws Exception {
        mockMvc.perform(get("/api/suppliers/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void getProductsBySupplierId_ShouldReturn200() throws Exception {
        mockMvc.perform(get("/api/suppliers/1/products")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getProductsBySupplierId_ShouldReturn404() throws Exception {
        mockMvc.perform(get("/api/suppliers/999/products")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
