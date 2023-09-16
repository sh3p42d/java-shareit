package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemService {
    Optional<Item> add(Item item, long userId);

    Optional<Item> getById(long id);

    List<Item> getAll(long userId);

    Optional<Item> update(Item item, long id, long userId);

    List<Item> search(String text);
}
