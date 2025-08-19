package com.devcambodia.HPK_Booking.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tbl_room")
@Getter
@Setter
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String type;
    private int capacity;
    private boolean available;
    @ManyToOne
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;

}
