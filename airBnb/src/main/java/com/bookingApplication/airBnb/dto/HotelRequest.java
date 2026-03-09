package com.bookingApplication.airBnb.dto;

import com.bookingApplication.airBnb.entity.HotelContactInfo;
import lombok.Data;

@Data
public class HotelRequest {
    private Long id;
    private String name;
    private String city;
    private String[] photos;
    private String[] amenities;
    private HotelContactInfo contactInfo;
    private Boolean active;
}
