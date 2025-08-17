package com.devcambodia.HPK_Booking.model;

import com.devcambodia.HPK_Booking.utils.UserStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tbl_users")
@Getter
@Setter
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String username;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "tbl_user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();
    @Enumerated(EnumType.STRING)
    private UserStatus status;
    private int loginCount;
    private String phone;
    private String accessToken;
    private String refreshToken;
    private String imageUrl;
    private Long createdBy;
    private Long modifiedBy;
    private Date createdAt;
    private Date modifiedAt;
}
