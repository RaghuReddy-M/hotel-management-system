package com.hotelbooking.controller;

import com.hotelbooking.dto.RoomDTO;
import com.hotelbooking.entity.Room;
import com.hotelbooking.service.RoomService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

	@Autowired
    private RoomService roomService;


	@PostMapping
	public ResponseEntity<RoomDTO> addRoom(@RequestBody Room room) {

	    if (room.getHotel() == null || room.getHotel().getId() == null) {
	        return ResponseEntity.badRequest().build();
	    }

	    RoomDTO saved = roomService.addRoom(room);
	    return ResponseEntity.ok(saved);
	}

    
	@PutMapping("/{id}")
	public ResponseEntity<RoomDTO> updateRoom(@PathVariable Long id,@RequestBody Room room) {
	    RoomDTO updated = roomService.updateRoom(id, room);
	    return ResponseEntity.ok(updated);
	}


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRoom(@PathVariable Long id) {
        roomService.deleteRoom(id);
        return ResponseEntity.ok("Room deleted successfully");
    }


    @GetMapping
    public ResponseEntity<List<RoomDTO>> getAllRooms() {
        List<RoomDTO> rooms = roomService.getAllRooms();
        return ResponseEntity.ok(rooms);
    }
    
    @GetMapping("/available")
    public ResponseEntity<List<RoomDTO>> getAvailableRooms(
            @RequestParam LocalDate checkIn,
            @RequestParam LocalDate checkOut) {

        List<RoomDTO> rooms = roomService.getAvailableRooms(checkIn, checkOut);
        return ResponseEntity.ok(rooms);
    }
    
    @GetMapping("/page")
    public ResponseEntity<List<RoomDTO>> getRoomsPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        List<RoomDTO> rooms = roomService.getRooms(page, size, sortBy, direction);
        return ResponseEntity.ok(rooms);
    }
    
    @GetMapping("/hotel/{hotelId}")
    public ResponseEntity<List<RoomDTO>> getRoomsByHotel(@PathVariable Long hotelId) {
        List<RoomDTO> rooms = roomService.getRoomsByHotel(hotelId);
        return ResponseEntity.ok(rooms);
    }



}
