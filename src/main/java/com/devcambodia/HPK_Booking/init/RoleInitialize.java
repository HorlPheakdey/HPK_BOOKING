package com.devcambodia.HPK_Booking.init;

import com.devcambodia.HPK_Booking.model.Role;
import com.devcambodia.HPK_Booking.model.User;
import com.devcambodia.HPK_Booking.repository.RoleRepository;
import com.devcambodia.HPK_Booking.repository.UserRepository;
import com.devcambodia.HPK_Booking.utils.UserStatus;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class RoleInitialize {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init(){
        if (roleRepository.count()==0){
            Role role = new Role();
            role.setName("ROLE_USER");
            roleRepository.save(role);
            Role role2 = new Role();
            role2.setName("ROLE_ADMIN");
            roleRepository.save(role2);
            Role role3 = new Role();
            role3.setName("SUPER_ADMIN");
            roleRepository.save(role3);
            User user = new User();
            user.setUsername("Admin");
            user.setEmail("pheakdey@gmail.com");
            user.setPassword(passwordEncoder.encode("admin123"));
            user.setStatus(UserStatus.ACTIVE);
            user.setRoles(Set.of(role3,role2,role));
            userRepository.save(user);
        }

    }


}
