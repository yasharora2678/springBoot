package com.bookingApplication.airBnb.service;

import com.bookingApplication.airBnb.dto.GuestDTO;
import com.bookingApplication.airBnb.entity.GuestEntity;
import com.bookingApplication.airBnb.entity.UserEntity;
import com.bookingApplication.airBnb.exceptions.ResourceNotFoundException;
import com.bookingApplication.airBnb.repository.GuestRepository;
import com.bookingApplication.airBnb.service.interfaces.GuestService;
import com.bookingApplication.airBnb.utils.AppUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class GuestServiceImpl implements GuestService {
    private GuestRepository guestRepository;
    private ModelMapper modelMapper;

    @Override
    public List<GuestDTO> getAllGuests() {
        UserEntity user = AppUtils.getCurrentUser();

        log.info("Fetching all guests of user with id: {}", user.getId());
        List<GuestEntity> guests = guestRepository.findByUser(user);
        return guests.stream()
                .map(guest -> modelMapper.map(guest, GuestDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public void updateGuest(Long guestId, GuestDTO guestDto) {
        log.info("Updating guest with id: {}", guestId);
        GuestEntity guest = guestRepository.findById(guestId).orElseThrow(() -> new ResourceNotFoundException("Guest not found with id: " + guestId));

        UserEntity user = AppUtils.getCurrentUser();
        if (!user.getId().equals(guest.getUser().getId())) {
            throw new AccessDeniedException("You are not the owner of this guest");
        }

        guest.setName(guestDto.getName());
        guest.setAge(guestDto.getAge());
        guest.setGender(guestDto.getGender());
        guestRepository.save(guest);

        log.info("Guest with ID: {} updated successfully", guestId);
    }

    @Override
    public void deleteGuest(Long guestId) {
        log.info("Deleting guest with ID: {}", guestId);
        GuestEntity guest = guestRepository.findById(guestId)
                .orElseThrow(() -> new ResourceNotFoundException("Guest not found with id: " + guestId));

        UserEntity user = AppUtils.getCurrentUser();
        if (!user.getId().equals(guest.getUser().getId())) {
            throw new AccessDeniedException("You are not the owner of this guest");
        }

        guestRepository.deleteById(guestId);
        log.info("Guest with ID: {} deleted successfully", guestId);
    }

    @Override
    public GuestDTO addNewGuest(GuestDTO guestDto) {
        log.info("Adding new guest: {}", guestDto);
        UserEntity user = AppUtils.getCurrentUser();
        GuestEntity savedGuest = GuestEntity
                .builder()
                .name(guestDto.getName())
                .age(guestDto.getAge())
                .gender(guestDto.getGender())
                .user(user)
                .build();
        guestRepository.save(savedGuest);
        log.info("Guest added with ID: {}", savedGuest.getId());
        return modelMapper.map(savedGuest, GuestDTO.class);
    }
}
