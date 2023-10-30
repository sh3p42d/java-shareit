package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.config.validator.Create;
import ru.practicum.shareit.config.validator.Update;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.constraints.Min;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Validated
public class ItemController {
    private static final String USER_ID_HEADER = "X-Sharer-User-Id";
    private final ItemClient itemClient;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ResponseEntity<Object> create(@Validated({Create.class}) @RequestBody final ItemDto dto,
                                         @RequestHeader(USER_ID_HEADER) final long userId) {
        return itemClient.create(dto, userId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("{id}")
    public ResponseEntity<Object> findById(@PathVariable final long id,
                                           @RequestHeader(USER_ID_HEADER) final long userId) {
        return itemClient.findById(id, userId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public ResponseEntity<Object> findAll(@RequestParam(defaultValue = "0") @Min(0) final int from,
                                          @RequestParam(defaultValue = "10") @PositiveOrZero final int size,
                                          @RequestHeader(USER_ID_HEADER) final long userId) {
        return itemClient.findAll(from, size, userId);
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("{id}")
    public ResponseEntity<Object> update(@Validated({Update.class}) @RequestBody final ItemDto dto,
                                         @PathVariable final long id,
                                         @RequestHeader(USER_ID_HEADER) final long userId) {
        return itemClient.update(id, dto, userId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/search")
    public ResponseEntity<Object> search(@RequestParam(defaultValue = "0") @Min(0) final int from,
                                         @RequestParam(defaultValue = "10") @PositiveOrZero final int size,
                                         @RequestParam("text") final String text,
                                         @RequestHeader(USER_ID_HEADER) final long userId) {
        return itemClient.search(text, from, size, userId);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("{itemId}/comment")
    public ResponseEntity<Object> addComment(@Validated({Create.class}) @RequestBody final CommentDto dto,
                                             @RequestHeader(USER_ID_HEADER) final long author,
                                             @PathVariable long itemId) {
        return itemClient.addComment(dto, author, itemId);
    }
}