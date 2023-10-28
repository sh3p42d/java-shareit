package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.config.validator.Create;
import ru.practicum.shareit.config.validator.Update;
import ru.practicum.shareit.user.dto.UserDto;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserClient userClient;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ResponseEntity<Object> create(@Validated({Create.class}) @RequestBody final UserDto dto) {
        return userClient.create(dto);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("{id}")
    public ResponseEntity<Object> findById(@PathVariable final long id) {
        return userClient.findById(id);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public ResponseEntity<Object> findAll() {
        return userClient.findAll();
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("{id}")
    public ResponseEntity<Object> update(@Validated({Update.class})
                                         @RequestBody final UserDto dto,
                                         @PathVariable final long id) {
        return userClient.update(id, dto);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("{id}")
    public void delete(@PathVariable final long id) {
        userClient.delete(id);
    }
}
