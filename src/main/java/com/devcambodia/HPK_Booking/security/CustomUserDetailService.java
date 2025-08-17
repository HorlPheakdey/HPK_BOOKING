package com.devcambodia.HPK_Booking.security;

import com.devcambodia.HPK_Booking.model.User;
import com.devcambodia.HPK_Booking.repository.UserRepository;
import com.devcambodia.HPK_Booking.utils.UserStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailService implements UserDetailsService {
    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return this.customUserDetail(email);
    }
    private CustomUserDetail customUserDetail(String email){
        Optional<User> user = userRepository.findFirstByEmailAndStatusWithRoles(email, UserStatus.ACTIVE);
        if(user.isEmpty()){
            log.error("User not found with email {}",email);
            throw new UsernameNotFoundException("User not found with email "+email);
        }
        return new CustomUserDetail(
                user.get().getEmail(),
                user.get().getPassword(),
                user.get().getRoles()
                        .stream().map(role -> new SimpleGrantedAuthority(role.getName()))
                        .collect(Collectors.toList())
        );
    }
    public void saveUserLoginCount(String email){
        Optional<User> user = userRepository.findFirstByEmailAndStatus(email,UserStatus.ACTIVE);
        if (user.isPresent()){
            int loginCount = user.get().getLoginCount()+1;
            user.get().setLoginCount(loginCount);
            user.get().setModifiedAt(new Date());
            if (user.get().getLoginCount() > 3){
                log.warn("Login count exceeded");
                user.get().setStatus(UserStatus.LOCKED);
            }
            userRepository.save(user.get());
        }
    }
    public void updateLoginCount(String email) {
        Optional<User> user = userRepository.findFirstByEmailAndStatus(email,UserStatus.ACTIVE);
        if(user.isPresent()) {
            user.get().setLoginCount(0);
            user.get().setModifiedAt(new Date());
            userRepository.save(user.get());
        }
    }
}
