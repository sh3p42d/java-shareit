package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingState;
import ru.practicum.shareit.booking.StatusBooking;
import ru.practicum.shareit.booking.dto.BookingDto;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final ItemService itemService;
    private final UserService userService;

    @Override
    public List<BookingDto> findAll(final long bookerId, final String state, final int from, final int size) {
        Timestamp timestampNow = Timestamp.from(Instant.now());
        Sort sort = Sort.by("start").descending();
        PageRequest page = PageRequest.of(from / size, size, sort);
        Page<Booking> bookingPage;

        switch (bookingStateAndIdCheck(bookerId, state)) {
            case ALL:
                bookingPage = bookingRepository.findAllByBookerId(bookerId, page);
                break;
            case WAITING:
                bookingPage = bookingRepository.findAllByBookerIdAndStatus(bookerId, StatusBooking.WAITING, page);
                break;
            case REJECTED:
                bookingPage = bookingRepository.findAllByBookerIdAndStatus(bookerId, StatusBooking.REJECTED, page);
                break;
            case FUTURE:
                bookingPage = bookingRepository.findAllByBookerIdAndStartAfter(bookerId, timestampNow, page);
                break;
            case CURRENT:
                bookingPage = bookingRepository.findAllByBookerIdAndStartBeforeAndEndAfter(bookerId, timestampNow, timestampNow, page);
                break;
            case PAST:
                bookingPage = bookingRepository.findAllByBookerIdAndEndBefore(bookerId, timestampNow, page);
                break;
            default:
                throw new BookingWrongStateException(state);
        }
        return bookingPage.getContent().stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingDto> findAllOwner(long ownerId, String state, final int from, final int size) {
        Timestamp timestampNow = Timestamp.from(Instant.now());
        Sort sort = Sort.by("start").descending();
        PageRequest page = PageRequest.of(from / size, size, sort);
        Page<Booking> bookingPage;

        switch (bookingStateAndIdCheck(ownerId, state)) {
            case ALL:
                bookingPage =  bookingRepository.findAllByItemOwnerId(ownerId, page);
                break;
            case WAITING:
                bookingPage =  bookingRepository.findAllByItemOwnerIdAndStatus(ownerId, StatusBooking.WAITING, page);
                break;
            case REJECTED:
                bookingPage =  bookingRepository.findAllByItemOwnerIdAndStatus(ownerId, StatusBooking.REJECTED, page);
                break;
            case FUTURE:
                bookingPage =  bookingRepository.findAllByItemOwnerIdAndStartAfter(ownerId, timestampNow, page);
                break;
            case CURRENT:
                bookingPage =  bookingRepository.findAllByItemOwnerIdAndStartBeforeAndEndAfter(ownerId, timestampNow, timestampNow, page);
                break;
            case PAST:
                bookingPage =  bookingRepository.findAllByItemOwnerIdAndEndBefore(ownerId, timestampNow, page);
                break;
            default:
                throw new BookingWrongStateException(state);
        }
        return bookingPage.getContent().stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());
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