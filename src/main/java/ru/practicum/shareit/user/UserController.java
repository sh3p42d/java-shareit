package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<UserDto> getAll() {
        return userService.getAll().stream().map(UserMapper::toUserDto).collect(Collectors.toList());
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public Optional<UserDto> getById(@PathVariable final long id) {
        return userService.getById(id).map(UserMapper::toUserDto);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Optional<UserDto> add(@Valid @RequestBody final UserDto dto) {
        return userService.add(UserMapper.toUser(dto)).map(UserMapper::toUserDto);
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/{id}")
    public Optional<UserDto> update(@RequestBody final UserDto dto,
                                    @PathVariable final long id) {
        return userService.update(UserMapper.toUser(dto), id).map(UserMapper::toUserDto);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable final long id) {
        userService.delete(id);
    }
}
