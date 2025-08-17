package com.devcambodia.HPK_Booking.service;

import com.devcambodia.HPK_Booking.security.CustomUserDetail;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;

public interface JwtService {
    Key getKeys();
    Claims getClaims(String token);
    String generateAccessToken(CustomUserDetail customUserDetail);
    String generateRefreshToken(CustomUserDetail customUserDetail);
    boolean validateToken(String token);
    boolean validateRefreshToken(String token);
    Date validateExpirationDate(String token);
    Date claimsDate(String token);
}
