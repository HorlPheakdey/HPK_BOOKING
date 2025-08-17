package com.devcambodia.HPK_Booking.dto.req;

import lombok.Data;

@Data
public class Register {
    private String username;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String phone;
    private String imageUrl;
}
