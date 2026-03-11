package com.bookingApplication.airBnb.repository;

import com.bookingApplication.airBnb.entity.BookingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<BookingEntity, Long> {
}
