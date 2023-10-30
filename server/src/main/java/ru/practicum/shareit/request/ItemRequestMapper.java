package ru.practicum.shareit.request;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.dto.ItemRequestInfoDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.stream.Collectors;

@UtilityClass
public class ItemRequestMapper {
    public static ItemRequestDto toItemRequestDto(ItemRequest itemRequest) {
        return ItemRequestDto.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .created(itemRequest.getCreated().toLocalDateTime())
                .items(itemRequest.getItems().stream()
                        .map(req -> ItemRequestInfoDto.builder()
                                .id(req.getId())
                                .name(req.getName())
                                .description(req.getDescription())
                                .available(req.getAvailable())
                                .requestId(req.getRequest().getId())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

    public static ItemRequest toItemRequest(ItemRequestDto itemRequestDto, long authorId) {
        return ItemRequest.builder()
                .id(itemRequestDto.getId())
                .author(User.builder()
                        .id(authorId)
                        .build())
                .description(itemRequestDto.getDescription())
                .created(Timestamp.from(Instant.now()))
                .items(new ArrayList<>())
                .build();
    }
}