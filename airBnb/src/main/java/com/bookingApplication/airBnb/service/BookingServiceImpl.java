package com.bookingApplication.airBnb.service;

import com.bookingApplication.airBnb.dto.BookingRequest;
import com.bookingApplication.airBnb.dto.GuestDTO;
import com.bookingApplication.airBnb.entity.*;
import com.bookingApplication.airBnb.enums.BookingStatus;
import com.bookingApplication.airBnb.exceptions.ResourceNotFoundException;
import com.bookingApplication.airBnb.exceptions.UnAuthorisedException;
import com.bookingApplication.airBnb.interfaces.BookingResponse;
import com.bookingApplication.airBnb.interfaces.BookingStatusResponse;
import com.bookingApplication.airBnb.interfaces.HotelReportResponse;
import com.bookingApplication.airBnb.repository.*;
import com.bookingApplication.airBnb.service.interfaces.BookingService;
import com.bookingApplication.airBnb.strategy.PricingService;
import com.bookingApplication.airBnb.utils.AppUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {
    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;
    private final InventoryRepository inventoryRepository;
    private final PricingService pricingService;
    private final BookingRepository bookingRepository;
    private final GuestRepository guestRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public BookingResponse initialiseBooking(BookingRequest bookingRequest) {
        log.info("Initialising booking for hotel : {}, room: {}, date {}-{}", bookingRequest.getHotelId(),
                bookingRequest.getRoomId(), bookingRequest.getCheckInDate(), bookingRequest.getCheckOutDate());
        HotelEntity hotel = hotelRepository.findById(bookingRequest.getHotelId()).
                orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id: " + bookingRequest.getHotelId()));
        RoomEntity room = roomRepository.findById(bookingRequest.getRoomId()).
                orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + bookingRequest.getRoomId()));

        List<InventoryEntity> inventoryList = inventoryRepository.findAndLockAvailableInventory(bookingRequest.getRoomId(),
                bookingRequest.getHotelId(), bookingRequest.getCheckInDate(), bookingRequest.getCheckOutDate(), bookingRequest.getRoomsCount());

        long daysCount = ChronoUnit.DAYS.between(bookingRequest.getCheckInDate(), bookingRequest.getCheckOutDate())+1;

        if (inventoryList.size() != daysCount) {
            throw new IllegalStateException("Room is not available anymore");
        }

        //reserve inventory now according to user request
        inventoryRepository.initBooking(bookingRequest.getRoomId(), bookingRequest.getCheckInDate(), bookingRequest.getCheckOutDate(), bookingRequest.getRoomsCount());

        BigDecimal priceForOneRoom = pricingService.calculateTotalPrice(inventoryList);
        BigDecimal totalPrice = priceForOneRoom.multiply(BigDecimal.valueOf(bookingRequest.getRoomsCount()));

        // Create the Booking

        BookingEntity booking = BookingEntity.builder()
                .bookingStatus(BookingStatus.RESERVED)
                .hotel(hotel)
                .room(room)
                .checkInDate(bookingRequest.getCheckInDate())
                .checkOutDate(bookingRequest.getCheckOutDate())
                .user(AppUtils.getCurrentUser())
                .roomsCount(bookingRequest.getRoomsCount())
                .amount(totalPrice)
                .build();

        booking = bookingRepository.save(booking);
        return modelMapper.map(booking, BookingResponse.class);
    }

    @Override
    @Transactional
    public BookingResponse addGuests(Long bookingId, List<GuestDTO> guestDtoList) {
        log.info("Adding guests for booking with id: {}", bookingId);
        BookingEntity booking = bookingRepository.findById(bookingId).
                orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + bookingId));

        UserEntity user = AppUtils.getCurrentUser();
        if(!user.getId().equals(booking.getUser().getId())) {
            throw new UnAuthorisedException("Booking does not belong to this user with id: "+user.getId());
        }

        if(!booking.getBookingStatus().equals(BookingStatus.RESERVED)) {
            throw new IllegalStateException("Booking is not under reserved state, cannot add guests");
        }

        if(hasBookingExpired(booking)) {
            throw new IllegalStateException("Booking has already expired");
        }
        Set<GuestEntity> guestEntitySet = guestDtoList.stream()
                .map(element -> {
                    GuestEntity guest = modelMapper.map(element, GuestEntity.class);
                    guest.setUser(user);
                    return guestRepository.save(guest);
                })
                .collect(Collectors.toSet());

        booking.setBookingStatus(BookingStatus.GUESTS_ADDED);
        booking.setGuests(guestEntitySet);
        booking = bookingRepository.save(booking);

        return modelMapper.map(booking, BookingResponse.class);
    }

    @Override
    public String initiatePayments(Long bookingId) {
        return "";
    }

    @Override
    public void cancelBooking(Long bookingId) {
        log.info("Cancelling booking for booking with id: {}", bookingId);

        BookingEntity booking = bookingRepository.findById(bookingId).
                orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + bookingId));

        UserEntity user = AppUtils.getCurrentUser();
        if(!user.getId().equals(booking.getUser().getId())) {
            throw new UnAuthorisedException("Booking does not belong to this user with id: "+user.getId());
        }

        if(booking.getBookingStatus().equals(BookingStatus.CONFIRMED)) {
            throw new IllegalStateException("Only confirmed bookings can be cancelled");
        }

        booking.setBookingStatus(BookingStatus.CANCELLED);
        booking = bookingRepository.save(booking);

        inventoryRepository.findAndLockReservedInventory(booking.getRoom().getId(),booking.getHotel().getId(), booking.getCheckInDate(), booking.getCheckOutDate(), booking.getRoomsCount());
        inventoryRepository.cancelBooking(booking.getRoom().getId(),booking.getHotel().getId(), booking.getCheckInDate(), booking.getCheckOutDate(), booking.getRoomsCount());
    }

    @Override
    public BookingStatusResponse getBookingStatus(Long bookingId) {
        log.info("Getting booking status for booking with id: {}", bookingId);

        BookingEntity booking = bookingRepository.findById(bookingId).
                orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + bookingId));

        UserEntity user = AppUtils.getCurrentUser();
        if(!user.getId().equals(booking.getUser().getId())) {
            throw new UnAuthorisedException("Booking does not belong to this user with id: "+user.getId());
        }

        BookingStatus status =  booking.getBookingStatus();
        return modelMapper.map(status, BookingStatusResponse.class);
    }

    @Override
    public List<BookingResponse> getAllBookingsByHotelId(Long hotelId) {
        log.info("Getting all bookings for hotel with id: {}", hotelId);

        HotelEntity hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id: " + hotelId));

        UserEntity user = AppUtils.getCurrentUser();
        if(!user.getId().equals(hotel.getOwner().getId())) {
            throw new AccessDeniedException("You are not the owner of hotel with id: "+hotelId);
        }

        List<BookingEntity> bookings = bookingRepository.findByHotel(hotel);
        return bookings.stream()
                .map((booking) -> modelMapper.map(booking, BookingResponse.class)).toList();
    }

    @Override
    public HotelReportResponse getHotelReport(Long hotelId, LocalDate startDate, LocalDate endDate) {
        HotelEntity hotel = hotelRepository.findById(hotelId).orElseThrow(() -> new ResourceNotFoundException("Hotel not " +
                "found with ID: "+hotelId));
        UserEntity user = AppUtils.getCurrentUser();

        log.info("Generating report for hotel with ID: {}", hotelId);

        return null;
    }

    @Override
    public List<BookingResponse> getMyBookings(Long hotelId) {
        UserEntity user = AppUtils.getCurrentUser();
        log.info("Getting all bookings for user with id: {} with hotel id: {}", user.getId(), hotelId);

        HotelEntity hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id: " + hotelId));

        List<BookingEntity> bookings = bookingRepository.findByHotelAndUser(hotel, user);
        return bookings.stream()
                .map((booking) -> modelMapper.map(booking, BookingResponse.class)).toList();
    }

    @Override
    public Boolean hasBookingExpired(BookingEntity booking) {
        return booking.getCreatedAt().plusMinutes(15).isBefore(LocalDateTime.now());
    }
}
