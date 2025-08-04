package com.think41.customerapi.repository;

import com.think41.customerapi.entity.User;
import com.think41.customerapi.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    
    /**
     * Count orders for a specific user
     */
    @Query(value = "SELECT COUNT(*) FROM orders WHERE user_id = :userId", nativeQuery = true)
    long countOrdersByUserId(@Param("userId") Integer userId);
    
    /**
     * Find user with order count by ID using native SQL
     */
    @Query(value = "SELECT u.*, COALESCE(order_counts.order_count, 0) as order_count " +
           "FROM users u " +
           "LEFT JOIN (SELECT user_id, COUNT(*) as order_count FROM orders GROUP BY user_id) order_counts " +
           "ON u.id = order_counts.user_id " +
           "WHERE u.id = :id",
           nativeQuery = true)
    Object[] findUserWithOrderCountById(@Param("id") Integer id);
    
    /**
     * Find all users with their order counts using native SQL
     */
    @Query(value = "SELECT u.*, COALESCE(order_counts.order_count, 0) as order_count " +
           "FROM users u " +
           "LEFT JOIN (SELECT user_id, COUNT(*) as order_count FROM orders GROUP BY user_id) order_counts " +
           "ON u.id = order_counts.user_id " +
           "ORDER BY u.id",
           countQuery = "SELECT COUNT(*) FROM users",
           nativeQuery = true)
    Page<Object[]> findAllUsersWithOrderCount(Pageable pageable);
    
    /**
     * Search users by name or email with order count using native SQL
     */
    @Query(value = "SELECT u.*, COALESCE(order_counts.order_count, 0) as order_count " +
           "FROM users u " +
           "LEFT JOIN (SELECT user_id, COUNT(*) as order_count FROM orders GROUP BY user_id) order_counts " +
           "ON u.id = order_counts.user_id " +
           "WHERE LOWER(u.first_name) LIKE LOWER(CONCAT('%', :search, '%')) " +
           "OR LOWER(u.last_name) LIKE LOWER(CONCAT('%', :search, '%')) " +
           "OR LOWER(u.email) LIKE LOWER(CONCAT('%', :search, '%')) " +
           "ORDER BY u.id",
           countQuery = "SELECT COUNT(*) FROM users u " +
                       "WHERE LOWER(u.first_name) LIKE LOWER(CONCAT('%', :search, '%')) " +
                       "OR LOWER(u.last_name) LIKE LOWER(CONCAT('%', :search, '%')) " +
                       "OR LOWER(u.email) LIKE LOWER(CONCAT('%', :search, '%'))",
           nativeQuery = true)
    Page<Object[]> searchUsersWithOrderCount(@Param("search") String search, Pageable pageable);
    
    /**
     * Find users by country with order count using native SQL
     */
    @Query(value = "SELECT u.*, COALESCE(order_counts.order_count, 0) as order_count " +
           "FROM users u " +
           "LEFT JOIN (SELECT user_id, COUNT(*) as order_count FROM orders GROUP BY user_id) order_counts " +
           "ON u.id = order_counts.user_id " +
           "WHERE u.country = :country " +
           "ORDER BY u.id",
           countQuery = "SELECT COUNT(*) FROM users WHERE country = :country",
           nativeQuery = true)
    Page<Object[]> findUsersByCountryWithOrderCount(@Param("country") String country, Pageable pageable);
}