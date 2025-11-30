package com.hotelbooking.service.impl;

import com.hotelbooking.dao.BookingRepository;
import com.hotelbooking.dao.HotelRepository;
import com.hotelbooking.dao.RoomRepository;
import com.hotelbooking.dto.RoomDTO;
import com.hotelbooking.entity.Booking;
import com.hotelbooking.entity.Hotel;
import com.hotelbooking.entity.Room;
import com.hotelbooking.exception.BadRequestException;
import com.hotelbooking.exception.ResourceNotFoundException;
import com.hotelbooking.service.RoomService;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final BookingRepository bookingRepository;
    private final HotelRepository hotelRepository;

    public RoomServiceImpl(RoomRepository roomRepository,
                           BookingRepository bookingRepository,
                           HotelRepository hotelRepository) {
        this.roomRepository = roomRepository;
        this.bookingRepository = bookingRepository;
        this.hotelRepository = hotelRepository;
    }

    // -----------------------
    // ADD ROOM (Multi-hotel)
    // -----------------------
    @Override
    public RoomDTO addRoom(Room room) {

        if (room.getHotel() == null || room.getHotel().getId() == null) {
            throw new BadRequestException("hotelId is required");
        }

        Hotel hotel = hotelRepository.findById(room.getHotel().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found"));

        room.setHotel(hotel);

        Room saved = roomRepository.save(room);

        return convertToDTO(saved);
    }

    // -----------------------
    // GET ALL ROOMS
    // -----------------------
    @Override
    public List<RoomDTO> getAllRooms() {
        List<Room> rooms = roomRepository.findAll();
        List<RoomDTO> dtos = new ArrayList<>();

        for (Room r : rooms) {
            dtos.add(convertToDTO(r));
        }
        return dtos;
    }

    // -----------------------
    // UPDATE ROOM (Multi-hotel)
    // -----------------------
    @Override
    public RoomDTO updateRoom(Long id, Room updatedRoom) {

        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found"));

        // update hotel if requested
        if (updatedRoom.getHotel() != null && updatedRoom.getHotel().getId() != null) {
            Hotel hotel = hotelRepository.findById(updatedRoom.getHotel().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Hotel not found"));
            room.setHotel(hotel);
        }

        room.setRoomNumber(updatedRoom.getRoomNumber());
        room.setType(updatedRoom.getType());
        room.setPricePerNight(updatedRoom.getPricePerNight());
        room.setAvailable(updatedRoom.isAvailable());

        Room saved = roomRepository.save(room);

        return convertToDTO(saved);
    }

    // -----------------------
    // DELETE ROOM
    // -----------------------
    @Override
    public void deleteRoom(Long id) {
        if (!roomRepository.existsById(id)) {
            throw new ResourceNotFoundException("Room not found");
        }
        roomRepository.deleteById(id);
    }

    // -----------------------
    // GET AVAILABLE ROOMS BY DATE
    // -----------------------
    @Override
    public List<RoomDTO> getAvailableRooms(LocalDate checkIn, LocalDate checkOut) {

        List<Room> allRooms = roomRepository.findAll();

        List<Booking> overlapping = bookingRepository
                .findByCheckInDateLessThanEqualAndCheckOutDateGreaterThanEqual(checkOut, checkIn);

        List<Long> bookedIds = overlapping.stream()
                .map(b -> b.getRoom().getId())
                .toList();

        List<RoomDTO> result = new ArrayList<>();

        for (Room room : allRooms) {
            if (!bookedIds.contains(room.getId()) && room.isAvailable()) {
                result.add(convertToDTO(room));
            }
        }

        return result;
    }

    // -----------------------
    // PAGINATION + SORTING
    // -----------------------
    @Override
    public List<RoomDTO> getRooms(int page, int size, String sortBy, String direction) {

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Room> roomPage = roomRepository.findAll(pageable);

        List<RoomDTO> dtos = new ArrayList<>();

        for (Room room : roomPage.getContent()) {
            dtos.add(convertToDTO(room));
        }

        return dtos;
    }

    // -----------------------
    // GET ROOMS BY HOTEL ID
    // -----------------------
    @Override
    public List<RoomDTO> getRoomsByHotel(Long hotelId) {

        hotelRepository.findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found"));

        List<Room> rooms = roomRepository.findAll();

        List<RoomDTO> result = new ArrayList<>();

        for (Room room : rooms) {
            if (room.getHotel().getId().equals(hotelId)) {
                result.add(convertToDTO(room));
            }
        }

        return result;
    }
    
    @Override
    public List<RoomDTO> getAvailableRoomsByHotel(Long hotelId, LocalDate checkIn, LocalDate checkOut) {

        //  Validate hotel exists
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found"));

        //  Fetch all rooms in this hotel
        List<Room> hotelRooms = roomRepository.findAll().stream()
                .filter(room -> room.getHotel().getId().equals(hotelId))
                .toList();

        //  Find bookings that overlap with given dates
        List<Booking> overlapping = bookingRepository
                .findByCheckInDateLessThanEqualAndCheckOutDateGreaterThanEqual(checkOut, checkIn);

        List<Long> bookedRoomIds = overlapping.stream()
                .map(b -> b.getRoom().getId())
                .toList();

        //  Filter only available rooms in selected hotel
        List<RoomDTO> result = new ArrayList<>();

        for (Room room : hotelRooms) {
            if (!bookedRoomIds.contains(room.getId()) && room.isAvailable()) {
                result.add(convertToDTO(room));
            }
        }

        return result;
    }


    // -----------------------
    // CONVERTER
    // -----------------------
    private RoomDTO convertToDTO(Room room) {
        RoomDTO dto = new RoomDTO();
        dto.setId(room.getId());
        dto.setRoomNumber(room.getRoomNumber());
        dto.setType(room.getType());
        dto.setPricePerNight(room.getPricePerNight());
        dto.setAvailable(room.isAvailable());
        dto.setHotelId(room.getHotel().getId());
        dto.setHotelName(room.getHotel().getName());
        return dto;
    }
}
