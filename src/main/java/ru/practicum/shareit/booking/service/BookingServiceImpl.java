package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingState;
import ru.practicum.shareit.booking.StatusBooking;
import ru.practicum.shareit.booking.error.*;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.error.ItemNotFoundByUserException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.service.UserService;

import org.springframework.transaction.annotation.Transactional;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final ItemService itemService;
    private final UserService userService;

    @Override
    public List<Booking> findAll(final long bookerId, final String state) {
        Timestamp timestampNow = Timestamp.from(Instant.now());
        Sort sort = Sort.by("start").descending();

        switch (bookingStateAndIdCheck(bookerId, state)) {
            case ALL:
                return bookingRepository.findAllByBookerId(bookerId, sort);
            case WAITING:
                return bookingRepository.findAllByBookerIdAndStatus(bookerId, StatusBooking.WAITING, sort);
            case REJECTED:
                return bookingRepository.findAllByBookerIdAndStatus(bookerId, StatusBooking.REJECTED, sort);
            case FUTURE:
                return bookingRepository.findAllByBookerIdAndStartAfter(bookerId, timestampNow, sort);
            case CURRENT:
                return bookingRepository.findAllByBookerIdAndStartBeforeAndEndAfter(bookerId, timestampNow, timestampNow, sort);
            case PAST:
                return bookingRepository.findAllByBookerIdAndEndBefore(bookerId, timestampNow, sort);
            default:
                throw new BookingWrongStateException(state);
        }
    }

    @Override
    public List<Booking> findAllOwner(long ownerId, String state) {
        Timestamp timestampNow = Timestamp.from(Instant.now());
        Sort sort = Sort.by("start").descending();

        switch (bookingStateAndIdCheck(ownerId, state)) {
            case ALL:
                return bookingRepository.findAllByItemOwnerId(ownerId, sort);
            case WAITING:
                return bookingRepository.findAllByItemOwnerIdAndStatus(ownerId, StatusBooking.WAITING, sort);
            case REJECTED:
                return bookingRepository.findAllByItemOwnerIdAndStatus(ownerId, StatusBooking.REJECTED, sort);
            case FUTURE:
                return bookingRepository.findAllByItemOwnerIdAndStartAfter(ownerId, timestampNow, sort);
            case CURRENT:
                return bookingRepository.findAllByItemOwnerIdAndStartBeforeAndEndAfter(ownerId, timestampNow, timestampNow, sort);
            case PAST:
                return bookingRepository.findAllByItemOwnerIdAndEndBefore(ownerId, timestampNow, sort);
            default:
                throw new BookingWrongStateException(state);
        }
    }

    @Override
    public Booking getById(final long id, final long userId) {
        return bookingRepository.findById(id)
                .filter(b -> b.getBooker().getId() == userId || b.getItem().getOwner().getId() == userId)
                .orElseThrow(() -> new BookingNotFoundException(id));
    }

    @Override
    @Transactional
    public Booking add(final Booking booking, final long bookerId) {
        Optional<Item> item = Optional.of(itemService.getById(booking.getItem().getId(), bookerId));
        item.filter(Item::getAvailable)
                .orElseThrow(() -> new ItemNotAllowedException(booking.getItem().getId()));

        item.filter(i -> !Objects.equals(i.getOwner().getId(), booking.getBooker().getId()))
                .orElseThrow(() -> new BookingNotAllowedException(booking.getItem().getId()));

        booking.setItem(itemService.getById(booking.getItem().getId(), bookerId));
        booking.setBooker(userService.getById(booking.getBooker().getId()));
        return bookingRepository.save(booking);
    }

    @Override
    @Transactional
    public Booking approved(final long id, final long owner, final boolean approved) {
        Booking booking = bookingRepository.findById(id)
                .filter(b -> b.getItem().getOwner().getId() == owner)
                .orElseThrow(() -> new ItemNotFoundByUserException(id, owner));
        if (booking.getStatus() != StatusBooking.APPROVED) {
            booking.setStatus(approved ? StatusBooking.APPROVED : StatusBooking.REJECTED);
        } else {
            throw new BookingStateNotAvailable(id);
        }
        return bookingRepository.save(booking);
    }

    private BookingState bookingStateAndIdCheck(long userId, String state) {
        BookingState bookingState = BookingState.UNSUPPORTED_STATUS;
        userService.getById(userId);
        if (Arrays.stream(BookingState.values()).anyMatch(bs -> bs.name().equals(state))) {
            bookingState = BookingState.valueOf(state);
        }
        return bookingState;
    }
}