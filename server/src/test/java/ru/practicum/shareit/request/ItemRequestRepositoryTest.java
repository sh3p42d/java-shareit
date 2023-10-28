package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.helpers.GeneratorConverterHelper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class ItemRequestRepositoryTest extends GeneratorConverterHelper {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ItemRequestRepository itemRequestRepository;

    private User itemOwner;
    private User author;
    private Item item;
    private ItemRequest itemRequest;

    @BeforeEach
    public void beforeEach() {
        itemOwner = userRepository.save(generateUserForRepository());
        author = userRepository.save(generateUserForRepository());
        item = itemRepository.save(generateItemForRepository(itemOwner));
        itemRequest = itemRequestRepository
                .save(generateItemRequestForRepository(author, item));
        item.setRequest(itemRequest);
        item = itemRepository.save(item);
    }

    @Test
    void shouldFindAllByAuthorIdOrderByCreatedAsc() {
        List<ItemRequest> result = itemRequestRepository.findAllByAuthorIdOrderByCreatedAsc(author.getId());

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(itemRequest, result.get(0));
    }

    @Test
    void shouldFindAllByAuthorIdNotOrderByCreatedAsc() {
        Page<ItemRequest> result = itemRequestRepository.findAllByAuthorIdNotOrderByCreatedAsc(itemOwner.getId(), Pageable.unpaged());

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(itemRequest, result.stream().collect(Collectors.toList()).get(0));
    }
}
