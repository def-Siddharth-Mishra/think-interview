package com.think41.customerapi.controller;

import com.think41.customerapi.dto.CustomerResponse;
import com.think41.customerapi.dto.PagedResponse;
import com.think41.customerapi.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customers")
@CrossOrigin(origins = "http://localhost:5173")
@Tag(name = "Customer API", description = "APIs for managing customer data")
public class CustomerController {
    
    @Autowired
    private CustomerService customerService;
    
    @GetMapping
    @Operation(summary = "Get all customers", description = "Retrieve a paginated list of all customers with their order counts")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved customers"),
        @ApiResponse(responseCode = "400", description = "Invalid request parameters")
    })
    public ResponseEntity<PagedResponse<CustomerResponse>> getAllCustomers(
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            
            @Parameter(description = "Page size", example = "20")
            @RequestParam(defaultValue = "20") int size,
            
            @Parameter(description = "Search term for name or email")
            @RequestParam(required = false) String search,
            
            @Parameter(description = "Filter by country")
            @RequestParam(required = false) String country) {
        
        // Validate pagination parameters
        if (page < 0) {
            throw new IllegalArgumentException("Page number cannot be negative");
        }
        if (size <= 0 || size > 100) {
            throw new IllegalArgumentException("Page size must be between 1 and 100");
        }
        
        PagedResponse<CustomerResponse> customers = customerService.getAllCustomers(page, size, search, country);
        return ResponseEntity.ok(customers);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get customer by ID", description = "Retrieve a specific customer by their ID with order count")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved customer"),
        @ApiResponse(responseCode = "400", description = "Invalid customer ID format"),
        @ApiResponse(responseCode = "404", description = "Customer not found")
    })
    public ResponseEntity<CustomerResponse> getCustomerById(
            @Parameter(description = "Customer ID", example = "1")
            @PathVariable Integer id) {
        
        if (id <= 0) {
            throw new IllegalArgumentException("Customer ID must be a positive integer");
        }
        
        CustomerResponse customer = customerService.getCustomerById(id);
        return ResponseEntity.ok(customer);
    }
    
    @GetMapping("/count")
    @Operation(summary = "Get total customer count", description = "Get the total number of customers in the system")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved customer count")
    public ResponseEntity<Long> getTotalCustomerCount() {
        long count = customerService.getTotalCustomerCount();
        return ResponseEntity.ok(count);
    }
    
    @GetMapping("/{id}/exists")
    @Operation(summary = "Check if customer exists", description = "Check if a customer exists by their ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully checked customer existence"),
        @ApiResponse(responseCode = "400", description = "Invalid customer ID format")
    })
    public ResponseEntity<Boolean> customerExists(
            @Parameter(description = "Customer ID", example = "1")
            @PathVariable Integer id) {
        
        if (id <= 0) {
            throw new IllegalArgumentException("Customer ID must be a positive integer");
        }
        
        boolean exists = customerService.customerExists(id);
        return ResponseEntity.ok(exists);
    }
}