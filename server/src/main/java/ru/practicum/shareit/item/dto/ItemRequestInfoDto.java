package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class ItemRequestInfoDto implements Serializable {
    private long id;
    private String name;
    private String description;
    private Boolean available;
    private long requestId;
}
