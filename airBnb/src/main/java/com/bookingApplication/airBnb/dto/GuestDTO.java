package com.bookingApplication.airBnb.dto;

import com.bookingApplication.airBnb.enums.Gender;
import lombok.Data;

@Data
public class GuestDTO {
    private String name;
    private Gender gender;
    private Integer age;
}
