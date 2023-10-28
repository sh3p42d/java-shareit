package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;
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
    public UserDto getById(@PathVariable final long id) {
        return UserMapper.toUserDto(userService.getById(id));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public UserDto add(@RequestBody final UserDto dto) {
        return UserMapper.toUserDto(userService.add(UserMapper.toUser(dto)));
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/{id}")
    public UserDto update(@RequestBody final UserDto dto,
                          @PathVariable final long id) {
        return UserMapper.toUserDto(userService.update(UserMapper.toUser(dto), id));
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable final long id) {
        userService.delete(id);
    }
}
