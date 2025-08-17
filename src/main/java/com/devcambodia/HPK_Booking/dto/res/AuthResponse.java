package com.devcambodia.HPK_Booking.dto.res;


import lombok.Data;

@Data
public class AuthResponse {
    public String accessToken;
    public String refreshToken;

    public AuthResponse(String accessToken, String refreshToken) {
    }

    public AuthResponse() {
    }
}
