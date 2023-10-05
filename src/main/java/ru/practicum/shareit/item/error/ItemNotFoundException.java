package ru.practicum.shareit.item.error;

import javax.persistence.EntityNotFoundException;

public class ItemNotFoundException extends EntityNotFoundException {
    public ItemNotFoundException(Long id) {
        super(String.format("Item с id=%s не найден", id));
    }
}
