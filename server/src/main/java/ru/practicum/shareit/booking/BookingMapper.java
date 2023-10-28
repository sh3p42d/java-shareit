package ru.practicum.shareit.booking;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.sql.Timestamp;

@UtilityClass
public class BookingMapper {
    public BookingDto toBookingDto(Booking booking) {
        return BookingDto.builder()
                .id(booking.getId())
                .booker(BookingDto.Booker.builder()
                        .id(booking.getBooker().getId())
                        .name(booking.getBooker().getName())
                        .build())
                .item(BookingDto.Item.builder()
                        .id(booking.getItem().getId())
                        .name(booking.getItem().getName())
                        .build())
                .start(booking.getStart().toLocalDateTime())
                .end(booking.getEnd().toLocalDateTime())
                .status(booking.getStatus())
                .build();
    }

    public Booking toBooking(BookingResponseDto bookingDto, final long userId) {
        return Booking.builder()
                .booker(User.builder()
                        .id(userId)
                        .build())
                .item(Item.builder()
                        .id(bookingDto.getItemId())
                        .build())
                .start(Timestamp.valueOf(bookingDto.getStart()))
                .end(Timestamp.valueOf(bookingDto.getEnd()))
                .status(StatusBooking.WAITING)
                .build();
    }
}