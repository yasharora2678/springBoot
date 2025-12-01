package com.example.demo.DTO;

import lombok.Data;

@Data
public class UserDTO {
    private String name;
    private String phone;
    private String street;
    private String city;

    public UserDTO(String name, String phone, String street, String city) {
        this.name = name;
        this.phone = phone;
        this.city = city;
        this.street = street;
    }
}
