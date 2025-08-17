package com.devcambodia.HPK_Booking.repository;

import com.devcambodia.HPK_Booking.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {
    boolean existsByName(String name);
    Set<Role> findByNameIn(Set<String> name);
    Optional<Role> findByName(String name);
}
