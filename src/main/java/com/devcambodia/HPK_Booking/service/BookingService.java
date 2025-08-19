package com.devcambodia.HPK_Booking.service;

import com.devcambodia.HPK_Booking.model.Booking;

import java.util.Date;
import java.util.List;

public interface BookingService {
    Booking createBooking(Long userId, Long roomId, Date checkIn,Date checkOut );
    Booking cancelBooking(Long bookingId);
    List<Booking> getBookings();
}
