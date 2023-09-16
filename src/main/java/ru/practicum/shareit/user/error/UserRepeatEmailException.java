package ru.practicum.shareit.user.error;

public class UserRepeatEmailException extends RuntimeException {

    public UserRepeatEmailException(String email) {
        super(String.format("User c email: %s уже существует.", email));
    }
}
