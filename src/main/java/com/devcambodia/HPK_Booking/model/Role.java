package com.devcambodia.HPK_Booking.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tbl_roles")
@Getter
@Setter
public class Role implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true,nullable = false)
    private String name;
    @ManyToMany(mappedBy = "roles")
    private Set<User> users =new HashSet<>();
    private Long createdBy;
    private Long modifiedBy;
    private Date createdAt;
    private Date modifiedAt;
}
