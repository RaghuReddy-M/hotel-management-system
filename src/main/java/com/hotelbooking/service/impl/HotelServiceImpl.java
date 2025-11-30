package com.hotelbooking.service.impl;

import com.hotelbooking.dao.HotelRepository;
import com.hotelbooking.dto.HotelDTO;
import com.hotelbooking.entity.Hotel;
import com.hotelbooking.exception.ResourceNotFoundException;
import com.hotelbooking.service.HotelService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HotelServiceImpl implements HotelService {

	@Autowired
    private HotelRepository hotelRepository;


    @Override
    public HotelDTO addHotel(Hotel hotel) {
        Hotel saved = hotelRepository.save(hotel);
        return convertToDTO(saved);
    }

    @Override
    public List<HotelDTO> getAllHotels() {
        List<Hotel> hotels = hotelRepository.findAll();
        List<HotelDTO> dtos = new ArrayList<>();

        for (Hotel h : hotels) {
            dtos.add(convertToDTO(h));
        }

        return dtos;
    }

    @Override
    public HotelDTO getHotelById(Long id) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found"));

        return convertToDTO(hotel);
    }

    @Override
    public HotelDTO updateHotel(Long id, Hotel updated) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found"));

        hotel.setName(updated.getName());
        hotel.setCity(updated.getCity());
        hotel.setAddress(updated.getAddress());

        Hotel saved = hotelRepository.save(hotel);
        return convertToDTO(saved);
    }

    @Override
    public void deleteHotel(Long id) {
        if (!hotelRepository.existsById(id)) {
            throw new ResourceNotFoundException("Hotel not found");
        }
        hotelRepository.deleteById(id);
    }
    
    @Override
    public List<HotelDTO> searchHotelsByCity(String city) {

        List<Hotel> hotels = hotelRepository.findByCityIgnoreCase(city);

        List<HotelDTO> dtos = new ArrayList<>();

        for (Hotel h : hotels) {
            dtos.add(convertToDTO(h));
        }

        return dtos;
    }


    private HotelDTO convertToDTO(Hotel hotel) {
        HotelDTO dto = new HotelDTO();
        dto.setId(hotel.getId());
        dto.setName(hotel.getName());
        dto.setCity(hotel.getCity());
        dto.setAddress(hotel.getAddress());
        return dto;
    }
    
    
}
