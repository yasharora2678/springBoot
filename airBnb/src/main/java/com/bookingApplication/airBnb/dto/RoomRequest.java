package com.bookingApplication.airBnb.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class RoomRequest {
    private String type;
    private BigDecimal basePrice;
    private String[] photos;
    private String[] amenities;
    private Integer totalCount;
    private Integer capacity;
}
