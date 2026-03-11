package com.bookingApplication.airBnb.entity;

import com.bookingApplication.airBnb.enums.Gender;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name= "Guest")
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GuestEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    private String name;

    private Gender gender;

    private Integer age;
}
