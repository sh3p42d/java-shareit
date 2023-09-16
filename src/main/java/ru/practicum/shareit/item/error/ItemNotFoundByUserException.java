package ru.practicum.shareit.item.error;

public class ItemNotFoundByUserException extends RuntimeException {
    public ItemNotFoundByUserException(final long id, final long owner) {
        super(String.format("Item с id=%s у User с id=%s не найден", id, owner));
    }
}
