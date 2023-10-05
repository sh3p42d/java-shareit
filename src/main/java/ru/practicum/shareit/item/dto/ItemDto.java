package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.request.ItemRequest;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
@Builder
public class ItemDto implements Serializable {
    private long id;
    private long owner;
    @NotBlank(message = "Имя Item не может быть пустым")
    private String name;
    @NotBlank(message = "Описание Item не может быть пустым")
    private String description;
    @NotNull(message = "Статус доступности Item не может быть пустым")
    private Boolean available;
    private ItemRequest request;
    private ItemBookingInfoDto lastBooking;
    private ItemBookingInfoDto nextBooking;
    private List<CommentDto> comments;
}
