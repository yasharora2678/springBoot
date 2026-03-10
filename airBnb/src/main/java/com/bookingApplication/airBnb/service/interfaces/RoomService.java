package com.bookingApplication.airBnb.service.interfaces;

import com.bookingApplication.airBnb.dto.RoomRequest;

import java.util.List;

public interface RoomService {
    RoomRequest createNewRoom(Long hotelId, RoomRequest roomDto);

    List<RoomRequest> getAllRoomsInHotel(Long hotelId);

    RoomRequest getRoomById(Long roomId);

    void deleteRoomById(Long roomId);

    RoomRequest updateRoomById(Long hotelId, Long roomId, RoomRequest roomDto);
}
