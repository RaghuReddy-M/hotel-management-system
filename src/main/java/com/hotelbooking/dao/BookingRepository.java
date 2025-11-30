package com.hotelbooking.dao;

import com.hotelbooking.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    // Find bookings for a room between dates (to check availability)
    List<Booking> findByRoomIdAndCheckInDateLessThanEqualAndCheckOutDateGreaterThanEqual(
            Long roomId,
            LocalDate checkOutDate,
            LocalDate checkInDate
    );
    
    List<Booking> findByCheckInDateLessThanEqualAndCheckOutDateGreaterThanEqual(
            LocalDate checkOut,
            LocalDate checkIn
    );

}
