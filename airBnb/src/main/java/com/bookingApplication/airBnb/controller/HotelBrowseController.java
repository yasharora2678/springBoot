package com.bookingApplication.airBnb.controller;

import com.bookingApplication.airBnb.dto.HotelPriceDTO;
import com.bookingApplication.airBnb.dto.HotelSearchRequest;
import com.bookingApplication.airBnb.interfaces.HotelInfoResponse;
import com.bookingApplication.airBnb.service.interfaces.HotelService;
import com.bookingApplication.airBnb.service.interfaces.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/hotels")
public class HotelBrowseController {
    private final InventoryService inventoryService;
    private final HotelService hotelService;

    @GetMapping("/search")
    ResponseEntity<Page<HotelPriceDTO>> search(@RequestBody HotelSearchRequest hotelSearchRequest) {
        Page<HotelPriceDTO> response =  inventoryService.searchHotels(hotelSearchRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{hotelId}/info")
    ResponseEntity<HotelInfoResponse> getHotelInfo(@PathVariable("hotelId") Long hotelId) {
        HotelInfoResponse hotel = hotelService.getHotelInfoById(hotelId);
        return ResponseEntity.status(HttpStatus.OK).body(hotel);
    }
}
