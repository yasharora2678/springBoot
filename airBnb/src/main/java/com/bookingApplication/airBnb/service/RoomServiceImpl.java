package com.bookingApplication.airBnb.service;

import com.bookingApplication.airBnb.dto.RoomRequest;
import com.bookingApplication.airBnb.entity.HotelEntity;
import com.bookingApplication.airBnb.entity.RoomEntity;
import com.bookingApplication.airBnb.entity.UserEntity;
import com.bookingApplication.airBnb.exceptions.ResourceNotFoundException;
import com.bookingApplication.airBnb.exceptions.UnAuthorisedException;
import com.bookingApplication.airBnb.repository.HotelRepository;
import com.bookingApplication.airBnb.repository.RoomRepository;
import com.bookingApplication.airBnb.service.interfaces.InventoryService;
import com.bookingApplication.airBnb.service.interfaces.RoomService;
import com.bookingApplication.airBnb.utils.AppUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoomServiceImpl implements RoomService {
    private final RoomRepository roomRepository;
    private final HotelRepository hotelRepository;
    private final InventoryService inventoryService;
    private final ModelMapper modelMapper;

    @Override
    public RoomRequest createNewRoom(Long hotelId, RoomRequest roomRequest) {
        log.info("Creating a new room in hotel with ID: {}", hotelId);
        HotelEntity hotel = hotelRepository
                .findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with ID: " + hotelId));

        UserEntity user = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!user.getId().equals(hotel.getOwner().getId())) {
            throw new UnAuthorisedException("This user does not own this hotel with id: " + hotelId);
        }

        RoomEntity room = RoomEntity.builder()
                .type(roomRequest.getType())
                .basePrice(roomRequest.getBasePrice())
                .hotel(hotel)
                .photos(roomRequest.getPhotos())
                .amenities(roomRequest.getAmenities())
                .totalCount(roomRequest.getTotalCount())
                .capacity(roomRequest.getCapacity())
                .build();
        room = roomRepository.save(room);

        if (hotel.getActive()) {
            inventoryService.initializeRoomForAYear(room);
        }

        return modelMapper.map(room, RoomRequest.class);
    }

    @Override
    public List<RoomRequest> getAllRoomsInHotel(Long hotelId) {
        log.info("Getting all rooms in hotel with ID: {}", hotelId);
        HotelEntity hotel = hotelRepository
                .findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with ID: " + hotelId));

        UserEntity user = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!user.getId().equals(hotel.getOwner().getId())) {
            throw new UnAuthorisedException("This user does not own this hotel with id: " + hotelId);
        }

        return hotel.getRooms()
                .stream()
                .map((element) -> modelMapper.map(element, RoomRequest.class))
                .collect(Collectors.toList());
    }

    @Override
    public RoomRequest getRoomById(Long roomId) {
        log.info("Getting the room with ID: {}", roomId);
        RoomEntity room = roomRepository
                .findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with ID: " + roomId));
        return modelMapper.map(room, RoomRequest.class);

    }

    @Override
    @Transactional
    public void deleteRoomById(Long roomId) {
        log.info("Deleting the room with ID: {}", roomId);
        RoomEntity room = roomRepository
                .findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with ID: " + roomId));

        UserEntity user = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!user.getId().equals(room.getHotel().getOwner().getId())) {
            throw new UnAuthorisedException("This user does not own this room with id: " + roomId);
        }
        inventoryService.deleteAllInventories(room);
        roomRepository.deleteById(roomId);
    }

    @Override
    public RoomRequest updateRoomById(Long hotelId, Long roomId, RoomRequest roomRequest) {
        log.info("Updating the room with ID: {}", roomId);
        HotelEntity hotel = hotelRepository
                .findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with ID: "+hotelId));

        UserEntity user = AppUtils.getCurrentUser();
        if(!user.getId().equals(hotel.getOwner().getId())) {
            throw new UnAuthorisedException("This user does not own this hotel with id: "+hotelId);
        }

        RoomEntity room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with ID: "+roomId));

        room.setType(roomRequest.getType());
//        room.setBasePrice(roomRequest.getBasePrice());
        room.setHotel(hotel);
        room.setPhotos(roomRequest.getPhotos());
        room.setAmenities(roomRequest.getAmenities());
//        room.setTotalCount(roomRequest.getTotalCount());
        room.setCapacity(roomRequest.getCapacity());

//        TODO: if price or inventory is updated, then update the inventory for this room
        room = roomRepository.save(room);

        return modelMapper.map(room, RoomRequest.class);
    }
}
