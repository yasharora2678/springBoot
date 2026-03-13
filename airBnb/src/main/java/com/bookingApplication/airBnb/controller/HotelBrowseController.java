package com.bookingApplication.airBnb.controller;

import com.bookingApplication.airBnb.dto.HotelPriceDTO;
import com.bookingApplication.airBnb.dto.HotelSearchRequest;
import com.bookingApplication.airBnb.interfaces.HotelInfoResponse;
import com.bookingApplication.airBnb.service.interfaces.HotelService;
import com.bookingApplication.airBnb.service.interfaces.InventoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@SecurityRequirement(name = "BearerAuth")
@Tag(name = "Hotel Browse", description = "Browse and search for hotels")
public class HotelBrowseController {
    private final InventoryService inventoryService;
    private final HotelService hotelService;

    @GetMapping("/search")
    @Operation(summary = "Search for hotels", description = "Filter hotels based on location, price, availability, etc.")
    public ResponseEntity<Page<HotelPriceDTO>> search(@ModelAttribute HotelSearchRequest hotelSearchRequest) {
        Page<HotelPriceDTO> response = inventoryService.searchHotels(hotelSearchRequest);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{hotelId}/info")
    @Operation(summary = "Get detailed hotel information", description = "Retrieve information about a specific hotel")
    public ResponseEntity<HotelInfoResponse> getHotelInfo(@PathVariable("hotelId") Long hotelId) {
        HotelInfoResponse hotel = hotelService.getHotelInfoById(hotelId);
        return ResponseEntity.status(HttpStatus.OK).body(hotel);
    }
}
