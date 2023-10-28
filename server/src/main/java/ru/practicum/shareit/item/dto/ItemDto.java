package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
public class ItemDto implements Serializable {
    private long id;
    private long owner;
    private String name;
    private String description;
    private Boolean available;
    private ItemBookingInfoDto lastBooking;
    private ItemBookingInfoDto nextBooking;
    private List<CommentDto> comments;
    private Long requestId;
}
