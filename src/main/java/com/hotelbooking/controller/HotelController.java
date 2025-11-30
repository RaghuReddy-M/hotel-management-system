package com.hotelbooking.controller;

import com.hotelbooking.dto.HotelDTO;
import com.hotelbooking.dto.RoomDTO;
import com.hotelbooking.entity.Hotel;
import com.hotelbooking.service.HotelService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;
import com.hotelbooking.service.RoomService;


@RestController
@RequestMapping("/api/hotels")
public class HotelController {

	@Autowired
    private HotelService hotelService;
	@Autowired
	private RoomService roomService;

    @PostMapping
    public ResponseEntity<HotelDTO> addHotel(@RequestBody Hotel hotel) {
        return ResponseEntity.ok(hotelService.addHotel(hotel));
    }

    @GetMapping
    public ResponseEntity<List<HotelDTO>> getAllHotels() {
        return ResponseEntity.ok(hotelService.getAllHotels());
    }

    @GetMapping("/{id}")
    public ResponseEntity<HotelDTO> getHotel(@PathVariable Long id) {
        return ResponseEntity.ok(hotelService.getHotelById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<HotelDTO> updateHotel(
            @PathVariable Long id,
            @RequestBody Hotel hotel) {
        return ResponseEntity.ok(hotelService.updateHotel(id, hotel));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteHotel(@PathVariable Long id) {
        hotelService.deleteHotel(id);
        return ResponseEntity.ok("Hotel deleted successfully");
    }
    
    @GetMapping("/{hotelId}/available")
    public ResponseEntity<List<RoomDTO>> getAvailableRoomsByHotel(
            @PathVariable Long hotelId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkIn,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOut
    ) {
        List<RoomDTO> rooms = roomService.getAvailableRoomsByHotel(hotelId, checkIn, checkOut);
        return ResponseEntity.ok(rooms);
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<HotelDTO>> searchHotels(@RequestParam String city) {
        List<HotelDTO> hotels = hotelService.searchHotelsByCity(city);
        return ResponseEntity.ok(hotels);
    }


}
