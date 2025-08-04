package com.think41.customerapi.service;

import com.think41.customerapi.dto.OrderResponse;
import com.think41.customerapi.dto.PagedResponse;
import com.think41.customerapi.entity.Order;
import com.think41.customerapi.exception.CustomerNotFoundException;
import com.think41.customerapi.exception.OrderNotFoundException;
import com.think41.customerapi.repository.OrderRepository;
import com.think41.customerapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
    
    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    /**
     * Get all orders for a specific customer with pagination
     */
    public PagedResponse<OrderResponse> getOrdersByCustomerId(Integer customerId, int page, int size) {
        // First verify customer exists
        if (!userRepository.existsById(customerId)) {
            throw new CustomerNotFoundException("Customer not found with ID: " + customerId);
        }
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Order> orderPage = orderRepository.findOrdersWithUserByUserId(customerId, pageable);
        
        List<OrderResponse> orders = orderPage.getContent().stream()
                .map(this::mapOrderToResponse)
                .collect(Collectors.toList());
        
        return new PagedResponse<>(
                orders,
                orderPage.getNumber(),
                orderPage.getSize(),
                orderPage.getTotalElements(),
                orderPage.getTotalPages(),
                orderPage.isFirst(),
                orderPage.isLast()
        );
    }
    
    /**
     * Get specific order details by order ID
     */
    public OrderResponse getOrderById(Integer orderId) {
        Order order = orderRepository.findOrderWithUserByOrderId(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with ID: " + orderId));
        
        return mapOrderToResponse(order);
    }
    
    /**
     * Get specific order for a customer (validates ownership)
     */
    public OrderResponse getOrderByIdForCustomer(Integer customerId, Integer orderId) {
        // First verify customer exists
        if (!userRepository.existsById(customerId)) {
            throw new CustomerNotFoundException("Customer not found with ID: " + customerId);
        }
        
        // Check if order exists and belongs to customer
        if (!orderRepository.existsByOrderIdAndUserId(orderId, customerId)) {
            throw new OrderNotFoundException("Order not found with ID: " + orderId + " for customer: " + customerId);
        }
        
        return getOrderById(orderId);
    }
    
    /**
     * Get order count for a customer
     */
    public long getOrderCountByCustomerId(Integer customerId) {
        if (!userRepository.existsById(customerId)) {
            throw new CustomerNotFoundException("Customer not found with ID: " + customerId);
        }
        
        return orderRepository.countByUserId(customerId);
    }
    
    /**
     * Map Order entity to OrderResponse
     */
    private OrderResponse mapOrderToResponse(Order order) {
        String customerName = order.getUser().getFirstName() + " " + order.getUser().getLastName();
        String customerEmail = order.getUser().getEmail();
        
        return new OrderResponse(
                order.getOrderId(),
                order.getUserId(),
                order.getStatus(),
                order.getGender(),
                order.getCreatedAt(),
                order.getReturnedAt(),
                order.getShippedAt(),
                order.getDeliveredAt(),
                order.getNumOfItem(),
                customerName,
                customerEmail
        );
    }
    
    /**
     * Map native query result to OrderResponse with customer details
     */
    private OrderResponse mapToOrderResponseWithCustomer(Object[] result) {
        Integer orderId = (Integer) result[0];
        Integer userId = (Integer) result[1];
        String status = (String) result[2];
        String gender = result[3] != null ? result[3].toString().trim() : null;
        
        // Handle timestamp conversion
        java.sql.Timestamp createdAtTimestamp = (java.sql.Timestamp) result[4];
        OffsetDateTime createdAt = createdAtTimestamp != null ? 
            createdAtTimestamp.toLocalDateTime().atOffset(java.time.ZoneOffset.UTC) : null;
            
        java.sql.Timestamp returnedAtTimestamp = (java.sql.Timestamp) result[5];
        OffsetDateTime returnedAt = returnedAtTimestamp != null ? 
            returnedAtTimestamp.toLocalDateTime().atOffset(java.time.ZoneOffset.UTC) : null;
            
        java.sql.Timestamp shippedAtTimestamp = (java.sql.Timestamp) result[6];
        OffsetDateTime shippedAt = shippedAtTimestamp != null ? 
            shippedAtTimestamp.toLocalDateTime().atOffset(java.time.ZoneOffset.UTC) : null;
            
        java.sql.Timestamp deliveredAtTimestamp = (java.sql.Timestamp) result[7];
        OffsetDateTime deliveredAt = deliveredAtTimestamp != null ? 
            deliveredAtTimestamp.toLocalDateTime().atOffset(java.time.ZoneOffset.UTC) : null;
        
        Integer numOfItem = (Integer) result[8];
        String firstName = (String) result[9];
        String lastName = (String) result[10];
        String email = (String) result[11];
        
        String customerName = firstName + " " + lastName;
        
        return new OrderResponse(
                orderId, userId, status, gender, createdAt, returnedAt, 
                shippedAt, deliveredAt, numOfItem, customerName, email
        );
    }
}