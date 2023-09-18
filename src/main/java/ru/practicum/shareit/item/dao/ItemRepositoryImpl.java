package ru.practicum.shareit.item.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ItemRepositoryImpl implements ItemRepository {
    private final Map<Long, Item> items;
    private Long id = 1L;

    @Override
    public Optional<Item> add(Item item) {
        item.setId(id++);
        items.put(item.getId(), item);
        return Optional.of(items.get(item.getId()));
    }

    @Override
    public Optional<Item> getById(long id) {
        if (items.containsKey(id)) {
            return Optional.of(items.get(id));
        }
        return Optional.empty();
    }

    @Override
    public List<Item> getAll(long userId) {
        return items.values().stream()
                .filter(item -> item.getOwner() == userId)
                .sorted(Comparator.comparingLong(Item::getId))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Item> update(Item item, long id) {
        Item itemUpdate = items.get(id);
        Optional.ofNullable(item.getName()).ifPresent(itemUpdate::setName);
        Optional.ofNullable(item.getDescription()).ifPresent(itemUpdate::setDescription);
        Optional.ofNullable(item.getAvailable()).ifPresent(itemUpdate::setAvailable);
        return Optional.of(itemUpdate);
    }

    @Override
    public List<Item> search(String text) {
        return items.values().stream()
                .filter(i -> (i.getName().toLowerCase().contains(text)
                        || i.getDescription().toLowerCase().contains(text)) && i.getAvailable())
                .collect(Collectors.toList());
    }
}