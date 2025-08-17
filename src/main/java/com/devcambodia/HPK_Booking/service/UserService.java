package com.devcambodia.HPK_Booking.service;

import com.devcambodia.HPK_Booking.dto.res.UserResponse;
import com.devcambodia.HPK_Booking.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    UserResponse createUser(User user);
    UserResponse updateUser(Long id, User user);
    UserResponse deleteUser(Long id);
    UserResponse getUserById(Long id);
    UserResponse getUserByUsername(String username);
    UserResponse getUserByEmail(String email);
    Page<UserResponse> getUsersByPage(Pageable pageable);
}
