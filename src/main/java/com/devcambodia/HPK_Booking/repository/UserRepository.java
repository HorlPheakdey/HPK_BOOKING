package com.devcambodia.HPK_Booking.repository;

import com.devcambodia.HPK_Booking.model.User;
import com.devcambodia.HPK_Booking.utils.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findFirstByEmailAndStatus(String email, UserStatus status);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByPhone(String phone);

    Optional<User> findAllByEmailAndStatus(String email, String status);
    // In your UserRepository interface
    @Query("SELECT u FROM User u JOIN FETCH u.roles WHERE u.email = :email AND u.status = :status")
    Optional<User> findFirstByEmailAndStatusWithRoles(@Param("email") String email, @Param("status") UserStatus status);
}
