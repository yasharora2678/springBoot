package com.bookingApplication.airBnb.service.interfaces;

import com.bookingApplication.airBnb.dto.GuestDTO;

import java.util.List;

public interface GuestService {
    List<GuestDTO> getAllGuests();

    void updateGuest(Long guestId, GuestDTO guestDto);

    void deleteGuest(Long guestId);

    GuestDTO addNewGuest(GuestDTO guestDto);
}
