package com.example.demo.Entity;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.example.demo.Dto.UserDetailsDTO;
// import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
// import jakarta.persistence.JoinColumns;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Table(name = "USER_DETAILS", schema = "ONBOARDING", uniqueConstraints = {
        @UniqueConstraint(columnNames = "email")
})
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "userDetailsCache")
public class UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;
    private String email;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    // it is used for ignorning user address for both eager and lazy loading
    // @JsonIgnore  
    // For composite key
    // @JoinColumns({
    // @JoinColumn(name = "address_state", referencedColumnName = "state"),
    // @JoinColumn(name = "address_city", referencedColumnName = "city")
    // })
    @JoinColumn(name = "user_address_id", referencedColumnName = "id")
    private UserAddress userAddress;

    
    public long getId() {
        return this.id;
    }
    
    public void setId(long id) {
        this.id = id;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getEmail() {
        return this.email;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public UserAddress getUserAddress() {
        return userAddress;
    }
    
    public void setUserAddress(UserAddress userAddress) {
        this.userAddress = userAddress;
    }

    public UserDetailsDTO toDto() {
        return new UserDetailsDTO(this);
    }
}
