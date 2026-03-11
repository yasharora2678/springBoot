package com.bookingApplication.airBnb.service;

import com.bookingApplication.airBnb.dto.RoomRequest;
import com.bookingApplication.airBnb.entity.HotelEntity;
import com.bookingApplication.airBnb.entity.RoomEntity;
import com.bookingApplication.airBnb.entity.UserEntity;
import com.bookingApplication.airBnb.exceptions.ResourceNotFoundException;
import com.bookingApplication.airBnb.exceptions.UnAuthorisedException;
import com.bookingApplication.airBnb.interfaces.HotelInfoResponse;
import com.bookingApplication.airBnb.dto.HotelRequest;
import com.bookingApplication.airBnb.repository.HotelRepository;
import com.bookingApplication.airBnb.service.interfaces.HotelService;
import com.bookingApplication.airBnb.utils.AppUtils;
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
public class HotelServiceImpl implements HotelService {
    private final HotelRepository hotelRepository;
    private final ModelMapper modelMapper;

    @Override
    public HotelRequest createNewHotel(HotelRequest hotelDto) {
        log.info("Creating a new hotel with name: {}", hotelDto.getName());
        UserEntity user = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        HotelEntity hotel = HotelEntity.builder().name(hotelDto.getName()).city(hotelDto.getCity()).photos(hotelDto.getPhotos()).amenities(hotelDto.getAmenities()).active(false).hotelContactInfo(hotelDto.getContactInfo()).owner(user).build();

        hotel = hotelRepository.save(hotel);

        log.info("Created a new hotel with ID: {}", hotel.getId());
        return modelMapper.map(hotel, HotelRequest.class);
    }

    @Override
    public HotelRequest getHotelById(Long id) {
        log.info("Getting hotel with ID: {}", id);

        HotelEntity hotel = hotelRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel with ID: " + id + " not found"));

        UserEntity user = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!user.getId().equals(hotel.getOwner().getId())) {
            throw new UnAuthorisedException(
                    "This user does not own this hotel with id: " + id);
        }

        return modelMapper.map(hotel, HotelRequest.class);
    }

    @Override
    public HotelRequest updateHotelById(Long id, HotelRequest hotelDto) {

        log.info("Updating a hotel with id: {}", id);

        HotelEntity hotel = hotelRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel with ID: " + id + " not found"));

        UserEntity user = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!user.getId().equals(hotel.getOwner().getId())) {
            throw new UnAuthorisedException("This user does not own this hotel with id: " + id);
        }

        hotel.setName(hotelDto.getName());
        hotel.setCity(hotelDto.getCity());
        hotel.setPhotos(hotelDto.getPhotos());
        hotel.setAmenities(hotelDto.getAmenities());
        hotel.setHotelContactInfo(hotelDto.getContactInfo());
        hotel.setActive(false);

        hotel = hotelRepository.save(hotel);

        log.info("Updated hotel with ID: {}", hotel.getId());

        return modelMapper.map(hotel, HotelRequest.class);
    }

    @Override
    public void deleteHotelById(Long id) {
        log.info("Deleting hotel with ID: {}", id);

        HotelEntity hotel = hotelRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel with ID: " + id + " not found"));

        UserEntity user = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!user.getId().equals(hotel.getOwner().getId())) {
            throw new UnAuthorisedException("This user does not own this hotel with id: " + id);
        }

        hotelRepository.deleteById(id);
    }

    @Override
    public void activateHotel(Long hotelId) {
        log.info("Activating hotel with ID: {}", hotelId);

        HotelEntity hotel = hotelRepository
                .findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel with ID: " + hotelId + " not found"));

        UserEntity user = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!user.getId().equals(hotel.getOwner().getId())) {
            throw new UnAuthorisedException("This user does not own this hotel with id: " + hotelId);
        }

        hotel.setActive(true);
        hotelRepository.save(hotel);
    }

    @Override
    public HotelInfoResponse getHotelInfoById(Long hotelId) {
        log.info("Getting hotel information with ID: {}", hotelId);

        HotelEntity hotel = hotelRepository
                .findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel with ID: " + hotelId + " not found"));
        List<RoomRequest> rooms = hotel.getRooms()
                .stream()
                .map((element) -> modelMapper.map(element, RoomRequest.class))
                .toList();

        return new HotelInfoResponse(modelMapper.map(hotel, HotelRequest.class), rooms);
    }

    @Override
    public List<HotelRequest> getAllHotels() {
        UserEntity user = AppUtils.getCurrentUser();
        log.info("Getting all hotels for the admin user with ID: {}", user.getId());

        List<HotelEntity> hotels = hotelRepository.findByOwner(user);
        return hotels
                .stream()
                .map((element) -> modelMapper.map(element, HotelRequest.class))
                .collect(Collectors.toList());
    }
}
