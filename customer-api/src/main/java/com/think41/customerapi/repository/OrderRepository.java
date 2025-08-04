package com.think41.customerapi.repository;

import com.think41.customerapi.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    
    /**
     * Find all orders for a specific customer with pagination
     */
    @Query("SELECT o FROM Order o WHERE o.userId = :userId ORDER BY o.createdAt DESC")
    Page<Order> findByUserId(@Param("userId") Integer userId, Pageable pageable);
    
    /**
     * Find order by ID with customer details using JPA
     */
    @Query("SELECT o FROM Order o JOIN FETCH o.user WHERE o.orderId = :orderId")
    Optional<Order> findOrderWithUserByOrderId(@Param("orderId") Integer orderId);
    
    /**
     * Find orders for a customer with customer details using JPA
     */
    @Query("SELECT o FROM Order o JOIN FETCH o.user WHERE o.userId = :userId ORDER BY o.createdAt DESC")
    Page<Order> findOrdersWithUserByUserId(@Param("userId") Integer userId, Pageable pageable);
    
    /**
     * Count orders for a specific customer
     */
    long countByUserId(Integer userId);
    
    /**
     * Check if order exists for a specific customer
     */
    boolean existsByOrderIdAndUserId(Integer orderId, Integer userId);
}