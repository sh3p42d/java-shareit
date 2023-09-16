package ru.practicum.shareit.user.error;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long id) {
        super(String.format("User с id=%s не найден.", id));
    }
}
