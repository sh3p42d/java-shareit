package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.error.ItemNotFoundByUserException;
import ru.practicum.shareit.item.error.ItemNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.service.UserService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserService userService;


    @Override
    public Optional<Item> add(Item item, long owner) {
        userService.getById(owner);
        item.setOwner(owner);
        return itemRepository.add(item);
    }

    @Override
    public Optional<Item> getById(long id) {
        return Optional.of(itemRepository.getById(id)
                .orElseThrow(() -> new ItemNotFoundException(id)));
    }

    @Override
    public List<Item> getAll(long owner) {
        return itemRepository.getAll(owner);
    }

    @Override
    public Optional<Item> update(Item item, long id, long owner) {
        itemRepository.getById(id).map(Item::getOwner).filter(s -> s == owner)
                .orElseThrow(() -> new ItemNotFoundByUserException(id, owner));
        return itemRepository.update(item, id);
    }

    @Override
    public List<Item> search(String text) {
        if (text.isEmpty()) return Collections.emptyList();
        return itemRepository.search(text.toLowerCase());
    }
}
