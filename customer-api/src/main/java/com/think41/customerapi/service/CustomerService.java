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
        
        // Count orders for this user
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
     * Map database result to CustomerResponse DTO
     */
    private CustomerResponse mapToCustomerResponse(Object[] result) {
        User user = (User) result[0];
        Long orderCount = (Long) result[1];
        
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
}