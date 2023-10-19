package ru.practicum.shareit.booking;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.booking.dto.BookingInfoDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.helpers.GeneratorConverterHelper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class BookingRepositoryTest extends GeneratorConverterHelper {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    private Item item;
    private User booker;
    private User itemOwner;
    private Booking booking;
    private LocalDateTime timeNow;

    private final StatusBooking statusBooking = StatusBooking.APPROVED;

    @BeforeEach
    public void beforeEach() {
        itemOwner = userRepository.save(generateUserForRepository());
        booker = userRepository.save(generateUserForRepository());
        item = itemRepository.save(generateItemForRepository(itemOwner));
        booking = bookingRepository.save(generateBookingForRepository(booker, item));
        timeNow = LocalDateTime.now();
    }

    @AfterEach
    public void afterEach() {
        userRepository.deleteAll();
        itemRepository.deleteAll();
        bookingRepository.deleteAll();
    }

    @Test
    public void shouldFindAllByBookerId() {
        Page<Booking> result = bookingRepository
                .findAllByBookerId(booker.getId(), Pageable.unpaged());

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(booking, result.getContent().get(0));
    }

    @Test
    public void shouldFindAllByBookerIdAndStatus() {
        Page<Booking> result = bookingRepository
                .findAllByBookerIdAndStatus(booker.getId(), statusBooking, Pageable.unpaged());

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(booking, result.getContent().get(0));
    }

    @Test
    public void shouldFindAllByBookerIdAndStartAfter() {
        Page<Booking> result = bookingRepository
                .findAllByBookerIdAndStartAfter(booker.getId(), Timestamp.valueOf(timeNow.minusDays(1)), Pageable.unpaged());

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(booking, result.getContent().get(0));
    }

    @Test
    public void shouldFindAllByBookerIdAndStartBeforeAndEndAfter() {
        Page<Booking> result = bookingRepository
                .findAllByBookerIdAndStartBeforeAndEndAfter(booker.getId(), Timestamp.valueOf(timeNow.plusDays(5)),
                        Timestamp.valueOf(timeNow.minusDays(5)), Pageable.unpaged());

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(booking, result.getContent().get(0));
    }

    @Test
    public void shouldFindAllByItemOwnerId() {
        Page<Booking> result = bookingRepository
                .findAllByItemOwnerId(itemOwner.getId(), Pageable.unpaged());

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(booking, result.getContent().get(0));
    }

    @Test
    public void shouldFindAllByItemOwnerIdAndStatus() {
        Page<Booking> result = bookingRepository
                .findAllByItemOwnerIdAndStatus(itemOwner.getId(), statusBooking, Pageable.unpaged());

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(booking, result.getContent().get(0));
    }

    @Test
    public void shouldFindAllByItemOwnerIdAndStartAfter() {
        Page<Booking> result = bookingRepository
                .findAllByItemOwnerIdAndStartAfter(itemOwner.getId(), Timestamp.valueOf(timeNow.minusDays(1)), Pageable.unpaged());

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(booking, result.getContent().get(0));
    }

    @Test
    public void shouldFindAllByItemOwnerIdAndStartBeforeAndEndAfter() {
        Page<Booking> result = bookingRepository
                .findAllByItemOwnerIdAndStartBeforeAndEndAfter(itemOwner.getId(), Timestamp.valueOf(timeNow.plusDays(5)),
                        Timestamp.valueOf(timeNow.minusDays(5)), Pageable.unpaged());

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(booking, result.getContent().get(0));
    }

    @Test
    public void shouldFindAllByBookerIdAndEndBefore() {
        Page<Booking> result = bookingRepository
                .findAllByBookerIdAndEndBefore(booker.getId(), Timestamp.valueOf(timeNow.plusDays(10)), Pageable.unpaged());

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(booking, result.getContent().get(0));
    }

    @Test
    public void shouldFindAllByItemOwnerIdAndEndBefore() {
        Page<Booking> result = bookingRepository
                .findAllByItemOwnerIdAndEndBefore(itemOwner.getId(), Timestamp.valueOf(timeNow.plusDays(10)), Pageable.unpaged());

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(booking, result.getContent().get(0));
    }

    @Test
    public void shouldFindFirstByItemIdAndStartAfterAndStatus() {
        BookingInfoDto result = bookingRepository
                .findFirstByItemIdAndStartAfterAndStatus(item.getId(), Timestamp.valueOf(timeNow.minusDays(1)),
                        statusBooking, Sort.unsorted());

        assertNotNull(result);
        assertEquals(booking.getId(), result.getId());
        assertEquals(booking.getBooker(), result.getBooker());
        assertEquals(booking.getStatus(), result.getStatus());
    }

    @Test
    void shouldFindFirstByItemIdAndStartBeforeAndStatus() {
        BookingInfoDto result = bookingRepository
                .findFirstByItemIdAndStartBeforeAndStatus(item.getId(), Timestamp.valueOf(timeNow.plusDays(5)),
                        statusBooking, Sort.unsorted());

        assertNotNull(result);
        assertEquals(booking.getId(), result.getId());
        assertEquals(booking.getBooker(), result.getBooker());
        assertEquals(booking.getStatus(), result.getStatus());
    }

    @Test
    void shouldFindFirstByBookerIdAndItemIdAndStatusNotLikeAndEndBefore() {
        Optional<Booking> result = bookingRepository
                .findFirstByBookerIdAndItemIdAndStatusNotLikeAndEndBefore(booker.getId(), item.getId(),
                        StatusBooking.REJECTED, Timestamp.valueOf(timeNow.plusDays(10)));

        assertNotNull(result);
        assertTrue(result.isPresent());
        assertEquals(booking.getId(), result.get().getId());
        assertEquals(booking.getBooker(), result.get().getBooker());
        assertEquals(booking.getStatus(), result.get().getStatus());
    }
}
