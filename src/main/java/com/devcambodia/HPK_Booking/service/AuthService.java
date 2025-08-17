package com.devcambodia.HPK_Booking.service;

import com.devcambodia.HPK_Booking.dto.req.Login;
import com.devcambodia.HPK_Booking.dto.req.Register;
import com.devcambodia.HPK_Booking.dto.res.AuthResponse;

public interface AuthService {
    AuthResponse register(Register register);
    AuthResponse login(Login login);
    void logout();
}
