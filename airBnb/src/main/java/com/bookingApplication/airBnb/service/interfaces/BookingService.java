package com.bookingApplication.airBnb.service.interfaces;

import com.bookingApplication.airBnb.entity.BookingEntity;
import com.bookingApplication.airBnb.interfaces.BookingResponse;
import com.bookingApplication.airBnb.dto.BookingRequest;
import com.bookingApplication.airBnb.dto.GuestDTO;
import com.bookingApplication.airBnb.interfaces.BookingStatusResponse;
import com.bookingApplication.airBnb.interfaces.HotelReportResponse;

import java.time.LocalDate;
import java.util.List;

public interface BookingService {
    BookingResponse initialiseBooking(BookingRequest bookingRequest);

    BookingResponse addGuests(Long bookingId, List<GuestDTO> guestDtoList);

    String initiatePayments(Long bookingId);

//    void capturePayment(Event event);

    void cancelBooking(Long bookingId);

    BookingStatusResponse getBookingStatus(Long bookingId);

    List<BookingResponse> getAllBookingsByHotelId(Long hotelId);

    HotelReportResponse getHotelReport(Long hotelId, LocalDate startDate, LocalDate endDate);

    List<BookingResponse> getMyBookings(Long hotelId);

    Boolean hasBookingExpired(BookingEntity booking);
}
