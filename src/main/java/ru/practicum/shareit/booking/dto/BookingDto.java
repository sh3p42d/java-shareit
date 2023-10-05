package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.StatusBooking;
import ru.practicum.shareit.item.dto.ItemBookingResponseDto;
import ru.practicum.shareit.user.model.User;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
public class BookingDto implements Serializable {
    private final long id;
    private final User booker;
    private final ItemBookingResponseDto item;
    private final LocalDateTime start;
    private final LocalDateTime end;
    private final StatusBooking status;
}