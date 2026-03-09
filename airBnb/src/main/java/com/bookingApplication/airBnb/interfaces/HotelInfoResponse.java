package com.bookingApplication.airBnb.interfaces;

import com.bookingApplication.airBnb.dto.HotelRequest;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HotelInfoResponse {
    private HotelRequest hotel;
//    private List<RoomDTO> rooms;
}
