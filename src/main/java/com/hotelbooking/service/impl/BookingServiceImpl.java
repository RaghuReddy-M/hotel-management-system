package com.hotelbooking.service.impl;

import com.hotelbooking.dao.BookingRepository;
import com.hotelbooking.dao.RoomRepository;
import com.hotelbooking.dao.UserRepository;
import com.hotelbooking.dto.BookingDTO;
import com.hotelbooking.entity.Booking;
import com.hotelbooking.entity.Room;
import com.hotelbooking.entity.User;
import com.hotelbooking.exception.BadRequestException;
import com.hotelbooking.exception.ResourceNotFoundException;
import com.hotelbooking.service.BookingService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {

	@Autowired
    private  BookingRepository bookingRepository;
	@Autowired
    private  UserRepository userRepository;
	@Autowired
    private  RoomRepository roomRepository;

    @Override
    public BookingDTO createBooking(Long userId, Long roomId, LocalDate checkIn, LocalDate checkOut) {

        // Validate user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new  ResourceNotFoundException("User not found"));

        // Validate room
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new  ResourceNotFoundException("Room not found"));

        // Check availability
        boolean isRoomBooked = !bookingRepository
                .findByRoomIdAndCheckInDateLessThanEqualAndCheckOutDateGreaterThanEqual(
                        roomId, checkOut, checkIn
                ).isEmpty();

        if (isRoomBooked) {
            throw new BadRequestException("Room is not available for selected dates");
        }

        // Calculate number of nights
        long nights = ChronoUnit.DAYS.between(checkIn, checkOut);

        if (nights <= 0) {
            throw new BadRequestException("Checkout date must be after check-in date");
        }

        double totalAmount = room.getPricePerNight() * nights;

        Booking booking = new Booking();
        booking.setUser(user);
        booking.setRoom(room);
        booking.setCheckInDate(checkIn);
        booking.setCheckOutDate(checkOut);
        booking.setTotalAmount(totalAmount);
        booking.setStatus("BOOKED");

        Booking savedBooking = bookingRepository.save(booking);

        return convertToDTO(savedBooking);
    }

    @Override
    public List<BookingDTO> getBookingsByUser(Long userId) {
        List<Booking> bookings = bookingRepository.findAll();
        List<BookingDTO> dtoList = new ArrayList<>();

        for (Booking booking : bookings) {
            if (booking.getUser().getId().equals(userId)) {
                dtoList.add(convertToDTO(booking));
            }
        }

        return dtoList;
    }

    private BookingDTO convertToDTO(Booking booking) {
        BookingDTO dto = new BookingDTO();
        dto.setId(booking.getId());
        dto.setUserId(booking.getUser().getId());
        dto.setRoomId(booking.getRoom().getId());
        dto.setCheckInDate(booking.getCheckInDate());
        dto.setCheckOutDate(booking.getCheckOutDate());
        dto.setTotalAmount(booking.getTotalAmount());
        dto.setStatus(booking.getStatus());
        return dto;
    }
    
    @Override
    public BookingDTO cancelBooking(Long bookingId) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new  ResourceNotFoundException("Booking not found"));
        
        // Get logged-in user
        User loggedInUser = (User) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        // If user is CUSTOMER — ensure booking belongs to them
        if (loggedInUser.getRole().equals("CUSTOMER")) {
            if (!booking.getUser().getId().equals(loggedInUser.getId())) {
                throw new BadRequestException("You are not allowed to cancel this booking");
            }
        }


        if (booking.getStatus().equals("CANCELLED")) {
            throw new BadRequestException("Booking is already cancelled");
        }

        //  update booking status
        booking.setStatus("CANCELLED");

        // make room available again
        Room room = booking.getRoom();
        room.setAvailable(true);
        roomRepository.save(room);

        Booking savedBooking = bookingRepository.save(booking);

        return convertToDTO(savedBooking);
    }
}