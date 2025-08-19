package com.devcambodia.HPK_Booking.security.filter;

import com.devcambodia.HPK_Booking.config.JwtConfig;
import com.devcambodia.HPK_Booking.dto.req.Login;
import com.devcambodia.HPK_Booking.dto.res.AuthResponse;
import com.devcambodia.HPK_Booking.security.CustomUserDetail;
import com.devcambodia.HPK_Booking.security.CustomUserDetailService;
import com.devcambodia.HPK_Booking.service.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class JwtAuthFilter extends AbstractAuthenticationProcessingFilter {
    private JwtService jwtService;
    private CustomUserDetailService customUserDetailService;
   public JwtAuthFilter(JwtService jwtService,
                        CustomUserDetailService customUserDetailService,
                        JwtConfig  jwtConfig,
                        AuthenticationManager authenticationManager) {
       super(new AntPathRequestMatcher(jwtConfig.getUrl(), "POST"));
       setAuthenticationManager(authenticationManager);
       this.jwtService = jwtService;
       this.customUserDetailService = customUserDetailService;

    }
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {
       Login login = new ObjectMapper().readValue(request.getInputStream(), Login.class);
       customUserDetailService.loadUserByUsername(login.getEmail());
        return getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(
                login.getEmail(), login.getPassword(),
                Collections.emptyList()
        ));
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult) throws IOException, ServletException {
       // super.successfulAuthentication(request, response, chain, authResult);
        CustomUserDetail customUserDetail = (CustomUserDetail) authResult.getPrincipal();
        var accessToken = jwtService.generateAccessToken(customUserDetail);
        var refreshToken = jwtService.generateRefreshToken(customUserDetail);
        customUserDetailService.updateLoginCount(customUserDetail.getEmail());
        customUserDetailService.saveUserLoginCount(customUserDetail.getEmail());
        AuthResponse authResponse = new AuthResponse(accessToken, refreshToken);
        var authRes = new ObjectMapper().writeValueAsString(authResponse);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(authRes);
        log.info("Successfully logged in");
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
       // super.unsuccessfulAuthentication(request, response, failed);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        Map<String, Object> error = new HashMap<>();
        error.put("error", "Authentication failed");
        error.put("message", failed.getMessage());
        error.put("path", request.getRequestURI());
        error.put("timestamp",new Date(System.currentTimeMillis()).toString());
        new ObjectMapper().writeValue(response.getOutputStream(), error);
        log.info("Unsuccessful logged in");
    }
}
