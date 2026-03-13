package com.bookingApplication.airBnb.repository;

import com.bookingApplication.airBnb.entity.HotelEntity;
import com.bookingApplication.airBnb.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HotelRepository extends JpaRepository<HotelEntity, Long> {
    List<HotelEntity> findByOwner(UserEntity userEntity);
}
