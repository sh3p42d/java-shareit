package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.helpers.GeneratorConverterHelper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class BookingMapperTest extends GeneratorConverterHelper {
    private Booking booking;
    private BookingResponseDto bookingResponseDto;

    @BeforeEach
    public void beforeEach() {
        booking = generateBooking();
        bookingResponseDto = bookingToBookingResponseDto(booking);
    }

    @Test
    void shouldCreateBookingDto() {
        BookingDto bookingDto = BookingMapper.toBookingDto(booking);

        assertEquals(bookingDto.getId(), booking.getId());
        assertEquals(bookingDto.getBooker().getId(), booking.getBooker().getId());
        assertEquals(bookingDto.getItem().getId(), booking.getItem().getId());
        assertEquals(bookingDto.getStatus(), booking.getStatus());
        assertEquals(bookingDto.getStart(), booking.getStart().toLocalDateTime());
        assertEquals(bookingDto.getEnd(), booking.getEnd().toLocalDateTime());
    }

    @Test
    void shouldCreateBooking() {
        Booking testBooking = BookingMapper.toBooking(bookingResponseDto, booking.getBooker().getId());

        assertEquals(testBooking.getItem().getId(), bookingResponseDto.getItemId());
        assertEquals(testBooking.getStart().toLocalDateTime(), bookingResponseDto.getStart());
        assertEquals(testBooking.getEnd().toLocalDateTime(), bookingResponseDto.getEnd());
    }

    @Test
    void shouldNotEquals() {
        Booking testBooking = generateBooking();

        assertNotEquals(testBooking, booking);
    }
}
