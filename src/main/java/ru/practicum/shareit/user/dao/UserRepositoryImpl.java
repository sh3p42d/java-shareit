package ru.practicum.shareit.user.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

import java.util.*;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    private final Map<Long, User> users;
    private Long id = 1L;

    @Override
    public Optional<User> add(User user) {
        user.setId(id++);
        if (emailCheck(user)) {
            --id;
            return Optional.empty();
        }
        users.put(user.getId(), user);
        return Optional.of(users.get(user.getId()));
    }

    @Override
    public Optional<User> getById(long id) {
        if (users.containsKey(id)) {
            return Optional.of(users.get(id));
        }
        return Optional.empty();
    }

    @Override
    public List<User> getAll() {
        return users.values().stream()
                .sorted(Comparator.comparingLong(User::getId))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<User> update(User user, long id) {
        if (emailCheck(user)) {
            if (!Objects.equals(users.get(id).getEmail(), user.getEmail())) {
                return Optional.empty();
            }
        }
        User userUpdate = users.get(id);
        Optional.ofNullable(user.getName()).ifPresent(userUpdate::setName);
        Optional.ofNullable(user.getEmail()).ifPresent(userUpdate::setEmail);
        System.out.println(userUpdate);
        return Optional.of(userUpdate);
    }

    @Override
    public void delete(long id) {
        users.remove(id);
    }

    private boolean emailCheck(User user) {
        for (User userEmailSearch : users.values()) {
            if (userEmailSearch.getEmail().equals(user.getEmail())) {
                return true;
            }
        }
        return false;
    }
}
