package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    Item add(Item item, long userId);

    Item getById(long id, long userId);

    List<Item> getAll(long userId, int from, int size);

    Item update(Item item, long id, long userId);

    List<Item> search(String text, int from, int size);

    Comment addComment(Comment dto, long author, long itemId);
}