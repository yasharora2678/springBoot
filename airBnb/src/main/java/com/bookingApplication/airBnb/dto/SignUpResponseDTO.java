package com.bookingApplication.airBnb.dto;

import com.bookingApplication.airBnb.enums.Gender;
import lombok.Data;

import java.time.LocalDate;

@Data
public class SignUpResponseDTO {
        private Long id;
        private String email;
        private String name;
        private Gender gender;
        private LocalDate dateOfBirth;
}
