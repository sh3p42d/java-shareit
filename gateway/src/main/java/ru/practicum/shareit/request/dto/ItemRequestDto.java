package ru.practicum.shareit.request.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.config.validator.Create;
import ru.practicum.shareit.item.dto.ItemRequestInfoDto;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ItemRequestDto {
    private Long id;
    @NotBlank(message = "Описание не может быть пустым.", groups = {Create.class})
    private String description;
    private LocalDateTime created;
    private List<ItemRequestInfoDto> items;
}