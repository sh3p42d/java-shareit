package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.user.model.User;

import java.sql.Timestamp;

public interface CommentInfoDto {
    long getId();

    String getText();

    User getAuthor();

    Timestamp getCreated();
}