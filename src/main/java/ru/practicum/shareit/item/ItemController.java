package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private static final String USER_ID_HEADER = "X-Sharer-User-Id";
    private final ItemService itemService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<ItemDto> findAll(@RequestHeader(USER_ID_HEADER) final long owner) {
        return itemService.getAll(owner).stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public Optional<ItemDto> getById(@PathVariable final long id) {
        return itemService.getById(id).map(ItemMapper::toItemDto);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Optional<ItemDto> add(@Valid @RequestBody final ItemDto dto,
                                    @RequestHeader(USER_ID_HEADER) final long userId) {
        return itemService.add(ItemMapper.toItem(dto), userId).map(ItemMapper::toItemDto);
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/{id}")
    public Optional<ItemDto> update(@RequestBody final ItemDto dto,
                                    @PathVariable final long id,
                                    @RequestHeader(USER_ID_HEADER) final long userId) {
        return itemService.update(ItemMapper.toItem(dto), id, userId).map(ItemMapper::toItemDto);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/search")
    public List<ItemDto> search(@RequestParam("text") final String text) {
        return itemService.search(text).stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
    }
}
