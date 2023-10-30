package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.config.validator.Create;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Validated
public class BookingController {

    private static final String USER_ID_HEADER = "X-Sharer-User-Id";
    private final BookingClient bookingClient;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ResponseEntity<Object> create(@Validated(Create.class) @Valid @RequestBody final BookingResponseDto dto,
                                         @RequestHeader(USER_ID_HEADER) final long bookerId) {
        return bookingClient.create(dto, bookerId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("{id}")
    public ResponseEntity<Object> findById(@PathVariable final long id,
                                           @RequestHeader(USER_ID_HEADER) final long userId) {
        return bookingClient.findById(id, userId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public ResponseEntity<Object> findAll(@RequestParam(defaultValue = "0") @Min(0) final int from,
                                          @RequestParam(defaultValue = "10") @PositiveOrZero final int size,
                                          @RequestHeader(USER_ID_HEADER) final long bookerId,
                                          @RequestParam(value = "state", defaultValue = "ALL") final String state) {
        return bookingClient.findAll(from, size, bookerId, state);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/owner")
    public ResponseEntity<Object> findAllOwner(@RequestParam(defaultValue = "0") @Min(0) final int from,
                                               @RequestParam(defaultValue = "10") @PositiveOrZero final int size,
                                               @RequestHeader(USER_ID_HEADER) final long bookerId,
                                               @RequestParam(value = "state", defaultValue = "ALL") final String state) {
        return bookingClient.findAllOwner(from, size, bookerId, state);
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("{id}")
    public ResponseEntity<Object> update(@PathVariable final long id,
                                         @RequestHeader(USER_ID_HEADER) final long owner,
                                         @RequestParam("approved") final boolean approved) {
        return bookingClient.update(id, owner, approved);
    }
}