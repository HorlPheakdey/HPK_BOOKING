package com.devcambodia.HPK_Booking.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "tbl_booking")
@Getter
@Setter
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date checkInDate;
    private Date checkOutDate;
    private String status;
    @ManyToOne
    private User user;
    @ManyToOne
    private Room room;
}
