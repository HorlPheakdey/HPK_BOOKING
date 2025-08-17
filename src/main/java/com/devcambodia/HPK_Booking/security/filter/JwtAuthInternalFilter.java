package com.devcambodia.HPK_Booking.security.filter;

import com.devcambodia.HPK_Booking.config.JwtConfig;
import com.devcambodia.HPK_Booking.service.JwtService;
import com.devcambodia.HPK_Booking.utils.CustomResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthInternalFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final JwtConfig jwtConfig;
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var accessToken = request.getHeader(jwtConfig.getHeader());
        log.info("Filter {}",request.getRequestURI());
        if (accessToken != null && !accessToken.isEmpty() && accessToken.startsWith(jwtConfig.getPrefix())) {
            accessToken = accessToken.substring(jwtConfig.getPrefix().length());
            try {
                if (jwtService.validateToken(accessToken)) {
                    Claims claims = jwtService.getClaims(accessToken);
                    var email = claims.getSubject();
                    List <String> authorities = claims.get("authorities", List.class);
                    if (email != null) {
                        UsernamePasswordAuthenticationToken authenticationToken =
                                new UsernamePasswordAuthenticationToken(email, null, authorities.stream()
                                        .map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    }
                }
            }catch (Exception e){
                log.error("Invalid token",e.getMessage());
                CustomResponse customResponse = new CustomResponse();
                customResponse.error("Invalid token", HttpStatus.BAD_REQUEST);
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getWriter(), customResponse);
                return;
            }
        }
        log.info("Filter {}",request.getRequestURI());
        filterChain.doFilter(request, response);
    }
}
