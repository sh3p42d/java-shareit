package ru.practicum.shareit.booking.error;

import javax.persistence.EntityNotFoundException;

public class BookingNotAllowedException extends EntityNotFoundException {
    public BookingNotAllowedException(Long id) {
        super(String.format("Booking для Item с id=%s недоступен для этого User", id));
    }
}