package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.StatusBooking;
import ru.practicum.shareit.booking.dto.BookingInfoDto;
import ru.practicum.shareit.booking.model.Booking;

import java.sql.Timestamp;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    Page<Booking> findAllByBookerId(long bookerId, Pageable pageable);

    Page<Booking> findAllByBookerIdAndStatus(long bookerId, StatusBooking status, Pageable pageable);

    Page<Booking> findAllByBookerIdAndStartAfter(Long bookerId, Timestamp start, Pageable pageable);

    Page<Booking> findAllByBookerIdAndStartBeforeAndEndAfter(Long bookerId, Timestamp start, Timestamp end, Pageable pageable);

    Page<Booking> findAllByItemOwnerId(long ownerId, Pageable pageable);

    Page<Booking> findAllByItemOwnerIdAndStatus(long ownerId, StatusBooking status, Pageable pageable);

    Page<Booking> findAllByItemOwnerIdAndStartAfter(Long ownerId, Timestamp start, Pageable pageable);

    Page<Booking> findAllByItemOwnerIdAndStartBeforeAndEndAfter(Long ownerId, Timestamp start, Timestamp end, Pageable pageable);

    Page<Booking> findAllByBookerIdAndEndBefore(Long bookerId, Timestamp end, Pageable pageable);

    Page<Booking> findAllByItemOwnerIdAndEndBefore(Long ownerId, Timestamp end, Pageable pageable);

    BookingInfoDto findFirstByItemIdAndStartAfterAndStatus(Long id, Timestamp start, StatusBooking status, Sort sort);

    BookingInfoDto findFirstByItemIdAndStartBeforeAndStatus(Long id, Timestamp end, StatusBooking status, Sort sort);

    Optional<Booking> findFirstByBookerIdAndItemIdAndStatusNotLikeAndEndBefore(Long bookerId, long itemId, StatusBooking status, Timestamp end);
}