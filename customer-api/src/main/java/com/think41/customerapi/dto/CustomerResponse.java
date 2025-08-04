package com.think41.customerapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

public class CustomerResponse {
    
    private Integer id;
    
    @JsonProperty("first_name")
    private String firstName;
    
    @JsonProperty("last_name")
    private String lastName;
    
    private String email;
    private Integer age;
    private String gender;
    private String state;
    
    @JsonProperty("street_address")
    private String streetAddress;
    
    @JsonProperty("postal_code")
    private String postalCode;
    
    private String city;
    private String country;
    private BigDecimal latitude;
    private BigDecimal longitude;
    
    @JsonProperty("traffic_source")
    private String trafficSource;
    
    @JsonProperty("created_at")
    private OffsetDateTime createdAt;
    
    @JsonProperty("order_count")
    private Long orderCount;
    
    // Constructors
    public CustomerResponse() {}
    
    public CustomerResponse(Integer id, String firstName, String lastName, String email, 
                           Integer age, String gender, String state, String streetAddress,
                           String postalCode, String city, String country, 
                           BigDecimal latitude, BigDecimal longitude, String trafficSource,
                           OffsetDateTime createdAt, Long orderCount) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.age = age;
        this.gender = gender;
        this.state = state;
        this.streetAddress = streetAddress;
        this.postalCode = postalCode;
        this.city = city;
        this.country = country;
        this.latitude = latitude;
        this.longitude = longitude;
        this.trafficSource = trafficSource;
        this.createdAt = createdAt;
        this.orderCount = orderCount;
    }
    
    // Getters and Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }
    
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    
    public String getState() { return state; }
    public void setState(String state) { this.state = state; }
    
    public String getStreetAddress() { return streetAddress; }
    public void setStreetAddress(String streetAddress) { this.streetAddress = streetAddress; }
    
    public String getPostalCode() { return postalCode; }
    public void setPostalCode(String postalCode) { this.postalCode = postalCode; }
    
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    
    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }
    
    public BigDecimal getLatitude() { return latitude; }
    public void setLatitude(BigDecimal latitude) { this.latitude = latitude; }
    
    public BigDecimal getLongitude() { return longitude; }
    public void setLongitude(BigDecimal longitude) { this.longitude = longitude; }
    
    public String getTrafficSource() { return trafficSource; }
    public void setTrafficSource(String trafficSource) { this.trafficSource = trafficSource; }
    
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
    
    public Long getOrderCount() { return orderCount; }
    public void setOrderCount(Long orderCount) { this.orderCount = orderCount; }
}