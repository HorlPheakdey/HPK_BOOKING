package com.devcambodia.HPK_Booking.dto.res;

import com.devcambodia.HPK_Booking.model.Role;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class UserResponse {
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private List<Role> roles;
    private String status;
    private String phone;
    private String imageUrl;
    private Long createdBy;
    private Long modifiedBy;
    private Date createdAt;
    private Date modifiedAt;
}
