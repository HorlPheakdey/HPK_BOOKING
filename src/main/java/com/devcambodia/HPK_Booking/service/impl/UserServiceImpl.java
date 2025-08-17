package com.devcambodia.HPK_Booking.service.impl;

import com.devcambodia.HPK_Booking.dto.res.UserResponse;
import com.devcambodia.HPK_Booking.model.User;
import com.devcambodia.HPK_Booking.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    @Override
    public UserResponse createUser(User user) {
        return null;
    }

    @Override
    public UserResponse updateUser(Long id, User user) {
        return null;
    }

    @Override
    public UserResponse deleteUser(Long id) {
        return null;
    }

    @Override
    public UserResponse getUserById(Long id) {
        return null;
    }

    @Override
    public UserResponse getUserByUsername(String username) {
        return null;
    }

    @Override
    public UserResponse getUserByEmail(String email) {
        return null;
    }

    @Override
    public Page<UserResponse> getUsersByPage(Pageable pageable) {
        return null;
    }
}
