package com.think41.customerapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class PagedResponse<T> {
    
    private List<T> content;
    
    @JsonProperty("page_number")
    private int pageNumber;
    
    @JsonProperty("page_size")
    private int pageSize;
    
    @JsonProperty("total_elements")
    private long totalElements;
    
    @JsonProperty("total_pages")
    private int totalPages;
    
    @JsonProperty("is_first")
    private boolean isFirst;
    
    @JsonProperty("is_last")
    private boolean isLast;
    
    // Constructors
    public PagedResponse() {}
    
    public PagedResponse(List<T> content, int pageNumber, int pageSize, 
                        long totalElements, int totalPages, boolean isFirst, boolean isLast) {
        this.content = content;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.isFirst = isFirst;
        this.isLast = isLast;
    }
    
    // Getters and Setters
    public List<T> getContent() { return content; }
    public void setContent(List<T> content) { this.content = content; }
    
    public int getPageNumber() { return pageNumber; }
    public void setPageNumber(int pageNumber) { this.pageNumber = pageNumber; }
    
    public int getPageSize() { return pageSize; }
    public void setPageSize(int pageSize) { this.pageSize = pageSize; }
    
    public long getTotalElements() { return totalElements; }
    public void setTotalElements(long totalElements) { this.totalElements = totalElements; }
    
    public int getTotalPages() { return totalPages; }
    public void setTotalPages(int totalPages) { this.totalPages = totalPages; }
    
    public boolean isFirst() { return isFirst; }
    public void setFirst(boolean first) { isFirst = first; }
    
    public boolean isLast() { return isLast; }
    public void setLast(boolean last) { isLast = last; }
}