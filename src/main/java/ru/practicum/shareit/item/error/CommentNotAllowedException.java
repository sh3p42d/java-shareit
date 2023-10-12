package ru.practicum.shareit.item.error;

public class CommentNotAllowedException extends RuntimeException {
    public CommentNotAllowedException(long authorId, long itemId) {
        super(String.format("User с id=%s не может оставить Comment для Item с id=%s.", authorId, itemId));
    }
}