package com.bookingApplication.airBnb.dto;

import com.bookingApplication.airBnb.enums.Gender;
import lombok.Data;

import java.time.LocalDate;

@Data
public class SignUpRequest {
    private String name;
    private String email;
    private String password;
    private Gender gender;
    private String dateOfBirth;
}
