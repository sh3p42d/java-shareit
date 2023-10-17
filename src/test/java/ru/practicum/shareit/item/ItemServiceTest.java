package ru.practicum.shareit.item;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.booking.StatusBooking;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.helpers.GeneratorConverterHelper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ItemServiceTest extends GeneratorConverterHelper {

    @Autowired
    private ItemService itemService;

    @MockBean
    private ItemRepository mockItemRepository;
    @MockBean
    private UserService mockUserService;
    @MockBean
    private CommentRepository mockCommentRepository;
    @MockBean
    private BookingRepository mockBookingRepository;

    private Item testValueItem;

    @BeforeEach
    void beforeEach() {
        testValueItem = generateItem();

        reset(mockItemRepository, mockUserService, mockCommentRepository, mockBookingRepository);
        when(mockItemRepository.findById(testValueItem.getId())).thenReturn(Optional.ofNullable(testValueItem));
    }

    @AfterEach
    void mockVerify() {
        verifyNoMoreInteractions(mockItemRepository, mockUserService, mockCommentRepository, mockBookingRepository);
    }

    @Test
    void shouldAddItem() {
        when(mockUserService.getById(testValueItem.getOwner().getId()))
                .thenReturn(testValueItem.getOwner());
        when(mockItemRepository.save(testValueItem))
                .thenReturn(testValueItem);

        Item item = itemService.add(testValueItem, testValueItem.getOwner().getId());
        assertEquals(testValueItem.getId(), item.getId());
        assertEquals(testValueItem.getOwner(), item.getOwner());
        assertEquals(testValueItem.getName(), item.getName());
        assertEquals(testValueItem.getDescription(), item.getDescription());
        assertEquals(testValueItem.getAvailable(), item.getAvailable());
        assertEquals(testValueItem.getComments(), item.getComments());
        assertEquals(testValueItem.getLastBooking(), item.getLastBooking());
        assertEquals(testValueItem.getNextBooking(), item.getNextBooking());
        assertEquals(testValueItem.getRequest(), item.getRequest());

        verify(mockUserService, times(1)).getById(testValueItem.getOwner().getId());
        verify(mockItemRepository, times(1)).save(item);
    }

    @Test
    void shouldGetItemById() {
        when(mockItemRepository.findById(testValueItem.getId()))
                .thenReturn(Optional.ofNullable(testValueItem));

        Item item = itemService.getById(testValueItem.getId(), testValueItem.getOwner().getId());
        assertEquals(testValueItem.getId(), item.getId());
        assertEquals(testValueItem.getOwner(), item.getOwner());
        assertEquals(testValueItem.getName(), item.getName());
        assertEquals(testValueItem.getDescription(), item.getDescription());
        assertEquals(testValueItem.getAvailable(), item.getAvailable());
        assertEquals(testValueItem.getComments(), item.getComments());
        assertEquals(testValueItem.getLastBooking(), item.getLastBooking());
        assertEquals(testValueItem.getNextBooking(), item.getNextBooking());
        assertEquals(testValueItem.getRequest(), item.getRequest());
        assertEquals(testValueItem.toString(), item.toString());
        assertEquals(testValueItem.hashCode(), item.hashCode());

        verify(mockItemRepository, times(1)).findById(testValueItem.getId());
        verify(mockCommentRepository, times(1)).findAllByItemId(testValueItem.getId());
        verify(mockBookingRepository, times(1))
                .findFirstByItemIdAndStartAfterAndStatus(anyLong(), any(Timestamp.class), any(StatusBooking.class), any(Sort.class));
        verify(mockBookingRepository, times(1))
                .findFirstByItemIdAndStartBeforeAndStatus(anyLong(), any(Timestamp.class), any(StatusBooking.class), any(Sort.class));
    }

    @Test
    void shouldGetAllItem() {
        when(mockItemRepository.findAllByOwnerId(anyLong(), any(PageRequest.class)))
                .thenReturn(List.of(testValueItem));

        List<Item> all = itemService.getAll(testValueItem.getOwner().getId(), 0, 10);
        assertEquals(1, all.size());
        assertEquals(testValueItem.getId(), all.get(0).getId());
        assertEquals(testValueItem.getOwner(), all.get(0).getOwner());
        assertEquals(testValueItem.getName(), all.get(0).getName());
        assertEquals(testValueItem.getDescription(), all.get(0).getDescription());
        assertEquals(testValueItem.getAvailable(), all.get(0).getAvailable());
        assertEquals(testValueItem.getComments(), all.get(0).getComments());
        assertEquals(testValueItem.getLastBooking(), all.get(0).getLastBooking());
        assertEquals(testValueItem.getNextBooking(), all.get(0).getNextBooking());
        assertEquals(testValueItem.getRequest(), all.get(0).getRequest());
        assertEquals(testValueItem.toString(), all.get(0).toString());
        assertEquals(testValueItem.hashCode(), all.get(0).hashCode());

        verify(mockItemRepository, times(1))
                .findAllByOwnerId(anyLong(), any(PageRequest.class));
        verify(mockCommentRepository, times(1))
                .findAllByItemId(testValueItem.getId());
        verify(mockBookingRepository, times(1))
                .findFirstByItemIdAndStartAfterAndStatus(anyLong(), any(Timestamp.class), any(StatusBooking.class), any(Sort.class));
        verify(mockBookingRepository, times(1))
                .findFirstByItemIdAndStartBeforeAndStatus(anyLong(), any(Timestamp.class), any(StatusBooking.class), any(Sort.class));
    }

    @Test
    void shouldUpdateItem() {
        when(mockItemRepository.findById(testValueItem.getId()))
                .thenReturn(Optional.ofNullable(testValueItem));
        when(mockItemRepository.save(testValueItem))
                .thenReturn(testValueItem);

        Item item = itemService.update(testValueItem, testValueItem.getId(), testValueItem.getOwner().getId());
        assertEquals(testValueItem.getId(), item.getId());
        assertEquals(testValueItem.getOwner(), item.getOwner());
        assertEquals(testValueItem.getName(), item.getName());
        assertEquals(testValueItem.getDescription(), item.getDescription());
        assertEquals(testValueItem.getAvailable(), item.getAvailable());
        assertEquals(testValueItem.getComments(), item.getComments());
        assertEquals(testValueItem.getLastBooking(), item.getLastBooking());
        assertEquals(testValueItem.getNextBooking(), item.getNextBooking());
        assertEquals(testValueItem.getRequest(), item.getRequest());
        assertEquals(testValueItem.toString(), item.toString());
        assertEquals(testValueItem.hashCode(), item.hashCode());

        verify(mockItemRepository, times(1)).findById(testValueItem.getId());
        verify(mockItemRepository, times(1)).save(item);
    }

    @Test
    void shouldSearchItem() {
        when(mockItemRepository.findAllByNameOrDescriptionContainingIgnoreCaseAndAvailableTrue(anyString(), anyString(), any(PageRequest.class)))
                .thenReturn(List.of(testValueItem));

        List<Item> all = itemService.search(testValueItem.getName(), 0, 10);
        assertEquals(1, all.size());
        assertEquals(testValueItem.getId(), all.get(0).getId());
        assertEquals(testValueItem.getOwner(), all.get(0).getOwner());
        assertEquals(testValueItem.getName(), all.get(0).getName());
        assertEquals(testValueItem.getDescription(), all.get(0).getDescription());
        assertEquals(testValueItem.getAvailable(), all.get(0).getAvailable());
        assertEquals(testValueItem.getComments(), all.get(0).getComments());
        assertEquals(testValueItem.getLastBooking(), all.get(0).getLastBooking());
        assertEquals(testValueItem.getNextBooking(), all.get(0).getNextBooking());
        assertEquals(testValueItem.getRequest(), all.get(0).getRequest());
        assertEquals(testValueItem.toString(), all.get(0).toString());
        assertEquals(testValueItem.hashCode(), all.get(0).hashCode());

        verify(mockItemRepository, times(1))
                .findAllByNameOrDescriptionContainingIgnoreCaseAndAvailableTrue(anyString(), anyString(), any(PageRequest.class));
    }

    @Test
    void shouldAddComment() {
        Comment testValueComment = generateComment(testValueItem);
        User user = generateUser();
        testValueComment.setAuthor(user);
        Booking booking = generateBooking();
        booking.setItem(testValueItem);

        when(mockCommentRepository.save(testValueComment))
                .thenReturn(testValueComment);
        when(mockBookingRepository.findFirstByBookerIdAndItemIdAndStatusNotLikeAndEndBefore(anyLong(), anyLong(), any(StatusBooking.class), any(Timestamp.class)))
                .thenReturn(Optional.of(booking));

        Comment comment = itemService.addComment(testValueComment, user.getId(), testValueItem.getId());
        assertEquals(testValueComment.getId(), comment.getId());
        assertEquals(testValueComment.getAuthor(), comment.getAuthor());
        assertEquals(testValueComment.getItem(), comment.getItem());
        assertEquals(testValueComment.getCreated(), comment.getCreated());
        assertEquals(testValueComment.getText(), comment.getText());
        assertEquals(testValueComment.toString(), comment.toString());

        verify(mockItemRepository, times(1)).findById(testValueItem.getId());
        verify(mockUserService, times(1)).getById(user.getId());
        verify(mockBookingRepository, times(1))
                .findFirstByBookerIdAndItemIdAndStatusNotLikeAndEndBefore(anyLong(), anyLong(), any(StatusBooking.class), any(Timestamp.class));
        verify(mockCommentRepository, times(1)).findAllByItemId(testValueItem.getId());
        verify(mockCommentRepository, times(1)).save(comment);
    }
}
