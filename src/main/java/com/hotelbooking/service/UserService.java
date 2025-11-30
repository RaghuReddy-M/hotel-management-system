package com.hotelbooking.service;

import com.hotelbooking.dto.UserDTO;
import com.hotelbooking.entity.User;

public interface UserService {

    UserDTO registerUser(User user);

    User findByEmail(String email);
}
