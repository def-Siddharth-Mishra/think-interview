package com.think41.customerapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;

public class ErrorResponse {
    
    private String error;
    private String message;
    private int status;
    private String path;
    
    @JsonProperty("timestamp")
    private OffsetDateTime timestamp;
    
    // Constructors
    public ErrorResponse() {
        this.timestamp = OffsetDateTime.now();
    }
    
    public ErrorResponse(String error, String message, int status, String path) {
        this();
        this.error = error;
        this.message = message;
        this.status = status;
        this.path = path;
    }
    
    // Getters and Setters
    public String getError() { return error; }
    public void setError(String error) { this.error = error; }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    
    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }
    
    public String getPath() { return path; }
    public void setPath(String path) { this.path = path; }
    
    public OffsetDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(OffsetDateTime timestamp) { this.timestamp = timestamp; }
}