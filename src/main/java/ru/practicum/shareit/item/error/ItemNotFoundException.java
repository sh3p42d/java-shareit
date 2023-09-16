package ru.practicum.shareit.item.error;

public class ItemNotFoundException extends RuntimeException {
    public ItemNotFoundException(Long id) {
        super(String.format("Item с id=%s не найден", id));
    }
}
