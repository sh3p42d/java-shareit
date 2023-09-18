package ru.practicum.shareit.booking.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.StatusBooking;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Booking {
    private long id;
    private long booker;
    private long item;
    private LocalDateTime start;
    private LocalDateTime end;
    private StatusBooking status;
}
