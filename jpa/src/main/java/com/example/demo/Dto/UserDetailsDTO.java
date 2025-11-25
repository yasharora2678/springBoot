package com.example.demo.Dto;

import com.example.demo.Entity.UserDetails;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDetailsDTO {
    private Long id;
    private String name;
    private String email;
    private String city;
    private String state;
    private String country;
    
    public UserDetailsDTO(UserDetails userDetails) {
        this.id = userDetails.getId();
        this.name = userDetails.getName();
        this.email = userDetails.getEmail();
        System.out.println("Going to query userAddress now");
        this.city = userDetails.getUserAddress() != null ? userDetails.getUserAddress().getCity() : null;
        this.state = userDetails.getUserAddress() != null ? userDetails.getUserAddress().getState() : null;
        this.country = userDetails.getUserAddress() != null ? userDetails.getUserAddress().getCountry() : null;
    }
}
