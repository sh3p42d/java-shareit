package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.error.UserNotFoundException;
import ru.practicum.shareit.user.error.UserRepeatEmailException;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public Optional<User> add(User user) {
        return Optional.of(userRepository.add(user)
                .orElseThrow(() -> new UserRepeatEmailException(user.getEmail())));
    }

    @Override
    public Optional<User> getById(long id) {
        return Optional.of(userRepository.getById(id)
                .orElseThrow(() -> new UserNotFoundException(id)));
    }

    @Override
    public List<User> getAll() {
        return userRepository.getAll();
    }

    @Override
    public Optional<User> update(User user, long id) {
        return Optional.of(userRepository.update(user, id)
                .orElseThrow(() -> new UserRepeatEmailException(user.getEmail())));
    }

    @Override
    public void delete(long id) {
        userRepository.delete(id);
    }
}
