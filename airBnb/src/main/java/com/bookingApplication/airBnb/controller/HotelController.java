package com.bookingApplication.airBnb.controller;

import com.bookingApplication.airBnb.dto.HotelRequest;
import com.bookingApplication.airBnb.service.interfaces.HotelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/admin/hotels")
@RequiredArgsConstructor
@Slf4j
public class HotelController {
    private final HotelService hotelService;

    @PostMapping
    public ResponseEntity<HotelRequest> createNewHotel(@RequestBody HotelRequest hotelDto) {
        log.info("Attempting to create a new hotel with name: {}", hotelDto.getName());
        HotelRequest hotel = hotelService.createNewHotel(hotelDto);
        return new ResponseEntity<>(hotel, HttpStatus.CREATED);
    }

    @GetMapping("/{hotelId}")
    public ResponseEntity<HotelRequest> getHotelById(@PathVariable Long hotelId) {
        HotelRequest hotelDto = hotelService.getHotelById(hotelId);
        return ResponseEntity.ok(hotelDto);
    }

    @PutMapping("/{hotelId}")
    public ResponseEntity<HotelRequest> updateHotelById(@PathVariable Long hotelId, @RequestBody HotelRequest hotelDto) {
        HotelRequest hotel = hotelService.updateHotelById(hotelId, hotelDto);
        return ResponseEntity.ok(hotel);
    }

    @DeleteMapping("/{hotelId}")
    public ResponseEntity<Void> deleteHotelById(@PathVariable Long hotelId) {
        hotelService.deleteHotelById(hotelId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{hotelId}/activate")
    public ResponseEntity<Void> activateHotel(@PathVariable Long hotelId) {
        hotelService.activateHotel(hotelId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<HotelRequest>> getAllHotels() {
        return ResponseEntity.ok(hotelService.getAllHotels());
    }
}
