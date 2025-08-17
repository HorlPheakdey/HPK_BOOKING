package com.devcambodia.HPK_Booking.security.filter;

import com.devcambodia.HPK_Booking.exception.HandleNotFound;
import com.devcambodia.HPK_Booking.model.Role;
import com.devcambodia.HPK_Booking.model.User;
import com.devcambodia.HPK_Booking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.*;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class CustomAuthenticationProvider implements AuthenticationProvider {
    private final UserRepository userRepository;
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        log.info("Authentication income {}", authentication);
        var email = authentication.getName();
        var password = authentication.getCredentials().toString();
        Optional<User> user = userRepository.findAllByEmailAndStatus(email,"ACTIVE");
        if(user.isEmpty()){
            log.info("User not found", email);
            throw new HandleNotFound("User not found");
        }
         List<GrantedAuthority> grantedAuthorities = grantedAuthorities(user.get().getRoles().stream().toList());
         Authentication authz = new UsernamePasswordAuthenticationToken(email, password, grantedAuthorities);
        log.info("Authentication out come {}", authz);
        return authz;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return false;
    }
    private List<GrantedAuthority> grantedAuthorities(List<Role> roles) {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        Set<String> permissions = new HashSet<>();
        if(!roles.isEmpty()){
            roles.forEach(role -> {
                permissions.add(role.getName());
            });
        }
        permissions.forEach(permission -> grantedAuthorities.add(new SimpleGrantedAuthority(permission)));
        return grantedAuthorities;
    }
}
