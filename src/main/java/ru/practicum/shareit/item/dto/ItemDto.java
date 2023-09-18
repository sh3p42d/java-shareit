package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.request.ItemRequest;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class ItemDto {
    private long id;
    private long owner;
    @NotBlank(message = "Имя Item не может быть пустым")
    private String name;
    @NotBlank(message = "Описание Item не может быть пустым")
    private String description;
    @NotNull(message = "Статус доступности Item не может быть пустым")
    private Boolean available;
    private ItemRequest request;
}
