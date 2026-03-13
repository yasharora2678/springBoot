package com.bookingApplication.airBnb.controller;

import com.bookingApplication.airBnb.dto.RoomRequest;
import com.bookingApplication.airBnb.service.interfaces.RoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/hotels/{hotelId}/rooms")
@RequiredArgsConstructor
@SecurityRequirement(name = "BearerAuth")
@Tag(name = "Rooms Management", description = "Manage hotel's rooms details")
public class RoomController {
    private final RoomService roomService;

    @PostMapping
    @Operation(summary = "Create a new room",
            description = "Adds a new room to a specific hotel")
    public ResponseEntity<RoomRequest> createNewRoom(@PathVariable Long hotelId,
                                                     @RequestBody RoomRequest roomDto) {
        RoomRequest room = roomService.createNewRoom(hotelId, roomDto);
        return new ResponseEntity<>(room, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Retrieve all rooms in a hotel",
            description = "Fetches all rooms belonging to the specified hotel")
    public ResponseEntity<List<RoomRequest>> getAllRoomsInHotel(@PathVariable Long hotelId) {
        return ResponseEntity.ok(roomService.getAllRoomsInHotel(hotelId));
    }

    @GetMapping("/{roomId}")
    @Operation(summary = "Get details of a specific room",
            description = "Fetches details of a specific room in a hotel by ID")
    public ResponseEntity<RoomRequest> getRoomById(@PathVariable Long hotelId, @PathVariable Long roomId) {
        return ResponseEntity.ok(roomService.getRoomById(roomId));
    }

    @DeleteMapping("/{roomId}")
    @Operation(summary = "Delete a room",
            description = "Deletes a room from the hotel by ID")
    public ResponseEntity<RoomRequest> deleteRoomById(@PathVariable Long hotelId, @PathVariable Long roomId) {
        roomService.deleteRoomById(hotelId,roomId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{roomId}")
    @Operation(summary = "Update a room",
            description = "Updates the details of an existing room", tags = {"Admin Inventory"})
    public ResponseEntity<RoomRequest> updateRoomById(@PathVariable Long hotelId, @PathVariable Long roomId,
                                                  @RequestBody RoomRequest roomDto) {
        return ResponseEntity.ok(roomService.updateRoomById(hotelId, roomId, roomDto));
    }
}
