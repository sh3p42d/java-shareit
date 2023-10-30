package ru.practicum.shareit.request;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.helpers.GeneratorConverterHelper;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ItemRequestServiceTest extends GeneratorConverterHelper {

    @Autowired
    private ItemRequestService itemRequestService;

    @MockBean
    private UserService mockUserService;
    @MockBean
    private ItemRepository mockItemRepository;
    @MockBean
    private ItemRequestRepository mockItemRequestRepository;

    private ItemRequest testValueItemRequest;

    @BeforeEach
    void beforeEach() {
        testValueItemRequest = generateItemRequest(generateItem());

        reset(mockItemRepository, mockUserService);
        when(mockUserService.getById(testValueItemRequest.getAuthor().getId()))
                .thenReturn(testValueItemRequest.getAuthor());
    }

    @AfterEach
    void mockVerify() {
        verifyNoMoreInteractions(mockItemRepository, mockUserService);
    }

    @Test
    void shouldAddItemRequest() {
        when(mockItemRequestRepository.save(testValueItemRequest))
                .thenReturn(testValueItemRequest);

        ItemRequest itemRequest = itemRequestService.add(testValueItemRequest, testValueItemRequest.getAuthor().getId());
        assertEquals(testValueItemRequest.getId(), itemRequest.getId());
        assertEquals(testValueItemRequest.getAuthor(), itemRequest.getAuthor());
        assertEquals(testValueItemRequest.getDescription(), itemRequest.getDescription());
        assertEquals(testValueItemRequest.getCreated(), itemRequest.getCreated());
        assertEquals(testValueItemRequest.getItems(), itemRequest.getItems());
        assertEquals(testValueItemRequest.toString(), itemRequest.toString());
        assertEquals(testValueItemRequest.hashCode(), itemRequest.hashCode());

        verify(mockUserService, times(1)).getById(testValueItemRequest.getAuthor().getId());
        verify(mockItemRequestRepository, times(1)).save(itemRequest);
    }

    @Test
    void shouldGetByIdItemRequest() {
        when(mockItemRequestRepository.findById(testValueItemRequest.getId()))
                .thenReturn(Optional.ofNullable(testValueItemRequest));

        ItemRequest itemRequest = itemRequestService
                .getById(testValueItemRequest.getId(), testValueItemRequest.getAuthor().getId());
        assertEquals(testValueItemRequest.getId(), itemRequest.getId());
        assertEquals(testValueItemRequest.getAuthor(), itemRequest.getAuthor());
        assertEquals(testValueItemRequest.getDescription(), itemRequest.getDescription());
        assertEquals(testValueItemRequest.getCreated(), itemRequest.getCreated());
        assertEquals(testValueItemRequest.getItems(), itemRequest.getItems());
        assertEquals(testValueItemRequest.toString(), itemRequest.toString());
        assertEquals(testValueItemRequest.hashCode(), itemRequest.hashCode());

        verify(mockUserService, times(1)).getById(testValueItemRequest.getAuthor().getId());
        verify(mockItemRequestRepository, times(1)).findById(testValueItemRequest.getId());
        verify(mockItemRepository, times(1)).findAllByRequestId(testValueItemRequest.getId());
    }

    @Test
    void shouldGetAllItemRequest() {
        when(mockItemRequestRepository.findAllByAuthorIdOrderByCreatedAsc(testValueItemRequest.getAuthor().getId()))
                .thenReturn(List.of(testValueItemRequest));

        List<ItemRequest> all = itemRequestService.getAll(testValueItemRequest.getAuthor().getId());
        assertEquals(testValueItemRequest.getId(), all.get(0).getId());
        assertEquals(testValueItemRequest.getAuthor(), all.get(0).getAuthor());
        assertEquals(testValueItemRequest.getDescription(), all.get(0).getDescription());
        assertEquals(testValueItemRequest.getCreated(), all.get(0).getCreated());
        assertEquals(testValueItemRequest.getItems(), all.get(0).getItems());
        assertEquals(testValueItemRequest.toString(), all.get(0).toString());
        assertEquals(testValueItemRequest.hashCode(), all.get(0).hashCode());

        verify(mockItemRepository, times(1)).findAllByRequestId(testValueItemRequest.getId());
        verify(mockUserService, times(1)).getById(testValueItemRequest.getAuthor().getId());
    }

    @Test
    void shouldGetPageItemRequest() {
        Page<ItemRequest> page = new PageImpl<>(List.of(testValueItemRequest));

        when(mockItemRequestRepository.findAllByAuthorIdNotOrderByCreatedAsc(anyLong(), any(Pageable.class)))
                .thenReturn(page);

        List<ItemRequest> all = itemRequestService.getPage(0, 10, testValueItemRequest.getAuthor().getId());
        assertEquals(testValueItemRequest.getId(), all.get(0).getId());
        assertEquals(testValueItemRequest.getAuthor(), all.get(0).getAuthor());
        assertEquals(testValueItemRequest.getDescription(), all.get(0).getDescription());
        assertEquals(testValueItemRequest.getCreated(), all.get(0).getCreated());
        assertEquals(testValueItemRequest.getItems(), all.get(0).getItems());
        assertEquals(testValueItemRequest.toString(), all.get(0).toString());
        assertEquals(testValueItemRequest.hashCode(), all.get(0).hashCode());

        verify(mockItemRepository, times(1)).findAllByRequestId(testValueItemRequest.getId());
        verify(mockUserService, times(1)).getById(testValueItemRequest.getAuthor().getId());
    }
}
