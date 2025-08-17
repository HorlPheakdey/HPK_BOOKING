package com.devcambodia.HPK_Booking.mapper;

import com.devcambodia.HPK_Booking.dto.res.AuthResponse;
import com.devcambodia.HPK_Booking.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public  AuthResponse toAuthResponse(User user){
       AuthResponse authResponse = new AuthResponse();
       BeanUtils.copyProperties(user,authResponse);
       return authResponse;
    }
}
