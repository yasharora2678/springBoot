package com.bookingApplication.airBnb.interfaces;

import com.bookingApplication.airBnb.enums.Gender;
import lombok.Data;

import java.time.LocalDate;

@Data
public class SignUpResponse {
        private Long id;
        private String email;
        private String name;
        private Gender gender;
        private LocalDate dateOfBirth;
}
