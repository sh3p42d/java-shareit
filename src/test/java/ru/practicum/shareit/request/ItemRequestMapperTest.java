package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.helpers.GeneratorConverterHelper;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class ItemRequestMapperTest extends GeneratorConverterHelper {
    private ItemRequest itemRequest;
    private ItemRequestDto itemRequestDto;

    @BeforeEach
    public void beforeEach() {
        itemRequest = generateItemRequest(generateItem());
        itemRequestDto = itemRequestToItemRequestDto(itemRequest);
    }

    @Test
    void shouldCreateItemRequestDto() {
        ItemRequestDto testItemRequestDto = ItemRequestMapper.toItemRequestDto(itemRequest);

        assertEquals(testItemRequestDto.getId(), itemRequest.getId());
        assertEquals(testItemRequestDto.getDescription(), itemRequest.getDescription());
        assertEquals(testItemRequestDto.getCreated(), itemRequest.getCreated().toLocalDateTime());
        assertEquals(testItemRequestDto.getItems().get(0).getId(), itemRequest.getItems().get(0).getId());
    }

    @Test
    void shouldCreateItemRequest() {
        ItemRequest testItemRequest = ItemRequestMapper.toItemRequest(itemRequestDto, itemRequest.getAuthor().getId());

        assertEquals(testItemRequest.getId(), itemRequestDto.getId());
        assertEquals(testItemRequest.getDescription(), itemRequestDto.getDescription());
        assertEquals(testItemRequest.getCreated().toLocalDateTime().truncatedTo(ChronoUnit.DAYS), LocalDateTime.now().truncatedTo(ChronoUnit.DAYS));
        assertEquals(testItemRequest.getItems().size(), 0);
    }

    @Test
    void shouldNotEquals() {
        ItemRequest testItemRequest = generateItemRequest(generateItem());

        assertNotEquals(testItemRequest, itemRequest);
    }
}
