package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Builder
public class CommentDto implements Serializable {
    private long id;
    private String text;
    private String authorName;
    private Timestamp created;
}