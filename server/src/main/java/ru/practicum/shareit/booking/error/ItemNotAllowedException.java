package ru.practicum.shareit.booking.error;

public class ItemNotAllowedException extends RuntimeException {
    public ItemNotAllowedException(Long id) {
        super(String.format("Item с id=%s недоступен для бронирования.", id));
    }
}