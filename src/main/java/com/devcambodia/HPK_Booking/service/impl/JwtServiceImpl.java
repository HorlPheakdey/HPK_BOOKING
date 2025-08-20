package com.devcambodia.HPK_Booking.service.impl;

import com.devcambodia.HPK_Booking.config.JwtConfig;
import com.devcambodia.HPK_Booking.exception.HandleExpiration;
import com.devcambodia.HPK_Booking.exception.HandleInvalidFormat;
import com.devcambodia.HPK_Booking.exception.HandleUnsupport;
import com.devcambodia.HPK_Booking.security.CustomUserDetail;
import com.devcambodia.HPK_Booking.security.CustomUserDetailService;
import com.devcambodia.HPK_Booking.service.JwtService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
public class JwtServiceImpl extends JwtConfig implements JwtService {
    private final CustomUserDetailService customUserDetailService;

    public JwtServiceImpl(@Lazy CustomUserDetailService customUserDetailService) {
        this.customUserDetailService = customUserDetailService;
    }

    @Override
    public Key getKeys() {
        byte[] key = Decoders.BASE64.decode(getSecretKey());
        return Keys.hmacShaKeyFor(key);
    }

    @Override
    public Claims getClaims(String token) {
        try {
            return Jwts.parserBuilder().setSigningKey(getKeys()).build().parseClaimsJws(token).getBody();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public String generateAccessToken(CustomUserDetail customUserDetail) {
        List<String> roles = new ArrayList<>();
        customUserDetail.getAuthorities().forEach(role ->
                roles.add(role.getAuthority()));
        try {
            return Jwts.builder().setSubject(customUserDetail.getEmail())
                    .claim("authorities", customUserDetail.getAuthorities()
                            .stream().map(GrantedAuthority::getAuthority)
                            .collect(Collectors.toList()))
                    .claim("roles", roles)
                    .claim("isEnable", customUserDetail.isEnabled())
                    .setIssuer("devcambodia")
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + getExpiration()))
                    .signWith(getKeys(), SignatureAlgorithm.HS256).compact();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }

    }

    @Override
    public String generateRefreshToken(CustomUserDetail customUserDetail) {
        try {
            return Jwts.builder().setSubject(customUserDetail.getEmail())
                    .setIssuer("devcambodia")
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + getExpirationRefreshToken()))
                    .signWith(getKeys(), SignatureAlgorithm.HS256).compact();

        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public boolean validateToken(String token) {
        final String email = extractEmail(token);
        UserDetails userDetails = customUserDetailService.loadUserByUsername(email);
        return userDetails != null;
    }

    @Override
    public boolean validateRefreshToken(String token) {
        return false;
    }

    @Override
    public Date validateExpirationDate(String token) {
        return null;
    }

    @Override
    public Date claimsDate(String token) {
        Claims claims = getClaims(token);
        return claims.getExpiration();
    }

    private String extractEmail(String token) {
        return extractClaimsTFunction(token, Claims::getSubject);
    }

    private <T> T extractClaimsTFunction(String token, Function<Claims, T> claimsTFunction) {
        final Claims claims = extractAllClaims(token);
        return claimsTFunction.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getKeys())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException ex) {
            log.error(ex.getLocalizedMessage());
            throw new HandleExpiration("Token has expired");
        } catch (UnsupportedJwtException ex) {
            log.error(ex.getLocalizedMessage());
            throw new HandleUnsupport("Token has unsupported token");
        } catch (MalformedJwtException | SignatureException ex) {
            log.error(ex.getLocalizedMessage());
            throw new HandleInvalidFormat("Token invalid format");
        } catch (Exception ex) {
            log.error(ex.getLocalizedMessage());
            throw new RuntimeException(ex.getMessage());
        }
    }
}
