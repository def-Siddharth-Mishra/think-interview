package com.think41.customerapi.controller;

import com.think41.customerapi.dto.CustomerResponse;
import com.think41.customerapi.dto.PagedResponse;
import com.think41.customerapi.exception.CustomerNotFoundException;
import com.think41.customerapi.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerController.class)
public class CustomerControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private CustomerService customerService;
    
    @Test
    public void testGetAllCustomers_Success() throws Exception {
        // Mock data
        CustomerResponse customer1 = new CustomerResponse(1, "John", "Doe", "john@example.com",
                30, "M", "CA", "123 Main St", "12345", "Los Angeles", "USA",
                new BigDecimal("34.0522"), new BigDecimal("-118.2437"), "Search",
                OffsetDateTime.now(), 5L);
        
        CustomerResponse customer2 = new CustomerResponse(2, "Jane", "Smith", "jane@example.com",
                25, "F", "NY", "456 Oak Ave", "67890", "New York", "USA",
                new BigDecimal("40.7128"), new BigDecimal("-74.0060"), "Email",
                OffsetDateTime.now(), 3L);
        
        PagedResponse<CustomerResponse> pagedResponse = new PagedResponse<>(
                Arrays.asList(customer1, customer2), 0, 20, 2L, 1, true, true);
        
        when(customerService.getAllCustomers(anyInt(), anyInt(), any(), any()))
                .thenReturn(pagedResponse);
        
        // Test
        mockMvc.perform(get("/api/customers")
                .param("page", "0")
                .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.page_number").value(0))
                .andExpect(jsonPath("$.page_size").value(20))
                .andExpect(jsonPath("$.total_elements").value(2));
    }
    
    @Test
    public void testGetCustomerById_Success() throws Exception {
        // Mock data
        CustomerResponse customer = new CustomerResponse(1, "John", "Doe", "john@example.com",
                30, "M", "CA", "123 Main St", "12345", "Los Angeles", "USA",
                new BigDecimal("34.0522"), new BigDecimal("-118.2437"), "Search",
                OffsetDateTime.now(), 5L);
        
        when(customerService.getCustomerById(1)).thenReturn(customer);
        
        // Test
        mockMvc.perform(get("/api/customers/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.first_name").value("John"))
                .andExpect(jsonPath("$.last_name").value("Doe"))
                .andExpect(jsonPath("$.email").value("john@example.com"))
                .andExpect(jsonPath("$.order_count").value(5));
    }
    
    @Test
    public void testGetCustomerById_NotFound() throws Exception {
        when(customerService.getCustomerById(999))
                .thenThrow(new CustomerNotFoundException("Customer not found with ID: 999"));
        
        mockMvc.perform(get("/api/customers/999"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").value("Customer Not Found"))
                .andExpect(jsonPath("$.status").value(404));
    }
    
    @Test
    public void testGetCustomerById_InvalidId() throws Exception {
        mockMvc.perform(get("/api/customers/invalid"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").value("Invalid Parameter"))
                .andExpect(jsonPath("$.status").value(400));
    }
    
    @Test
    public void testGetTotalCustomerCount() throws Exception {
        when(customerService.getTotalCustomerCount()).thenReturn(100000L);
        
        mockMvc.perform(get("/api/customers/count"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string("100000"));
    }
}