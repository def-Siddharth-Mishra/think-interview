package com.think41.customerapi.service;

import com.think41.customerapi.dto.CustomerResponse;
import com.think41.customerapi.dto.PagedResponse;
import com.think41.customerapi.entity.User;
import com.think41.customerapi.exception.CustomerNotFoundException;
import com.think41.customerapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerService {
    
    @Autowired
    private UserRepository userRepository;
    
    /**
     * Get all customers with pagination
     */
    public PagedResponse<CustomerResponse> getAllCustomers(int page, int size, String search, String country) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Object[]> userPage;
        
        if (search != null && !search.trim().isEmpty()) {
            userPage = userRepository.searchUsersWithOrderCount(search.trim(), pageable);
        } else if (country != null && !country.trim().isEmpty()) {
            userPage = userRepository.findUsersByCountryWithOrderCount(country.trim(), pageable);
        } else {
            userPage = userRepository.findAllUsersWithOrderCount(pageable);
        }
        
        List<CustomerResponse> customers = userPage.getContent().stream()
                .map(this::mapToCustomerResponse)
                .collect(Collectors.toList());
        
        return new PagedResponse<>(
                customers,
                userPage.getNumber(),
                userPage.getSize(),
                userPage.getTotalElements(),
                userPage.getTotalPages(),
                userPage.isFirst(),
                userPage.isLast()
        );
    }
    
    /**
     * Get customer by ID with order count
     */
    public CustomerResponse getCustomerById(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with ID: " + id));
        
        // Get order count separately
        long orderCount = userRepository.countOrdersByUserId(id);
        
        return new CustomerResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getAge(),
                user.getGender(),
                user.getState(),
                user.getStreetAddress(),
                user.getPostalCode(),
                user.getCity(),
                user.getCountry(),
                user.getLatitude(),
                user.getLongitude(),
                user.getTrafficSource(),
                user.getCreatedAt(),
                orderCount
        );
    }
    
    /**
     * Check if customer exists
     */
    public boolean customerExists(Integer id) {
        return userRepository.existsById(id);
    }
    
    /**
     * Get total customer count
     */
    public long getTotalCustomerCount() {
        return userRepository.count();
    }
    
    /**
     * Map native query result to CustomerResponse DTO
     */
    private CustomerResponse mapToCustomerResponse(Object[] result) {
        // Native query returns raw database values
        Integer id = (Integer) result[0];
        String firstName = (String) result[1];
        String lastName = (String) result[2];
        String email = (String) result[3];
        Integer age = (Integer) result[4];
        String gender = result[5] != null ? result[5].toString().trim() : null;
        String state = (String) result[6];
        String streetAddress = (String) result[7];
        String postalCode = (String) result[8];
        String city = (String) result[9];
        String country = (String) result[10];
        java.math.BigDecimal latitude = (java.math.BigDecimal) result[11];
        java.math.BigDecimal longitude = (java.math.BigDecimal) result[12];
        String trafficSource = (String) result[13];
        java.sql.Timestamp createdAtTimestamp = (java.sql.Timestamp) result[14];
        java.time.OffsetDateTime createdAt = createdAtTimestamp != null ? 
            createdAtTimestamp.toLocalDateTime().atOffset(java.time.ZoneOffset.UTC) : null;
        Long orderCount = result[15] != null ? ((Number) result[15]).longValue() : 0L;
        
        return new CustomerResponse(
                id, firstName, lastName, email, age, gender, state,
                streetAddress, postalCode, city, country, latitude, longitude,
                trafficSource, createdAt, orderCount
        );
    }
}