package com.bookingApplication.airBnb.repository;

import com.bookingApplication.airBnb.entity.HotelEntity;
import com.bookingApplication.airBnb.entity.InventoryEntity;
import com.bookingApplication.airBnb.entity.RoomEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface InventoryRepository extends JpaRepository<InventoryEntity, Long> {
    void deleteByRoom(RoomEntity room);

    List<InventoryEntity> findByRoomOrderByDate(RoomEntity room);

    List<InventoryEntity> findByHotelAndDateBetween(HotelEntity hotelEntity, LocalDate startDate, LocalDate endDate);
    @Query("""
                SELECT i
                FROM InventoryEntity i
                WHERE i.room.id = :roomId
                AND i.date BETWEEN :startDate AND :endDate
        """)
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    void getInventoryAndLockBeforeUpdate(@Param("roomId") Long roomId,
                                         @Param("startDate") LocalDate startDate,
                                         @Param("endDate") LocalDate endDate);

    @Query("""
                SELECT i
                FROM InventoryEntity i
                WHERE i.room.id = :roomId
                AND i.hotel.id = :hotelId
                AND i.date BETWEEN :startDate AND :endDate
                AND i.closed = false
                AND (i.totalCount - i.reservedCount - i.bookingCount) >= :roomsCount
    """)
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<InventoryEntity> findAndLockAvailableInventory(@Param("roomId") Long roomId,
                                                        @Param("hotelId") Long hotelId,
                                                        @Param("startDate") LocalDate startDate,
                                                        @Param("endDate") LocalDate endDate,
                                                        @Param("roomsCount") Integer roomsCount);

    @Modifying
    @Query("""
                UPDATE InventoryEntity i
                SET i.reservedCount = i.reservedCount + roomsCount
                WHERE i.room.id = :roomId
                AND i.date BETWEEN :startDate AND :endDate
                AND i.closed = false
                AND (i.totalCount - i.reservedCount - i.bookingCount) >= :roomsCount
    """)
    List<InventoryEntity> initBooking(@Param("roomId") Long roomId,
                                      @Param("startDate") LocalDate startDate,
                                      @Param("endDate") LocalDate endDate,
                                      @Param("roomsCount") Integer roomsCount);


    @Modifying
    @Query("""
                UPDATE InventoryEntity i
                SET i.surgeFactor = :surgeFactor,
                    i.closed = :closed
                WHERE i.room.id = :roomId
                  AND i.date BETWEEN :startDate AND :endDate
            """)
    void updateInventory(@Param("roomId") Long roomId,
                         @Param("startDate") LocalDate startDate,
                         @Param("endDate") LocalDate endDate,
                         @Param("closed") boolean closed,
                         @Param("surgeFactor") BigDecimal surgeFactor);
}
