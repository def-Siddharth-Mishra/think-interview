package com.think41.customerapi.controller;

import com.think41.customerapi.dto.OrderResponse;
import com.think41.customerapi.dto.PagedResponse;
import com.think41.customerapi.exception.CustomerNotFoundException;
import com.think41.customerapi.exception.OrderNotFoundException;
import com.think41.customerapi.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.OffsetDateTime;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
public class OrderControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private OrderService orderService;
    
    @Test
    public void testGetOrdersByCustomerId_Success() throws Exception {
        // Arrange
        OrderResponse order1 = new OrderResponse(1001, 1, "completed", "M", 
                OffsetDateTime.now(), null, OffsetDateTime.now(), OffsetDateTime.now(), 2,
                "John Doe", "john@example.com");
        OrderResponse order2 = new OrderResponse(1002, 1, "shipped", "M", 
                OffsetDateTime.now(), null, OffsetDateTime.now(), null, 1,
                "John Doe", "john@example.com");
        
        PagedResponse<OrderResponse> pagedResponse = new PagedResponse<>(
                Arrays.asList(order1, order2), 0, 10, 2L, 1, true, true);
        
        when(orderService.getOrdersByCustomerId(eq(1), eq(0), eq(10)))
                .thenReturn(pagedResponse);
        
        // Act & Assert
        mockMvc.perform(get("/api/customers/1/orders")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].order_id").value(1001))
                .andExpect(jsonPath("$.content[0].status").value("completed"))
                .andExpect(jsonPath("$.content[0].customer_name").value("John Doe"))
                .andExpect(jsonPath("$.total_elements").value(2))
                .andExpect(jsonPath("$.total_pages").value(1));
    }
    
    @Test
    public void testGetOrdersByCustomerId_CustomerNotFound() throws Exception {
        // Arrange
        when(orderService.getOrdersByCustomerId(eq(999), eq(0), eq(10)))
                .thenThrow(new CustomerNotFoundException("Customer not found with ID: 999"));
        
        // Act & Assert
        mockMvc.perform(get("/api/customers/999/orders"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").value("Customer Not Found"))
                .andExpect(jsonPath("$.message").value("Customer not found with ID: 999"))
                .andExpect(jsonPath("$.status").value(404));
    }
    
    @Test
    public void testGetOrderByIdForCustomer_Success() throws Exception {
        // Arrange
        OrderResponse order = new OrderResponse(1001, 1, "completed", "M", 
                OffsetDateTime.now(), null, OffsetDateTime.now(), OffsetDateTime.now(), 2,
                "John Doe", "john@example.com");
        
        when(orderService.getOrderByIdForCustomer(eq(1), eq(1001)))
                .thenReturn(order);
        
        // Act & Assert
        mockMvc.perform(get("/api/customers/1/orders/1001"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.order_id").value(1001))
                .andExpect(jsonPath("$.user_id").value(1))
                .andExpect(jsonPath("$.status").value("completed"))
                .andExpect(jsonPath("$.customer_name").value("John Doe"))
                .andExpect(jsonPath("$.customer_email").value("john@example.com"));
    }
    
    @Test
    public void testGetOrderByIdForCustomer_OrderNotFound() throws Exception {
        // Arrange
        when(orderService.getOrderByIdForCustomer(eq(1), eq(999)))
                .thenThrow(new OrderNotFoundException("Order not found with ID: 999 for customer: 1"));
        
        // Act & Assert
        mockMvc.perform(get("/api/customers/1/orders/999"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").value("Order Not Found"))
                .andExpect(jsonPath("$.message").value("Order not found with ID: 999 for customer: 1"))
                .andExpect(jsonPath("$.status").value(404));
    }
    
    @Test
    public void testGetOrderById_Success() throws Exception {
        // Arrange
        OrderResponse order = new OrderResponse(1001, 1, "completed", "M", 
                OffsetDateTime.now(), null, OffsetDateTime.now(), OffsetDateTime.now(), 2,
                "John Doe", "john@example.com");
        
        when(orderService.getOrderById(eq(1001)))
                .thenReturn(order);
        
        // Act & Assert
        mockMvc.perform(get("/api/orders/1001"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.order_id").value(1001))
                .andExpect(jsonPath("$.status").value("completed"))
                .andExpect(jsonPath("$.customer_name").value("John Doe"));
    }
    
    @Test
    public void testGetOrderCountByCustomerId_Success() throws Exception {
        // Arrange
        when(orderService.getOrderCountByCustomerId(eq(1)))
                .thenReturn(5L);
        
        // Act & Assert
        mockMvc.perform(get("/api/customers/1/orders/count"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string("5"));
    }
    
    @Test
    public void testGetOrdersByCustomerId_EmptyResult() throws Exception {
        // Arrange
        PagedResponse<OrderResponse> emptyResponse = new PagedResponse<>(
                Arrays.asList(), 0, 10, 0L, 0, true, true);
        
        when(orderService.getOrdersByCustomerId(eq(1), eq(0), eq(10)))
                .thenReturn(emptyResponse);
        
        // Act & Assert
        mockMvc.perform(get("/api/customers/1/orders"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(0))
                .andExpect(jsonPath("$.total_elements").value(0))
                .andExpect(jsonPath("$.total_pages").value(0));
    }
}