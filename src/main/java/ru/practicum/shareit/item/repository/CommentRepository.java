package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.dto.CommentInfoDto;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<CommentInfoDto> findAllByItemId(long itemId);
}