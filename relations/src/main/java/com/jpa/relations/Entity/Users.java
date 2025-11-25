package com.jpa.relations.Entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
// import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Table(name = "users")
@Entity
@JsonIdentityInfo(
    generator = ObjectIdGenerators.PropertyGenerator.class,
    property = "id"
)
@Data
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String phone;

    @OneToMany(mappedBy = "users" , cascade = CascadeType.ALL)
    // @JoinColumn(name = "user_id", referencedColumnName = "id")
    private List<Order> orderDetails = new ArrayList<>();

    public void setOrderDetails(List<Order> orders) {
        this.orderDetails = orders;
        for(Order order: orderDetails) {
            order.setUsers(this);
        }
    }
}
