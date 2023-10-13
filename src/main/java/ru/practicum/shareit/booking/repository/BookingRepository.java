package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.StatusBooking;
import ru.practicum.shareit.booking.dto.BookingInfoDto;
import ru.practicum.shareit.booking.model.Booking;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findAllByBookerId(long bookerId, Pageable pageable);

    List<Booking> findAllByBookerIdAndStatus(long bookerId, StatusBooking status, Pageable pageable);

    List<Booking> findAllByBookerIdAndStartAfter(Long bookerId, Timestamp start, Pageable pageable);

    List<Booking> findAllByBookerIdAndStartBeforeAndEndAfter(Long bookerId, Timestamp start, Timestamp end, Pageable pageable);

    List<Booking> findAllByItemOwnerId(long ownerId, Pageable pageable);

    List<Booking> findAllByItemOwnerIdAndStatus(long ownerId, StatusBooking status, Pageable pageable);

    List<Booking> findAllByItemOwnerIdAndStartAfter(Long ownerId, Timestamp start, Pageable pageable);

    List<Booking> findAllByItemOwnerIdAndStartBeforeAndEndAfter(Long ownerId, Timestamp start, Timestamp end, Pageable pageable);

    List<Booking> findAllByBookerIdAndEndBefore(Long bookerId, Timestamp end, Pageable pageable);

    List<Booking> findAllByItemOwnerIdAndEndBefore(Long ownerId, Timestamp end, Pageable pageable);

    BookingInfoDto findFirstByItemIdAndStartAfterAndStatus(Long id, Timestamp start, StatusBooking status, Sort sort);

    BookingInfoDto findFirstByItemIdAndStartBeforeAndStatus(Long id, Timestamp end, StatusBooking status, Sort sort);

    Optional<Booking> findFirstByBookerIdAndItemIdAndStatusNotLikeAndEndBefore(Long bookerId, long itemId, StatusBooking status, Timestamp end);
}