package com.bookingApplication.airBnb.service.interfaces;

import com.bookingApplication.airBnb.interfaces.HotelInfoResponse;
import com.bookingApplication.airBnb.dto.HotelRequest;

import java.util.List;

public interface HotelService {
    HotelRequest createNewHotel(HotelRequest hotelDto);
    HotelRequest getHotelById(Long id);
    HotelRequest updateHotelById(Long id, HotelRequest hotelDto);
    void deleteHotelById(Long id);
    void activateHotel(Long hotelId);
    HotelInfoResponse getHotelInfoById(Long hotelId);
    List<HotelRequest> getAllHotels();
}
