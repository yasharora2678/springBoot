package com.bookingApplication.airBnb.repository;

import com.bookingApplication.airBnb.entity.GuestEntity;
import com.bookingApplication.airBnb.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GuestRepository extends JpaRepository<GuestEntity, Long> {
    List<GuestEntity> findByUser(UserEntity user);
}
