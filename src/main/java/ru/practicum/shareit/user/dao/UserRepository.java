package ru.practicum.shareit.user.dao;

import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository  {

    Optional<User> add(User user);

    Optional<User> getById(long id);

    List<User> getAll();

    Optional<User> update(User user, long id);

    void delete(long id);
}
