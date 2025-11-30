package com.hotelbooking.service;

import com.hotelbooking.dto.RoomDTO;
import com.hotelbooking.entity.Room;

import java.time.LocalDate;
import java.util.List;

public interface RoomService {

    RoomDTO addRoom(Room room);

    List<RoomDTO> getAllRooms();
    
    RoomDTO updateRoom(Long id, Room room);

    void deleteRoom(Long id);
    
    List<RoomDTO> getAvailableRooms(LocalDate checkIn, LocalDate checkOut);

    List<RoomDTO> getRooms(int page, int size, String sortBy, String direction);

	List<RoomDTO> getRoomsByHotel(Long hotelId);
	
	List<RoomDTO> getAvailableRoomsByHotel(Long hotelId, LocalDate checkIn, LocalDate checkOut);



}
