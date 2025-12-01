package com.example.demo.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.Entity.UserDetails;

public interface UserDetailsRepository extends JpaRepository<UserDetails, Long>{
    @Query(value = "SELECT * FROM user_details WHERE name = :userName", nativeQuery = true)
    List<UserDetails> getUserDetailsByNameNativeQuery(@Param("userName") String name);
}
