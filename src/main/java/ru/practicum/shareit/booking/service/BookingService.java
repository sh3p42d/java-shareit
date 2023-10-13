package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

public interface BookingService {

    Booking add(Booking booking, long bookerId);

    Booking approved(long id, long owner, boolean approved);

    Booking getById(long id, long userId);

    List<Booking> findAll(long bookerId, String state, int from, int size);

    List<Booking> findAllOwner(long ownerId, String state, int from, int size);
}