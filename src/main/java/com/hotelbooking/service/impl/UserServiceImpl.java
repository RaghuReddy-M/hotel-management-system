package com.hotelbooking.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.hotelbooking.dao.UserRepository;
import com.hotelbooking.dto.UserDTO;
import com.hotelbooking.entity.User;
import com.hotelbooking.exception.BadRequestException;
import com.hotelbooking.service.UserService;

@Service
public class UserServiceImpl implements UserService
{
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	
//	public User convertDtoToEntity(UserDTO dto)
//	{
//		User user = new User();
//		BeanUtils.copyProperties(dto, user);
//		return user;
//	}
	


	@Override
	public UserDTO registerUser(User user) {
       if (userRepository.existsByEmail(user.getEmail())) {
    	      throw new BadRequestException("Email already registered");

	   }
       
    // ✅ Encrypt password before saving
       String encodedPassword = passwordEncoder.encode(user.getPassword());
   	   user.setPassword(encodedPassword);
   	
       User savedUser = userRepository.save(user);
	   UserDTO userDTO = new UserDTO();
	   BeanUtils.copyProperties(savedUser, userDTO);
	   return userDTO;
       
	}

	@Override
	public User findByEmail(String email) {
		 return userRepository.findByEmail(email)
	                .orElse(null);
	}

}
