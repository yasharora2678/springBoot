package com.example.demo.Entity;

import jakarta.persistence.Embeddable;

@Embeddable
public class UserAddressCK {
    private String street;
    private String pincode;

    public String getStreet() {
        return this.street;
    }

    public String getPincode() {
        return this.pincode;
    }

    public void setState(String street) {
        this.street = street;
    }

    public void setPinCode(String pincode) {
        this.pincode = pincode;
    }
}
