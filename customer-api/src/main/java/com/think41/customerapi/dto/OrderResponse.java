package com.think41.customerapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;

public class OrderResponse {
    
    @JsonProperty("order_id")
    private Integer orderId;
    
    @JsonProperty("user_id")
    private Integer userId;
    
    private String status;
    private String gender;
    
    @JsonProperty("created_at")
    private OffsetDateTime createdAt;
    
    @JsonProperty("returned_at")
    private OffsetDateTime returnedAt;
    
    @JsonProperty("shipped_at")
    private OffsetDateTime shippedAt;
    
    @JsonProperty("delivered_at")
    private OffsetDateTime deliveredAt;
    
    @JsonProperty("num_of_item")
    private Integer numOfItem;
    
    // Customer details (optional, for detailed view)
    @JsonProperty("customer_name")
    private String customerName;
    
    @JsonProperty("customer_email")
    private String customerEmail;
    
    // Constructors
    public OrderResponse() {}
    
    public OrderResponse(Integer orderId, Integer userId, String status, String gender,
                        OffsetDateTime createdAt, OffsetDateTime returnedAt,
                        OffsetDateTime shippedAt, OffsetDateTime deliveredAt, Integer numOfItem) {
        this.orderId = orderId;
        this.userId = userId;
        this.status = status;
        this.gender = gender;
        this.createdAt = createdAt;
        this.returnedAt = returnedAt;
        this.shippedAt = shippedAt;
        this.deliveredAt = deliveredAt;
        this.numOfItem = numOfItem;
    }
    
    public OrderResponse(Integer orderId, Integer userId, String status, String gender,
                        OffsetDateTime createdAt, OffsetDateTime returnedAt,
                        OffsetDateTime shippedAt, OffsetDateTime deliveredAt, Integer numOfItem,
                        String customerName, String customerEmail) {
        this(orderId, userId, status, gender, createdAt, returnedAt, shippedAt, deliveredAt, numOfItem);
        this.customerName = customerName;
        this.customerEmail = customerEmail;
    }
    
    // Getters and Setters
    public Integer getOrderId() { return orderId; }
    public void setOrderId(Integer orderId) { this.orderId = orderId; }
    
    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
    
    public OffsetDateTime getReturnedAt() { return returnedAt; }
    public void setReturnedAt(OffsetDateTime returnedAt) { this.returnedAt = returnedAt; }
    
    public OffsetDateTime getShippedAt() { return shippedAt; }
    public void setShippedAt(OffsetDateTime shippedAt) { this.shippedAt = shippedAt; }
    
    public OffsetDateTime getDeliveredAt() { return deliveredAt; }
    public void setDeliveredAt(OffsetDateTime deliveredAt) { this.deliveredAt = deliveredAt; }
    
    public Integer getNumOfItem() { return numOfItem; }
    public void setNumOfItem(Integer numOfItem) { this.numOfItem = numOfItem; }
    
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    
    public String getCustomerEmail() { return customerEmail; }
    public void setCustomerEmail(String customerEmail) { this.customerEmail = customerEmail; }
}