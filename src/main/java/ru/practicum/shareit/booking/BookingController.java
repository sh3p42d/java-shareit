package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Validated
public class BookingController {

    private static final String USER_ID_HEADER = "X-Sharer-User-Id";
    private final BookingService bookingService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<BookingDto> findAll(@RequestHeader(USER_ID_HEADER) final long bookerId,
                                    @RequestParam(defaultValue = "ALL") final String state,
                                    @RequestParam(defaultValue = "0") @Min(0) final int from,
                                    @RequestParam(defaultValue = "10") @Min(0) final int size) {

        return bookingService.findAll(bookerId, state, from, size);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/owner")
    public List<BookingDto> findAllOwner(@RequestHeader(USER_ID_HEADER) final long bookerId,
                                         @RequestParam(defaultValue = "ALL") final String state,
                                         @RequestParam(defaultValue = "0") @Min(0) final int from,
                                         @RequestParam(defaultValue = "10") @Min(0) final int size) {

        return bookingService.findAllOwner(bookerId, state, from, size);
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
