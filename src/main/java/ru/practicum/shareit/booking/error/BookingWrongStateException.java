package ru.practicum.shareit.booking.error;

public class BookingWrongStateException extends RuntimeException {
    public BookingWrongStateException(String state) {
        super(state);
    }
}