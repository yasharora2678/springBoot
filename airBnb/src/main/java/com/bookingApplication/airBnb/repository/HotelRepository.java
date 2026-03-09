package com.bookingApplication.airBnb.repository;

import com.bookingApplication.airBnb.entity.HotelEntity;
import com.bookingApplication.airBnb.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HotelRepository extends JpaRepository<HotelEntity, Long> {
    List<HotelEntity> findByOwner(UserEntity userEntity);
}
