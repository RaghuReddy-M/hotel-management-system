package com.hotelbooking.controller;

import com.hotelbooking.dto.BookingDTO;

import com.hotelbooking.dto.BookingRequest;
import com.hotelbooking.service.BookingService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

	@Autowired
    private BookingService bookingService;

    @PostMapping
    public ResponseEntity<BookingDTO> createBooking(@RequestBody BookingRequest request) {

        BookingDTO booking = bookingService.createBooking(
                request.getUserId(),
                request.getRoomId(),
                request.getCheckInDate(),
                request.getCheckOutDate()
        );

        return ResponseEntity.ok(booking);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BookingDTO>> getBookingsByUser(@PathVariable Long userId) {
        List<BookingDTO> bookings = bookingService.getBookingsByUser(userId);
        return ResponseEntity.ok(bookings);
    }
    
    @PutMapping("/{bookingId}/cancel")
    public ResponseEntity<BookingDTO> cancelBooking(@PathVariable Long bookingId) {
        BookingDTO cancelled = bookingService.cancelBooking(bookingId);
        return ResponseEntity.ok(cancelled);
    }

}
