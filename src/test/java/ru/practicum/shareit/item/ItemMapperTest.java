package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.practicum.shareit.booking.StatusBooking;
import ru.practicum.shareit.booking.dto.BookingInfoDto;
import ru.practicum.shareit.helpers.GeneratorConverterHelper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.*;

public class ItemMapperTest extends GeneratorConverterHelper {
    private Item item;
    private ItemDto itemDto;

    @BeforeEach
    public void beforeEach() {
        item = generateItem();

        ItemRequest itemRequest = generateItemRequest(item);
        item.setRequest(itemRequest);

        itemDto = itemToItemDto(item);
    }

    @Test
    void shouldCreateItemDto() {
        BookingInfoDto lastBooking = Mockito.mock(BookingInfoDto.class);
        BookingInfoDto nextBooking = Mockito.mock(BookingInfoDto.class);

        when(lastBooking.getId()).thenReturn(1L);
        when(lastBooking.getBooker()).thenReturn(generateUser());
        when(lastBooking.getStatus()).thenReturn(StatusBooking.APPROVED);

        when(nextBooking.getId()).thenReturn(2L);
        when(nextBooking.getBooker()).thenReturn(generateUser());
        when(nextBooking.getStatus()).thenReturn(StatusBooking.APPROVED);

        item.setLastBooking(lastBooking);
        item.setNextBooking(nextBooking);

        ItemDto testItemDto = ItemMapper.toItemDto(item);

        assertEquals(testItemDto.getId(), item.getId());
        assertEquals(testItemDto.getAvailable(), item.getAvailable());
        assertEquals(testItemDto.getName(), item.getName());
        assertEquals(testItemDto.getDescription(), item.getDescription());
        assertEquals(testItemDto.getOwner(), item.getOwner().getId());
        assertEquals(testItemDto.getLastBooking().getId(), item.getLastBooking().getId());
        assertEquals(testItemDto.getNextBooking().getId(), item.getNextBooking().getId());
        assertEquals(testItemDto.getRequestId(), item.getRequest().getId());
    }

    @Test
    void shouldCreateItem() {
        Item testItem = ItemMapper.toItem(itemDto, itemDto.getOwner());

        assertEquals(testItem.getId(), itemDto.getId());
        assertEquals(testItem.getOwner().getId(), itemDto.getOwner());
        assertEquals(testItem.getName(), itemDto.getName());
        assertEquals(testItem.getDescription(), itemDto.getDescription());
        assertEquals(testItem.getAvailable(), itemDto.getAvailable());
        assertEquals(testItem.getRequest().getId(), itemDto.getRequestId());
    }

    @Test
    void shouldNotEquals() {
        Item testItem = generateItem();

        assertNotEquals(item, testItem);
    }
}
