package com.bookingApplication.airBnb.interfaces;

import com.bookingApplication.airBnb.dto.HotelRequest;
import com.bookingApplication.airBnb.dto.RoomRequest;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class HotelInfoResponse {
    private HotelRequest hotel;
    private List<RoomRequest> rooms;
}
