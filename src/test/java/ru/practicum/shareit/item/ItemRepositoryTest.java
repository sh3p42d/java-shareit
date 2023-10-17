package ru.practicum.shareit.item;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.helpers.GeneratorConverterHelper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class ItemRepositoryTest extends GeneratorConverterHelper {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    ItemRequestRepository itemRequestRepository;

    private User itemOwner;
    private Item item;
    private ItemRequest itemRequest;

    @BeforeEach
    public void beforeEach() {
        itemOwner = userRepository.save(generateUserForRepository());
        item = itemRepository.save(generateItemForRepository(itemOwner));
        itemRequest = itemRequestRepository
                .save(generateItemRequestForRepository(userRepository.save(generateUserForRepository()), item));
        item.setRequest(itemRequest);
        item = itemRepository.save(item);
    }

    @AfterEach
    public void afterEach() {
        itemRepository.deleteAll();
    }

    @Test
    void shouldFindAllByNameOrDescriptionContainingIgnoreCaseAndAvailableTrue() {
        List<Item> result = itemRepository
                .findAllByNameOrDescriptionContainingIgnoreCaseAndAvailableTrue(item.getName(), item.getName(), Pageable.unpaged());

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(item, result.get(0));
    }

    @Test
    void shouldFindAllByOwnerId() {
        List<Item> result = itemRepository
                .findAllByOwnerId(itemOwner.getId(), Pageable.unpaged());

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(item, result.get(0));
    }

    @Test
    void shouldFindAllByRequestId() {
        List<Item> result = itemRepository
                .findAllByRequestId(itemRequest.getId());

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(item, result.get(0));
    }
}
