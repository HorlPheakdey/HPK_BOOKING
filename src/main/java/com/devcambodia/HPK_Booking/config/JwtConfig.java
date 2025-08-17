package com.devcambodia.HPK_Booking.config;


import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class JwtConfig {
    @Value("${jwt_expirationTimeAccessToken}")
    protected Long expiration;
    @Value("${Jwt_expirationTimeRefreshToken}")
    protected Long expirationRefreshToken;
    @Value("${jwt_secretKey}")
    protected String secretKey;
    @Value("${Jwt_header}")
    protected String header;
    @Value("${Jwt_url}")
    protected String url;
    @Value("${Jwt_prefix}")
    protected String prefix;
}
