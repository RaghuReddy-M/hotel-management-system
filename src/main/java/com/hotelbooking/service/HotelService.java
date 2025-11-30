package com.hotelbooking.service;

import com.hotelbooking.dto.HotelDTO;
import com.hotelbooking.entity.Hotel;

import java.util.List;

public interface HotelService {

    HotelDTO addHotel(Hotel hotel);

    List<HotelDTO> getAllHotels();

    HotelDTO getHotelById(Long id);

    HotelDTO updateHotel(Long id, Hotel hotel);

    void deleteHotel(Long id);
    
    List<HotelDTO> searchHotelsByCity(String city);

}
