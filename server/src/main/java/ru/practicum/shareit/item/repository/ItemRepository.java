package ru.practicum.shareit.item.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    Page<Item> findAllByNameOrDescriptionContainingIgnoreCaseAndAvailableTrue(String name, String description, Pageable pageable);

    Page<Item> findAllByOwnerId(long id, Pageable pageable);

    List<Item> findAllByRequestId(long id);
}