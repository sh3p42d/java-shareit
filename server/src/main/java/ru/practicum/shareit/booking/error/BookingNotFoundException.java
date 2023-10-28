package ru.practicum.shareit.booking.error;

import javax.persistence.EntityNotFoundException;

public class BookingNotFoundException extends EntityNotFoundException {
    public BookingNotFoundException(Long id) {
        super(String.format("Booking с id=%s не найден", id));
    }

}