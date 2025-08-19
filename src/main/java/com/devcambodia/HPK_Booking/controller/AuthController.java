package com.devcambodia.HPK_Booking.controller;

import com.devcambodia.HPK_Booking.dto.req.Login;
import com.devcambodia.HPK_Booking.dto.req.Register;
import com.devcambodia.HPK_Booking.dto.res.AuthResponse;
import com.devcambodia.HPK_Booking.service.AuthService;
import com.devcambodia.HPK_Booking.utils.CustomResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;
    private final CustomResponse customResponse;

    @PostMapping("/login")
    public ResponseEntity<Object> login(@Valid @RequestBody Login login){
        AuthResponse authResponse = authService.login(login);
        log.info("Login Successfully");
        return customResponse.success("Login Successfully",authResponse, HttpStatus.OK);
    }
    @PostMapping("/create")
    public ResponseEntity<Object> register(@Valid @RequestBody Register register){
        AuthResponse authResponse = authService.register(register);
        log.info("Register Successfully");
        return customResponse.success("Register Successfully",authResponse, HttpStatus.OK);
    }
}
