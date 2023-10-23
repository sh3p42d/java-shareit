package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Validated
public class ItemController {
    private static final String USER_ID_HEADER = "X-Sharer-User-Id";
    private final ItemService itemService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<ItemDto> findAll(@RequestHeader(USER_ID_HEADER) final long owner,
                                 @RequestParam(defaultValue = "0") @Min(0) final int from,
                                 @RequestParam(defaultValue = "10") @Min(0) final int size) {
        return itemService.getAll(owner, from, size).stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public ItemDto getById(@PathVariable final long id,
                           @RequestHeader(USER_ID_HEADER) final long userId) {
        return ItemMapper.toItemDto(itemService.getById(id, userId));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ItemDto add(@Valid @RequestBody final ItemDto dto,
                       @RequestHeader(USER_ID_HEADER) final long userId) {
        return ItemMapper.toItemDto(itemService.add(ItemMapper.toItem(dto, userId), userId));
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/{id}")
    public ItemDto update(@RequestBody final ItemDto dto,
                          @PathVariable final long id,
                          @RequestHeader(USER_ID_HEADER) final long userId) {
        return ItemMapper.toItemDto(itemService.update(ItemMapper.toItem(dto, userId), id, userId));
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/search")
    public List<ItemDto> search(@RequestParam("text") final String text,
                                @RequestParam(defaultValue = "0") @Min(0) final int from,
                                @RequestParam(defaultValue = "10") @Min(0) final int size) {
        return itemService.search(text, from, size).stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("{itemId}/comment")
    public CommentDto addComment(
            @Valid @RequestBody final CommentDto dto,
            @RequestHeader(USER_ID_HEADER) final long author,
            @PathVariable long itemId
    ) {
        return CommentMapper.toCommentDto(itemService.addComment(CommentMapper.toComment(dto, author, itemId),
                author, itemId));
    }
}
