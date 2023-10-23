package ru.practicum.shareit.booking;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.error.*;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.helpers.GeneratorConverterHelper;
import ru.practicum.shareit.item.error.ItemNotFoundByUserException;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.persistence.EntityNotFoundException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class BookingServiceTest extends GeneratorConverterHelper {

    @Autowired
    private BookingService bookingService;

    @MockBean
    private BookingRepository mockBookingRepository;
    @MockBean
    private ItemService mockItemService;
    @MockBean
    private UserService mockUserService;

    private Booking testValueBooking;

    @BeforeEach
    void beforeEach() {
        testValueBooking = generateBooking();

        reset(mockBookingRepository, mockUserService, mockItemService);
        when(mockBookingRepository.findById(testValueBooking.getId())).thenReturn(Optional.ofNullable(testValueBooking));
    }

    @AfterEach
    void mockVerify() {
        verifyNoMoreInteractions(mockBookingRepository, mockUserService, mockItemService);
    }

    @Test
    void shouldAddBooking() {
        when(mockItemService.getById(testValueBooking.getItem().getId(), testValueBooking.getBooker().getId()))
                .thenReturn(testValueBooking.getItem());
        when(mockUserService.getById(testValueBooking.getBooker().getId()))
                .thenReturn(testValueBooking.getBooker());
        when(mockBookingRepository.save(testValueBooking))
                .thenReturn(testValueBooking);

        Booking booking = bookingService.add(testValueBooking, testValueBooking.getBooker().getId());
        assertEquals(testValueBooking.getId(), booking.getId());
        assertEquals(testValueBooking.getBooker().getId(), booking.getBooker().getId());
        assertEquals(testValueBooking.getItem(), booking.getItem());
        assertEquals(testValueBooking.getStart(), booking.getStart());
        assertEquals(testValueBooking.getEnd(), booking.getEnd());
        assertEquals(testValueBooking.getStatus(), booking.getStatus());
        assertEquals(testValueBooking.toString(), booking.toString());
        assertEquals(testValueBooking.hashCode(), booking.hashCode());

        verify(mockItemService, times(2))
                .getById(testValueBooking.getItem().getId(), testValueBooking.getBooker().getId());
        verify(mockUserService, times(1))
                .getById(testValueBooking.getBooker().getId());
        verify(mockBookingRepository, times(1))
                .save(booking);
    }

    @Test
    void shouldGetBookingById() {
        when(mockBookingRepository.findById(testValueBooking.getId())).thenReturn(Optional.ofNullable(testValueBooking));

        Booking booking = bookingService.getById(testValueBooking.getId(), testValueBooking.getBooker().getId());
        assertEquals(testValueBooking.getId(), booking.getId());
        assertEquals(testValueBooking.getBooker().getId(), booking.getBooker().getId());
        assertEquals(testValueBooking.getItem(), booking.getItem());
        assertEquals(testValueBooking.getStart(), booking.getStart());
        assertEquals(testValueBooking.getEnd(), booking.getEnd());
        assertEquals(testValueBooking.getStatus(), booking.getStatus());
        assertEquals(testValueBooking.toString(), booking.toString());
        assertEquals(testValueBooking.hashCode(), booking.hashCode());

        verify(mockBookingRepository, times(1))
                .findById(testValueBooking.getId());
    }

    @Test
    void shouldGetAllBookingByBookerId() {
        Long bookerId = testValueBooking.getBooker().getId();

        when(mockBookingRepository
                .findAllByBookerId(anyLong(), any(PageRequest.class)))
                .thenReturn(new PageImpl<>(List.of(testValueBooking)));

        List<BookingDto> all = bookingService.findAll(bookerId, "ALL", 0, 10);
        assertEquals(testValueBooking.getId(), all.get(0).getId());
        assertEquals(testValueBooking.getBooker().getId(), all.get(0).getBooker().getId());
        assertEquals(testValueBooking.getItem().getId(), all.get(0).getItem().getId());
        assertEquals(testValueBooking.getStart().toLocalDateTime(), all.get(0).getStart());
        assertEquals(testValueBooking.getEnd().toLocalDateTime(), all.get(0).getEnd());
        assertEquals(testValueBooking.getStatus(), all.get(0).getStatus());

        when(mockBookingRepository
                .findAllByBookerIdAndStatus(anyLong(), any(StatusBooking.class), any(PageRequest.class)))
                .thenReturn(new PageImpl<>(List.of(testValueBooking)));

        List<BookingDto> waiting = bookingService.findAll(bookerId, "WAITING", 0, 10);
        assertEquals(waiting.size(), 1);

        when(mockBookingRepository
                .findAllByBookerIdAndStatus(anyLong(), any(StatusBooking.class), any(PageRequest.class)))
                .thenReturn(new PageImpl<>(List.of(testValueBooking)));

        List<BookingDto> rejected = bookingService.findAll(bookerId, "REJECTED", 0, 10);
        assertEquals(rejected.size(), 1);

        when(mockBookingRepository
                .findAllByBookerIdAndStartAfter(anyLong(), any(Timestamp.class), any(PageRequest.class)))
                .thenReturn(new PageImpl<>(List.of(testValueBooking)));

        List<BookingDto> future = bookingService.findAll(bookerId, "FUTURE", 0, 10);
        assertEquals(future.size(), 1);


        when(mockBookingRepository
                .findAllByBookerIdAndStartBeforeAndEndAfter(anyLong(), any(Timestamp.class), any(Timestamp.class), any(PageRequest.class)))
                .thenReturn(new PageImpl<>(List.of(testValueBooking)));

        List<BookingDto> current = bookingService.findAll(bookerId, "CURRENT", 0, 10);
        assertEquals(current.size(), 1);

        when(mockBookingRepository
                .findAllByBookerIdAndEndBefore(anyLong(), any(Timestamp.class), any(PageRequest.class)))
                .thenReturn(new PageImpl<>(List.of(testValueBooking)));

        List<BookingDto> past = bookingService.findAll(bookerId, "PAST", 0, 10);
        assertEquals(past.size(), 1);

        verify(mockUserService, times(6))
                .getById(testValueBooking.getBooker().getId());
        verify(mockBookingRepository, times(1))
                .findAllByBookerId(anyLong(), any(PageRequest.class));
        verify(mockBookingRepository, times(2))
                .findAllByBookerIdAndStatus(anyLong(), any(StatusBooking.class), any(PageRequest.class));
        verify(mockBookingRepository, times(1))
                .findAllByBookerIdAndStartAfter(anyLong(), any(Timestamp.class), any(PageRequest.class));
        verify(mockBookingRepository, times(1))
                .findAllByBookerIdAndStartBeforeAndEndAfter(anyLong(), any(Timestamp.class), any(Timestamp.class), any(PageRequest.class));
        verify(mockBookingRepository, times(1))
                .findAllByBookerIdAndEndBefore(anyLong(), any(Timestamp.class), any(PageRequest.class));
    }

    @Test
    void shouldGetAllBookingByOwnerId() {
        Long ownerId = testValueBooking.getItem().getOwner().getId();

        when(mockBookingRepository
                .findAllByItemOwnerId(anyLong(), any(PageRequest.class)))
                .thenReturn(new PageImpl<>(List.of(testValueBooking)));

        List<BookingDto> all = bookingService.findAllOwner(ownerId, "ALL", 0, 10);
        assertEquals(testValueBooking.getId(), all.get(0).getId());
        assertEquals(testValueBooking.getBooker().getId(), all.get(0).getBooker().getId());
        assertEquals(testValueBooking.getItem().getId(), all.get(0).getItem().getId());
        assertEquals(testValueBooking.getStart().toLocalDateTime(), all.get(0).getStart());
        assertEquals(testValueBooking.getEnd().toLocalDateTime(), all.get(0).getEnd());
        assertEquals(testValueBooking.getStatus(), all.get(0).getStatus());

        when(mockBookingRepository
                .findAllByItemOwnerIdAndStatus(anyLong(), any(StatusBooking.class), any(PageRequest.class)))
                .thenReturn(new PageImpl<>(List.of(testValueBooking)));

        List<BookingDto> waiting = bookingService.findAllOwner(ownerId, "WAITING", 0, 10);
        assertEquals(waiting.size(), 1);

        when(mockBookingRepository
                .findAllByItemOwnerIdAndStatus(anyLong(), any(StatusBooking.class), any(PageRequest.class)))
                .thenReturn(new PageImpl<>(List.of(testValueBooking)));

        List<BookingDto> rejected = bookingService.findAllOwner(ownerId, "REJECTED", 0, 10);
        assertEquals(rejected.size(), 1);

        when(mockBookingRepository
                .findAllByItemOwnerIdAndStartAfter(anyLong(), any(Timestamp.class), any(PageRequest.class)))
                .thenReturn(new PageImpl<>(List.of(testValueBooking)));

        List<BookingDto> future = bookingService.findAllOwner(ownerId, "FUTURE", 0, 10);
        assertEquals(future.size(), 1);


        when(mockBookingRepository
                .findAllByItemOwnerIdAndStartBeforeAndEndAfter(anyLong(), any(Timestamp.class), any(Timestamp.class), any(PageRequest.class)))
                .thenReturn(new PageImpl<>(List.of(testValueBooking)));

        List<BookingDto> current = bookingService.findAllOwner(ownerId, "CURRENT", 0, 10);
        assertEquals(current.size(), 1);

        when(mockBookingRepository
                .findAllByItemOwnerIdAndEndBefore(anyLong(), any(Timestamp.class), any(PageRequest.class)))
                .thenReturn(new PageImpl<>(List.of(testValueBooking)));

        List<BookingDto> past = bookingService.findAllOwner(ownerId, "PAST", 0, 10);
        assertEquals(past.size(), 1);

        verify(mockUserService, times(6)).getById(ownerId);
        verify(mockBookingRepository, times(1))
                .findAllByItemOwnerId(anyLong(), any(PageRequest.class));
        verify(mockBookingRepository, times(2))
                .findAllByItemOwnerIdAndStatus(anyLong(), any(StatusBooking.class), any(PageRequest.class));
        verify(mockBookingRepository, times(1))
                .findAllByItemOwnerIdAndStartAfter(anyLong(), any(Timestamp.class), any(PageRequest.class));
        verify(mockBookingRepository, times(1))
                .findAllByItemOwnerIdAndStartBeforeAndEndAfter(anyLong(), any(Timestamp.class), any(Timestamp.class), any(PageRequest.class));
        verify(mockBookingRepository, times(1))
                .findAllByItemOwnerIdAndEndBefore(anyLong(), any(Timestamp.class), any(PageRequest.class));
    }

    @Test
    void shouldApprove() {
        when(mockBookingRepository.save(testValueBooking)).thenReturn(testValueBooking);

        Booking booking = bookingService.approved(testValueBooking.getId(), testValueBooking.getItem().getOwner().getId(), true);
        assertEquals(testValueBooking.getId(), booking.getId());
        assertEquals(testValueBooking.getBooker().getId(), booking.getBooker().getId());
        assertEquals(testValueBooking.getItem(), booking.getItem());
        assertEquals(testValueBooking.getStart(), booking.getStart());
        assertEquals(testValueBooking.getEnd(), booking.getEnd());
        assertEquals(testValueBooking.getStatus(), booking.getStatus());
        assertEquals(testValueBooking.toString(), booking.toString());
        assertEquals(testValueBooking.hashCode(), booking.hashCode());

        verify(mockBookingRepository, times(1)).findById(testValueBooking.getId());
        verify(mockBookingRepository, times(1)).save(testValueBooking);
    }

    @Test
    void shouldBookingNotFoundException() {
        final EntityNotFoundException bookingNotFoundException = assertThrows(
                BookingNotFoundException.class,
                () -> bookingService.getById(-999L, -999L));

        assertEquals(String.format("Booking с id=%s не найден", -999L),
                bookingNotFoundException.getMessage());

        verify(mockBookingRepository, times(1)).findById(anyLong());
    }

    @Test
    void shouldItemNotAllowedException() {
        testValueBooking.getItem().setAvailable(false);

        when(mockItemService.getById(testValueBooking.getItem().getId(), testValueBooking.getBooker().getId())).thenReturn(testValueBooking.getItem());

        final RuntimeException itemNotAllowedException = assertThrows(
                ItemNotAllowedException.class,
                () -> bookingService.add(testValueBooking, testValueBooking.getBooker().getId()));

        assertEquals(String.format("Item с id=%s недоступен для бронирования.",
                testValueBooking.getItem().getId()), itemNotAllowedException.getMessage());

        verify(mockItemService, times(1)).getById(anyLong(), anyLong());
    }

    @Test
    void shouldBookingNotAllowedException() {
        testValueBooking.setBooker(testValueBooking.getItem().getOwner());
        when(mockItemService.getById(anyLong(), anyLong())).thenReturn(testValueBooking.getItem());

        final RuntimeException bookingNotAllowedException = assertThrows(
                BookingNotAllowedException.class,
                () -> bookingService.add(testValueBooking, testValueBooking.getItem().getOwner().getId()));

        assertEquals(String.format("Booking для Item с id=%s недоступен для этого User",
                testValueBooking.getItem().getId()), bookingNotAllowedException.getMessage());

        verify(mockItemService, times(1)).getById(anyLong(), anyLong());
    }

    @Test
    void shouldBookingWrongStateException() {
        final RuntimeException bookingWrongStateException = assertThrows(
                BookingWrongStateException.class,
                () -> bookingService.findAll(testValueBooking.getBooker().getId(), "UNSUPPORTED_STATUS", 0, 10));

        assertEquals("UNSUPPORTED_STATUS", bookingWrongStateException.getMessage(), "Неизвестный статус.");

        verify(mockUserService, times(1)).getById(anyLong());
    }

    @Test
    void shouldItemNotFoundByUserException() {
        User userWithoutItems = generateUser();

        final RuntimeException itemNotFoundByUserException = assertThrows(
                ItemNotFoundByUserException.class,
                () -> bookingService.approved(testValueBooking.getId(), userWithoutItems.getId(), true));

        assertEquals(String.format("Item с id=%s у User с id=%s не найден", testValueBooking.getId(), userWithoutItems.getId()),
                itemNotFoundByUserException.getMessage());

        verify(mockBookingRepository, times(1)).findById(testValueBooking.getId());
    }

    @Test
    void shouldBookingStateNotAvailable() {
        testValueBooking.setStatus(StatusBooking.APPROVED);

        final RuntimeException bookingStateNotAvailable = assertThrows(
                BookingStateNotAvailable.class,
                () -> bookingService.approved(testValueBooking.getId(), testValueBooking.getItem().getOwner().getId(), true));

        assertEquals((String.format("Статус Booking с id=%s уже подтвержден.", testValueBooking.getId())), bookingStateNotAvailable.getMessage());

        verify(mockBookingRepository, times(1)).findById(testValueBooking.getId());
    }
}
