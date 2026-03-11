package com.bookingApplication.airBnb.service;

import com.bookingApplication.airBnb.dto.BookingRequest;
import com.bookingApplication.airBnb.dto.GuestDTO;
import com.bookingApplication.airBnb.entity.BookingEntity;
import com.bookingApplication.airBnb.entity.HotelEntity;
import com.bookingApplication.airBnb.entity.InventoryEntity;
import com.bookingApplication.airBnb.entity.RoomEntity;
import com.bookingApplication.airBnb.enums.BookingStatus;
import com.bookingApplication.airBnb.exceptions.ResourceNotFoundException;
import com.bookingApplication.airBnb.interfaces.BookingResponse;
import com.bookingApplication.airBnb.interfaces.HotelReportResponse;
import com.bookingApplication.airBnb.repository.BookingRepository;
import com.bookingApplication.airBnb.repository.HotelRepository;
import com.bookingApplication.airBnb.repository.InventoryRepository;
import com.bookingApplication.airBnb.repository.RoomRepository;
import com.bookingApplication.airBnb.service.interfaces.BookingService;
import com.bookingApplication.airBnb.strategy.PricingService;
import com.bookingApplication.airBnb.utils.AppUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {
    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;
    private final InventoryRepository inventoryRepository;
    private final PricingService pricingService;
    private final BookingRepository bookingRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public BookingResponse initialiseBooking(BookingRequest bookingRequest) {
        log.info("Initialising booking for hotel : {}, room: {}, date {}-{}", bookingRequest.getHotelId(),
                bookingRequest.getRoomId(), bookingRequest.getCheckInDate(), bookingRequest.getCheckOutDate());
        HotelEntity hotel = hotelRepository.findById(bookingRequest.getHotelId()).
                orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id: " + bookingRequest.getHotelId()));
        RoomEntity room = roomRepository.findById(bookingRequest.getRoomId()).
                orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + bookingRequest.getHotelId()));

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
    public BookingResponse addGuests(Long bookingId, List<GuestDTO> guestDtoList) {
        return null;
    }

    @Override
    public String initiatePayments(Long bookingId) {
        return "";
    }

    @Override
    public void cancelBooking(Long bookingId) {

    }

    @Override
    public BookingStatus getBookingStatus(Long bookingId) {
        return null;
    }

    @Override
    public List<BookingResponse> getAllBookingsByHotelId(Long hotelId) {
        return List.of();
    }

    @Override
    public HotelReportResponse getHotelReport(Long hotelId, LocalDate startDate, LocalDate endDate) {
        return null;
    }

    @Override
    public List<BookingResponse> getMyBookings() {
        return List.of();
    }
}
