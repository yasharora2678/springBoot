package com.bookingApplication.airBnb.service.interfaces;

import com.bookingApplication.airBnb.dto.HotelPriceDTO;
import com.bookingApplication.airBnb.dto.HotelSearchRequest;
import com.bookingApplication.airBnb.dto.InventoryDTO;
import com.bookingApplication.airBnb.dto.UpdateInventoryRequestDTO;
import com.bookingApplication.airBnb.entity.RoomEntity;
import org.springframework.data.domain.Page;

import java.util.List;

public interface InventoryService {
    void initializeRoomForAYear(RoomEntity room);

    void deleteAllInventories(RoomEntity room);

    Page<HotelPriceDTO> searchHotels(HotelSearchRequest hotelSearchRequest);

    List<InventoryDTO> getAllInventoryByRoom(Long roomId);

    void updateInventory(Long roomId, UpdateInventoryRequestDTO updateInventoryRequestDto);
}
