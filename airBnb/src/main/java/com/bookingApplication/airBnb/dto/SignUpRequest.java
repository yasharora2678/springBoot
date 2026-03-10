package com.bookingApplication.airBnb.dto;

import com.bookingApplication.airBnb.enums.Gender;
import com.bookingApplication.airBnb.enums.Role;
import lombok.Data;


@Data
public class SignUpRequest {
    private String name;
    private String email;
    private String password;
    private Gender gender;
    private String dateOfBirth;
    private Role role;
}
