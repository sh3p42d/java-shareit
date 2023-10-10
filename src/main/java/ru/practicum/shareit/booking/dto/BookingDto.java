package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.StatusBooking;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
public class BookingDto implements Serializable {
    private final long id;
    private final Booker booker;
    private final Item item;

    @Data
    @Builder
    public static class Booker implements Serializable {
        private final long id;
        private final String name;
    }

    @Data
    @Builder
    public static class Item implements Serializable {
        private final long id;
        private final String name;
    }

    private final LocalDateTime start;
    private final LocalDateTime end;
    private final StatusBooking status;
}