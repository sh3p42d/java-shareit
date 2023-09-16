package ru.practicum.shareit.item.dao;

import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemRepository {
    Optional<Item> add(Item item);

    Optional<Item> getById(long id);

    List<Item> getAll(long userId);

    Optional<Item> update(Item item, long id);

    List<Item> search(String text);
}
