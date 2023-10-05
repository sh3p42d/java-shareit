package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {

    private static final String USER_ID_HEADER = "X-Sharer-User-Id";
    private final BookingService bookingService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<BookingDto> findAll(@RequestHeader(USER_ID_HEADER) final long bookerId,
                                    @RequestParam(defaultValue = "ALL") final String state) {

        return bookingService.findAll(bookerId, state).stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/owner")
    public List<BookingDto> findAllOwner(@RequestHeader(USER_ID_HEADER) final long bookerId,
                                         @RequestParam(defaultValue = "ALL") final String state) {

        return bookingService.findAllOwner(bookerId, state).stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("{id}")
    public BookingDto findById(@PathVariable final long id,
                               @RequestHeader(USER_ID_HEADER) final long userId) {

        return BookingMapper.toBookingDto(bookingService.getById(id, userId));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public BookingDto create(
            @Valid @RequestBody final BookingResponseDto dto,
            @RequestHeader(USER_ID_HEADER) final long bookerId) {

        return BookingMapper.toBookingDto(bookingService.add(BookingMapper.toBooking(dto, bookerId), bookerId));
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("{id}")
    public BookingDto update(@PathVariable final long id,
                             @RequestHeader(USER_ID_HEADER) final long owner,
                             @RequestParam("approved") final boolean approved) {

        return BookingMapper.toBookingDto(bookingService.approved(id, owner, approved));
    }
}
