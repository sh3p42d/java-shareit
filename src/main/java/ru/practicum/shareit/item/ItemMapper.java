package ru.practicum.shareit.item;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.dto.ItemBookingInfoDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.stream.Collectors;

@UtilityClass
public class ItemMapper {
    public static ItemDto toItemDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .owner(item.getOwner().getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .lastBooking(item.getLastBooking() != null ?
                        ItemBookingInfoDto.builder()
                                .id(item.getLastBooking().getId())
                                .bookerId(item.getLastBooking().getBooker().getId())
                                .build() : null)
                .nextBooking(item.getNextBooking() != null ?
                        ItemBookingInfoDto.builder()
                                .id(item.getNextBooking().getId())
                                .bookerId(item.getNextBooking().getBooker().getId())
                                .build() : null)
                .comments(item.getComments().stream().map(CommentMapper::toCommentDto).collect(Collectors.toList()))
                .build();
    }

    public static Item toItem(ItemDto itemDto, long userId) {
        return Item.builder()
                .id(itemDto.getId())
                .owner(User.builder()
                        .id(userId)
                        .build())
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .build();
    }
}
