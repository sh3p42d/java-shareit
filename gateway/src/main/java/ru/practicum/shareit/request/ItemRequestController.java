package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.config.validator.Create;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import javax.validation.constraints.Min;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Validated
public class ItemRequestController {
    private static final String USER_ID_HEADER = "X-Sharer-User-Id";
    private final ItemRequestClient itemRequestClient;

    @ResponseStatus(HttpStatus.OK)
    @PostMapping
    public ResponseEntity<Object> create(@Validated({Create.class}) @RequestBody final ItemRequestDto dto,
                                         @RequestHeader(USER_ID_HEADER) final long authorId) {
        return itemRequestClient.create(dto, authorId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("{id}")
    public ResponseEntity<Object> findById(@PathVariable final long id,
                                           @RequestHeader(USER_ID_HEADER) final long authorId) {
        return itemRequestClient.findById(id, authorId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public ResponseEntity<Object> findAll(@RequestHeader(USER_ID_HEADER) final long userId) {
        return itemRequestClient.findAll(userId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("all")
    public ResponseEntity<Object> findAllPageable(@RequestParam(defaultValue = "0") @Min(0) final int from,
                                                  @RequestParam(defaultValue = "10") @PositiveOrZero final int size,
                                                  @RequestHeader(USER_ID_HEADER) final long userId) {
        return itemRequestClient.findAllPageable(from, size, userId);
    }
}