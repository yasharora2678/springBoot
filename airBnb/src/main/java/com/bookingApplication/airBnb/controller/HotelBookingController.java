package com.bookingApplication.airBnb.controller;

import com.bookingApplication.airBnb.dto.BookingRequest;
import com.bookingApplication.airBnb.dto.GuestDTO;
import com.bookingApplication.airBnb.interfaces.BookingResponse;
import com.bookingApplication.airBnb.interfaces.BookingStatusResponse;
import com.bookingApplication.airBnb.service.interfaces.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bookings")
@SecurityRequirement(name = "BearerAuth")
@Tag(name = "Booking Flow", description = "Operations related to booking and payments")
public class HotelBookingController {
    private final BookingService bookingService;

    @PostMapping("/init")
    @Operation(summary = "Initialize a new booking", tags = {"Booking Flow"})
    public ResponseEntity<BookingResponse> initialiseBooking(@RequestBody BookingRequest bookingRequest) {
        BookingResponse bookingResponse = bookingService.initialiseBooking(bookingRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(bookingResponse);
    }

    @PostMapping("/{bookingId}/guests")
    @Operation(summary = "Add guests to a booking", tags = {"Booking Guests"})
    public ResponseEntity<BookingResponse> guestBooking(@PathVariable("bookingId") Long bookingId, @RequestBody List<GuestDTO> guestDTO) {
        BookingResponse bookingResponse = bookingService.addGuests(bookingId, guestDTO);
        return ResponseEntity.status(HttpStatus.OK).body(bookingResponse);
    }

//    @PostMapping("/{bookingId}/payments")
//      @Operation(summary = "Initiate payments flow for the booking", tags = {"Booking Flow"})
//    public ResponseEntity<BookingPaymentInitResponseDTO> initiatePayment(@PathVariable Long bookingId) {
//        String sessionUrl = bookingService.initiatePayments(bookingId);
//        return ResponseEntity.ok(new BookingPaymentInitResponseDTO(sessionUrl));
//    }

    @PostMapping("/{bookingId}/cancel")
    @Operation(summary = "Cancel the booking", tags = {"Booking Flow"})
    public ResponseEntity<Void> cancelBooking(@PathVariable Long bookingId) {
        bookingService.cancelBooking(bookingId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{bookingId}/status")
    @Operation(summary = "Check the status of the booking", tags = {"Booking Flow"})
    public ResponseEntity<BookingStatusResponse> getBookingStatus(@PathVariable Long bookingId) {
        BookingStatusResponse bookingStatusResponse = bookingService.getBookingStatus(bookingId);
        return ResponseEntity.status(HttpStatus.OK).body(bookingStatusResponse);
    }
}
