package ru.practicum.shareit.request.error;

import javax.persistence.EntityNotFoundException;

public class ItemRequestNotFoundException extends EntityNotFoundException {
    public ItemRequestNotFoundException(Long id) {
        super(String.format("Request с id=%s не найден", id));
    }
}