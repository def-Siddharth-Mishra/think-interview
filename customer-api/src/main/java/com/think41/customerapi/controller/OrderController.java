package com.think41.customerapi.controller;

import com.think41.customerapi.dto.OrderResponse;
import com.think41.customerapi.dto.PagedResponse;
import com.think41.customerapi.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class OrderController {
    
    @Autowired
    private OrderService orderService;
    
    /**
     * Get all orders for a specific customer
     * GET /api/customers/{customerId}/orders
     */
    @GetMapping("/customers/{customerId}/orders")
    public ResponseEntity<PagedResponse<OrderResponse>> getOrdersByCustomerId(
            @PathVariable Integer customerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        PagedResponse<OrderResponse> orders = orderService.getOrdersByCustomerId(customerId, page, size);
        return ResponseEntity.ok(orders);
    }
    
    /**
     * Get specific order details for a customer
     * GET /api/customers/{customerId}/orders/{orderId}
     */
    @GetMapping("/customers/{customerId}/orders/{orderId}")
    public ResponseEntity<OrderResponse> getOrderByIdForCustomer(
            @PathVariable Integer customerId,
            @PathVariable Integer orderId) {
        
        OrderResponse order = orderService.getOrderByIdForCustomer(customerId, orderId);
        return ResponseEntity.ok(order);
    }
    
    /**
     * Get specific order details by order ID (global lookup)
     * GET /api/orders/{orderId}
     */
    @GetMapping("/orders/{orderId}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable Integer orderId) {
        OrderResponse order = orderService.getOrderById(orderId);
        return ResponseEntity.ok(order);
    }
    
    /**
     * Get order count for a customer
     * GET /api/customers/{customerId}/orders/count
     */
    @GetMapping("/customers/{customerId}/orders/count")
    public ResponseEntity<Long> getOrderCountByCustomerId(@PathVariable Integer customerId) {
        long count = orderService.getOrderCountByCustomerId(customerId);
        return ResponseEntity.ok(count);
    }
}