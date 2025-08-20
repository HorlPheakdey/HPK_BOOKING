package com.devcambodia.HPK_Booking.service.impl;

import ch.qos.logback.core.util.StringUtil;
import com.devcambodia.HPK_Booking.dto.req.Login;
import com.devcambodia.HPK_Booking.dto.req.Register;
import com.devcambodia.HPK_Booking.dto.res.AuthResponse;
import com.devcambodia.HPK_Booking.exception.HandleDuplicate;
import com.devcambodia.HPK_Booking.exception.HandleNotFound;
import com.devcambodia.HPK_Booking.exception.HandleNotNull;
import com.devcambodia.HPK_Booking.exception.HandlePassword;
import com.devcambodia.HPK_Booking.mapper.UserMapper;
import com.devcambodia.HPK_Booking.model.Role;
import com.devcambodia.HPK_Booking.model.User;
import com.devcambodia.HPK_Booking.repository.RoleRepository;
import com.devcambodia.HPK_Booking.repository.UserRepository;
import com.devcambodia.HPK_Booking.security.CustomUserDetail;
import com.devcambodia.HPK_Booking.security.CustomUserDetailService;
import com.devcambodia.HPK_Booking.service.AuthService;
import com.devcambodia.HPK_Booking.service.JwtService;
import com.devcambodia.HPK_Booking.utils.UserStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {
    private final JwtService jwtService;
    private final CustomUserDetailService customUserDetailService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
   private final UserMapper userMapper;
   private final RoleRepository roleRepository;
    private CustomUserDetail customUserDetail;
    @Override
    public AuthResponse register(Register register) {
        User user = new User();
        if (userRepository.existsByEmail(register.getEmail())) {
            log.info("User with email {} already exists", register.getEmail());
            throw new HandleDuplicate("User with email already exists");
        }
        if (userRepository.existsByPhone(register.getPhone())) {
            log.info("User with Phone {} already exists", register.getPhone());
            throw new HandleDuplicate("User with Phone already exists");
        }
        if (userRepository.existsByUsername(register.getUsername())) {
            log.info("User with Username {} already exists", register.getUsername());
            throw new HandleDuplicate("User with Username already exists");
        }
        user.setFirstName(register.getFirstName());
        user.setLastName(register.getLastName());
        user.setEmail(register.getEmail());
        user.setPhone(register.getPhone());
        user.setPassword(passwordEncoder.encode(register.getPassword()));
        user.setEmail(register.getEmail());
        user.setUsername(register.getUsername());
        Role role = roleRepository.findByName("ROLE_USER").orElseThrow(() -> new HandleNotFound("This role not found USER"));
        user.getRoles().add(role);
        user.setImageUrl(register.getImageUrl());
        user.setStatus(UserStatus.ACTIVE);
        user.setLoginCount(0);
        user.setCreatedAt(new Date(System.currentTimeMillis()));
        CustomUserDetail customUserDetail = new CustomUserDetail();
        String accessToken = jwtService.generateAccessToken(customUserDetail);
        String refreshToken = jwtService.generateRefreshToken(customUserDetail);
        user.setAccessToken(accessToken);
        user.setRefreshToken(refreshToken);
        userRepository.save(user);
        return userMapper.toAuthResponse(user);
    }

    @Override
    public AuthResponse login(Login login) {
         String email = login.getEmail();
         String password = login.getPassword();
        CustomUserDetail customUserDetail = (CustomUserDetail) customUserDetailService.loadUserByUsername(email);
        if (StringUtil.isNullOrEmpty(password)) {
            log.error("Password is null or empty");
            throw new HandleNotNull("Password is null or empty");
        }
        if (!passwordEncoder.matches(password, customUserDetail.getPassword())) {
            log.error("Invalid Password");
            throw new HandlePassword("Invalid Password");
        }
        customUserDetailService.saveUserLoginCount(customUserDetail.getEmail());
        AuthResponse authResponse = new AuthResponse();
        Optional<User> obj = userRepository.findFirstByEmailAndStatus(email, UserStatus.ACTIVE);
        if (obj.isPresent()){
            authResponse.accessToken = obj.get().getAccessToken();
            authResponse.refreshToken = obj.get().getRefreshToken();
        }
        return authResponse;
    }

    @Override
    public void logout() {

    }
}
