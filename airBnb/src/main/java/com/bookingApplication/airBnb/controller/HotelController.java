package com.bookingApplication.airBnb.controller;

import com.bookingApplication.airBnb.dto.HotelRequest;
import com.bookingApplication.airBnb.interfaces.BookingResponse;
import com.bookingApplication.airBnb.interfaces.HotelReportResponse;
import com.bookingApplication.airBnb.service.interfaces.BookingService;
import com.bookingApplication.airBnb.service.interfaces.HotelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController()
@RequestMapping("/admin/hotels")
@RequiredArgsConstructor
@Slf4j
@SecurityRequirement(name = "BearerAuth")
@Tag(name = "Hotel Management", description = "Manage hotel details")
public class HotelController {
    private final HotelService hotelService;
    private final BookingService bookingService;

    @PostMapping
    @Operation(summary = "Create a new hotel", description = "Adds a new hotel to the system")
    public ResponseEntity<HotelRequest> createNewHotel(@RequestBody HotelRequest hotelDto) {
        log.info("Attempting to create a new hotel with name: {}", hotelDto.getName());
        HotelRequest hotel = hotelService.createNewHotel(hotelDto);
        return new ResponseEntity<>(hotel, HttpStatus.CREATED);
    }

    @GetMapping("/{hotelId}")
    @Operation(summary = "Get hotel by ID", description = "Fetch details of a specific hotel")
    public ResponseEntity<HotelRequest> getHotelById(@PathVariable Long hotelId) {
        HotelRequest hotelDto = hotelService.getHotelById(hotelId);
        return ResponseEntity.ok(hotelDto);
    }

    @PutMapping("/{hotelId}")
    @Operation(summary = "Update hotel details", description = "Modify hotel information")
    public ResponseEntity<HotelRequest> updateHotelById(@PathVariable Long hotelId, @RequestBody HotelRequest hotelDto) {
        HotelRequest hotel = hotelService.updateHotelById(hotelId, hotelDto);
        return ResponseEntity.ok(hotel);
    }

    @DeleteMapping("/{hotelId}")
    @Operation(summary = "Delete a hotel", description = "Removes a hotel from the system")
    public ResponseEntity<String> deleteHotelById(@PathVariable Long hotelId) {
        hotelService.deleteHotelById(hotelId);
        return ResponseEntity.status(HttpStatus.OK).body("Deleted hotel with id: " + hotelId);
    }

    @PatchMapping("/{hotelId}/activate")
    @Operation(summary = "Activate a hotel", description = "Marks a hotel as active")
    public ResponseEntity<String> activateHotel(@PathVariable Long hotelId) {
        hotelService.activateHotel(hotelId);
        return ResponseEntity.status(HttpStatus.OK).body("Activated hotel with id: " + hotelId);
    }

    @GetMapping
    @Operation(summary = "Get all hotels owned by admin", description = "Retrieve a list of all hotels owned by the admin")
    public ResponseEntity<List<HotelRequest>> getAllHotels() {
        return ResponseEntity.ok(hotelService.getAllHotels());
    }

    @GetMapping("/{hotelId}/bookings")
    @Operation(summary = "Get all bookings of a hotel", description = "Fetches all bookings related to a specific hotel", tags = {"Booking Flow"})
    public ResponseEntity<List<BookingResponse>> getAllBookingsByHotelId(@PathVariable Long hotelId) {
        return ResponseEntity.ok(bookingService.getAllBookingsByHotelId(hotelId));
    }

    @GetMapping("/{hotelId}/reports")
    @Operation(summary = "Generate a hotel booking report",description = "Generates a report for hotel bookings within a date range", tags = {"Booking Flow"})
    public ResponseEntity<HotelReportResponse> getHotelReport(@PathVariable Long hotelId,
                                                              @RequestParam(required = false) LocalDate startDate,
                                                              @RequestParam(required = false) LocalDate endDate) {

        if (startDate == null) startDate = LocalDate.now().minusMonths(1);
        if (endDate == null) endDate = LocalDate.now();

        return ResponseEntity.ok(bookingService.getHotelReport(hotelId, startDate, endDate));
    }
}
