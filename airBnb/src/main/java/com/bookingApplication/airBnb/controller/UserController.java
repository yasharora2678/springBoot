package com.bookingApplication.airBnb.controller;

import com.bookingApplication.airBnb.dto.GuestDTO;
import com.bookingApplication.airBnb.dto.ProfileUpdateRequestDTO;
import com.bookingApplication.airBnb.entity.UserEntity;
import com.bookingApplication.airBnb.interfaces.BookingResponse;
import com.bookingApplication.airBnb.interfaces.SignUpResponse;
import com.bookingApplication.airBnb.service.interfaces.BookingService;
import com.bookingApplication.airBnb.service.interfaces.GuestService;
import com.bookingApplication.airBnb.service.interfaces.UserService;
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
@RequestMapping("/user")
@SecurityRequirement(name = "BearerAuth")
@Tag(name = "User Profile", description = "Manage user profiles and bookings")
public class UserController {

    private final UserService userService;
    private final BookingService bookingService;
    private final GuestService guestService;

    @PatchMapping("/profile")
    @Operation(summary = "Update my profile", description = "Allows a user to update their profile details.", tags = {"User Profile"})
    public ResponseEntity<Void> updateProfile(@RequestBody ProfileUpdateRequestDTO profileUpdateRequestDto) {
        userService.updateProfile(profileUpdateRequestDto);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get User By Id", description = "Allows a user to fetch their profile details.", tags = {"User Profile"})
    public ResponseEntity<UserEntity> getUserByUsername(@PathVariable Long id) {
        UserEntity response = userService.getUserById(id);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{hotelId}/myBookings")
    @Operation(summary = "Get my bookings", description = "Fetches a list of all past bookings for the user.", tags = {"User Profile"})
    public ResponseEntity<List<BookingResponse>> getMyBookings(@PathVariable("hotelId") Long hotelId) {
        return ResponseEntity.ok(bookingService.getMyBookings(hotelId));
    }

    @GetMapping("/profile")
    @Operation(summary = "Get my profile", description = "Retrieves the current user's profile details.", tags = {"User Profile"})
    public ResponseEntity<SignUpResponse> getMyProfile() {
        return ResponseEntity.ok(userService.getMyProfile());
    }

    @GetMapping("/guests")
    @Operation(summary = "Get my guests", description = "Retrieves a list of guests associated with the user's bookings.", tags = {"Booking Guests"})
    public ResponseEntity<List<GuestDTO>> getAllGuests() {
        return ResponseEntity.ok(guestService.getAllGuests());
    }

    @PostMapping("/guests")
    @Operation(summary = "Add a guest", description = "Adds a new guest to the user's guest list.", tags = {"Booking Guests"})
    public ResponseEntity<GuestDTO> addNewGuest(@RequestBody GuestDTO guestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(guestService.addNewGuest(guestDto));
    }

    @PutMapping("/guests/{guestId}")
    @Operation(summary = "Update a guest", description = "Updates details of a guest in the user's guest list.", tags = {"Booking Guests"})
    public ResponseEntity<Void> updateGuest(@PathVariable Long guestId, @RequestBody GuestDTO guestDto) {
        guestService.updateGuest(guestId, guestDto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/guests/{guestId}")
    @Operation(summary = "Remove a guest", description = "Removes a guest from the user's guest list.", tags = {"Booking Guests"})
    public ResponseEntity<Void> deleteGuest(@PathVariable Long guestId) {
        guestService.deleteGuest(guestId);
        return ResponseEntity.noContent().build();
    }
}
