package ru.practicum.shareit.request;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ItemRequest {
    private long id;
    private long userId;
    private String itemDescription;
    private LocalDateTime created;
}
