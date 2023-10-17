package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.helpers.GeneratorConverterHelper;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class UserMapperTest extends GeneratorConverterHelper {
    private User user;
    private UserDto userDto;

    @BeforeEach
    public void beforeEach() {
        user = generateUser();
        userDto = userToUserDto(user);
    }

    @Test
    void shouldCreateUserDto() {
        UserDto testUserDto = UserMapper.toUserDto(user);

        assertEquals(testUserDto.getId(), user.getId());
        assertEquals(testUserDto.getName(), user.getName());
        assertEquals(testUserDto.getEmail(), user.getEmail());
    }

    @Test
    void shouldCreateUser() {
        User testUser = UserMapper.toUser(userDto);

        assertEquals(testUser.getId(), userDto.getId());
        assertEquals(testUser.getName(), userDto.getName());
        assertEquals(testUser.getEmail(), userDto.getEmail());
    }

    @Test
    void shouldNotEquals() {
        User testUser = generateUser();

        assertNotEquals(testUser, user);
    }
}
