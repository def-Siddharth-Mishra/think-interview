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
     * Find all users with their order counts (paginated)
     */
    @Query("SELECT u, COUNT(o.orderId) as orderCount " +
           "FROM User u LEFT JOIN u.orders o " +
           "GROUP BY u.id, u.firstName, u.lastName, u.email, u.age, u.gender, u.state, u.streetAddress, u.postalCode, u.city, u.country, u.latitude, u.longitude, u.trafficSource, u.createdAt " +
           "ORDER BY u.id")
    Page<Object[]> findAllUsersWithOrderCount(Pageable pageable);
    
    /**
     * Search users by name or email with order count
     */
    @Query("SELECT u, COUNT(o.orderId) as orderCount " +
           "FROM User u LEFT JOIN u.orders o " +
           "WHERE LOWER(u.firstName) LIKE LOWER(CONCAT('%', :search, '%')) " +
           "OR LOWER(u.lastName) LIKE LOWER(CONCAT('%', :search, '%')) " +
           "OR LOWER(u.email) LIKE LOWER(CONCAT('%', :search, '%')) " +
           "GROUP BY u.id, u.firstName, u.lastName, u.email, u.age, u.gender, u.state, u.streetAddress, u.postalCode, u.city, u.country, u.latitude, u.longitude, u.trafficSource, u.createdAt " +
           "ORDER BY u.id")
    Page<Object[]> searchUsersWithOrderCount(@Param("search") String search, Pageable pageable);
    
    /**
     * Find users by country with order count
     */
    @Query("SELECT u, COUNT(o.orderId) as orderCount " +
           "FROM User u LEFT JOIN u.orders o " +
           "WHERE u.country = :country " +
           "GROUP BY u.id, u.firstName, u.lastName, u.email, u.age, u.gender, u.state, u.streetAddress, u.postalCode, u.city, u.country, u.latitude, u.longitude, u.trafficSource, u.createdAt " +
           "ORDER BY u.id")
    Page<Object[]> findUsersByCountryWithOrderCount(@Param("country") String country, Pageable pageable);
}