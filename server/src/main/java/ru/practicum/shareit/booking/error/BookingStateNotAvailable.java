package ru.practicum.shareit.booking.error;

public class BookingStateNotAvailable extends RuntimeException {
    public BookingStateNotAvailable(Long id) {
        super(String.format("Статус Booking с id=%s уже подтвержден.", id));
    }
}