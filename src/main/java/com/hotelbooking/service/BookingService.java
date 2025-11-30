package com.hotelbooking.service;

import com.hotelbooking.dto.BookingDTO;
import com.hotelbooking.entity.Booking;

import java.time.LocalDate;
import java.util.List;

public interface BookingService {

    BookingDTO createBooking(Long userId, Long roomId, LocalDate checkIn, LocalDate checkOut);

    List<BookingDTO> getBookingsByUser(Long userId);
    
    BookingDTO cancelBooking(Long bookingId);

}
