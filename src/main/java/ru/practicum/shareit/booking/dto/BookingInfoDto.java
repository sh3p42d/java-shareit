package ru.practicum.shareit.booking.dto;

import ru.practicum.shareit.booking.StatusBooking;
import ru.practicum.shareit.user.model.User;

public interface BookingInfoDto {
    long getId();

    User getBooker();

    StatusBooking getStatus();
}
