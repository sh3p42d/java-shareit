package ru.practicum.shareit.user;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.practicum.shareit.helpers.GeneratorConverterHelper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserServiceTest extends GeneratorConverterHelper {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository mockUserRepository;

    private User testValueUser;

    @BeforeEach
    void beforeEach() {
        testValueUser = generateUser();

        reset(mockUserRepository);
    }

    @AfterEach
    void mockVerify() {
        verifyNoMoreInteractions(mockUserRepository);
    }

    @Test
    void shouldAddUser() {
        when(mockUserRepository.save(testValueUser)).thenReturn(testValueUser);

        User user = userService.add(testValueUser);
        assertEquals(testValueUser.getId(), user.getId());
        assertEquals(testValueUser.getName(), user.getName());
        assertEquals(testValueUser.getEmail(), user.getEmail());
        assertEquals(testValueUser.toString(), user.toString());
        assertEquals(testValueUser.hashCode(), user.hashCode());

        verify(mockUserRepository, times(1)).save(user);
    }

    @Test
    void shouldGetByIdUser() {
        when(mockUserRepository.findById(testValueUser.getId()))
                .thenReturn(Optional.ofNullable(testValueUser));

        User user = userService.getById(testValueUser.getId());
        assertEquals(testValueUser.getId(), user.getId());
        assertEquals(testValueUser.getName(), user.getName());
        assertEquals(testValueUser.getEmail(), user.getEmail());
        assertEquals(testValueUser.toString(), user.toString());
        assertEquals(testValueUser.hashCode(), user.hashCode());

        verify(mockUserRepository, times(1)).findById(testValueUser.getId());
    }

    @Test
    void shouldGetAllUser() {
        when(mockUserRepository.findAll())
                .thenReturn(List.of(testValueUser));

        List<User> all = userService.getAll();
        assertEquals(testValueUser.getId(), all.get(0).getId());
        assertEquals(testValueUser.getName(), all.get(0).getName());
        assertEquals(testValueUser.getEmail(), all.get(0).getEmail());
        assertEquals(testValueUser.toString(), all.get(0).toString());
        assertEquals(testValueUser.hashCode(), all.get(0).hashCode());

        verify(mockUserRepository, times(1)).findAll();
    }

    @Test
    void shouldUpdateUser() {
        when(mockUserRepository.findById(testValueUser.getId()))
                .thenReturn(Optional.ofNullable(testValueUser));
        when(mockUserRepository.save(testValueUser))
                .thenReturn(testValueUser);

        User user = userService.update(testValueUser, testValueUser.getId());
        assertEquals(testValueUser.getId(), user.getId());
        assertEquals(testValueUser.getName(), user.getName());
        assertEquals(testValueUser.getEmail(), user.getEmail());
        assertEquals(testValueUser.toString(), user.toString());
        assertEquals(testValueUser.hashCode(), user.hashCode());

        verify(mockUserRepository, times(1)).findById(testValueUser.getId());
        verify(mockUserRepository, times(1)).save(user);
    }

    @Test
    void shouldDeleteUser() {
        userService.delete(testValueUser.getId());

        verify(mockUserRepository, times(1)).deleteById(testValueUser.getId());
    }
}
