package com.think41.customerapi.entity;

import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "orders")
public class Order {
    
    @Id
    @Column(name = "order_id")
    private Integer orderId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Column(nullable = false, length = 50)
    private String status;
    
    @Column(length = 1, columnDefinition = "CHAR(1)")
    private String gender;
    
    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;
    
    @Column(name = "returned_at")
    private OffsetDateTime returnedAt;
    
    @Column(name = "shipped_at")
    private OffsetDateTime shippedAt;
    
    @Column(name = "delivered_at")
    private OffsetDateTime deliveredAt;
    
    @Column(name = "num_of_item")
    private Integer numOfItem;
    
    // Constructors
    public Order() {}
    
    public Order(Integer orderId, User user, String status) {
        this.orderId = orderId;
        this.user = user;
        this.status = status;
    }
    
    // Getters and Setters
    public Integer getOrderId() { return orderId; }
    public void setOrderId(Integer orderId) { this.orderId = orderId; }
    
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    
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
}