package ru.practicum.shareit.item.error;

import javax.persistence.EntityNotFoundException;

public class ItemNotFoundByUserException extends EntityNotFoundException {
    public ItemNotFoundByUserException(final long id, final long owner) {
        super(String.format("Item с id=%s у User с id=%s не найден", id, owner));
    }
}
