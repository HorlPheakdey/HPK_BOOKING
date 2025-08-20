package com.devcambodia.HPK_Booking.repository;

import com.devcambodia.HPK_Booking.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {
    boolean existsByName(String name);
    Set<Role> findByNameIn(Set<String> name);
    Optional<Role> findByName(String name);
    @Query("SELECT u.roles FROM User u WHERE u.id = :userId")
    List<Role> getRoleByUserId(@Param("userId") Long userId);
}
