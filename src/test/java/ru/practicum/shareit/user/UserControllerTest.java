package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.practicum.shareit.helpers.GeneratorConverterHelper;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.error.UserNotFoundException;
import ru.practicum.shareit.user.error.UserRepeatEmailException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest extends GeneratorConverterHelper {
    private final String url = "/users";

    @MockBean
    private UserService userService;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mvc;

    private User user;
    private UserDto userDto;

    @BeforeEach
    void beforeEach() {
        user = generateUser();
        userDto = userToUserDto(user);

        reset(userService);
    }

    @AfterEach
    void mockVerify() {
        verifyNoMoreInteractions(userService);
    }

    @Test
    void shouldAddUser() throws Exception {
        when(userService.add(any(User.class))).thenReturn(user);

        mvc.perform(post(url)
                        .content(mapper.writeValueAsString(userDto))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(user.getId()), Long.class))
                .andExpect(jsonPath("$.email", is(user.getEmail())))
                .andExpect(jsonPath("$.name", is(user.getName())));

        verify(userService, times(1)).add(any(User.class));
    }

    @Test
    void shouldNotAddUser() throws Exception {
        MvcResult result = mvc.perform(post(url)
                        .content(mapper.writeValueAsString(UserDto.builder()
                                .id(1L)
                                .build()))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isBadRequest())
                .andReturn();

        assertNotNull(result.getResolvedException());
        assertTrue(result.getResolvedException().getMessage().contains("Почта пользователя не может быть пустой"));
    }

    @Test
    void shouldNotRepeatEmail() throws Exception {
        when(userService.add(any(User.class))).thenThrow(new UserRepeatEmailException("email@email.com"));

        MvcResult result = mvc.perform(post(url)
                        .content(mapper.writeValueAsString(userDto))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isConflict())
                .andReturn();

        assertNotNull(result.getResolvedException());
        assertTrue(result.getResolvedException().getMessage().contains("User c email=email@email.com уже существует."));
        verify(userService, times(1)).add(any(User.class));
    }

    @Test
    void shouldGetUserById() throws Exception {
        when(userService.getById(anyLong())).thenReturn(user);

        mvc.perform(get(url + "/" + user.getId())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(user.getId()), Long.class))
                .andExpect(jsonPath("$.email", is(user.getEmail())))
                .andExpect(jsonPath("$.name", is(user.getName())));

        verify(userService, times(1)).getById(anyLong());
    }

    @Test
    void shouldNotGetUserById() throws Exception {
        when(userService.getById(anyLong())).thenThrow(new UserNotFoundException(10L));

        MvcResult result = mvc.perform(get(url + "/" + 10)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();

        assertNotNull(result.getResolvedException());
        assertTrue(result.getResolvedException().getMessage().contains("User с id=10 не найден."));
        verify(userService, times(1)).getById(anyLong());
    }

    @Test
    void shouldGetAllUser() throws Exception {
        when(userService.getAll()).thenReturn(List.of(user));

        mvc.perform(get(url)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(user.getId()), Long.class))
                .andExpect(jsonPath("$[0].email", is(user.getEmail())))
                .andExpect(jsonPath("$[0].name", is(user.getName())));

        verify(userService, times(1)).getAll();
    }

    @Test
    void shouldUpdateUser() throws Exception {
        when(userService.update(user, user.getId())).thenReturn(user);

        mvc.perform(patch(url + "/" + user.getId())
                        .content(mapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(user.getId()), Long.class))
                .andExpect(jsonPath("$.email", is(user.getEmail())))
                .andExpect(jsonPath("$.name", is(user.getName())));

        verify(userService, times(1)).update(user, user.getId());
    }

    @Test
    void shouldNotUpdateUser() throws Exception {
        when(userService.update(any(User.class), anyLong())).thenThrow(new UserNotFoundException(10L));

        MvcResult result = mvc.perform(patch(url + "/" + 10)
                        .content(mapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();

        assertNotNull(result.getResolvedException());
        assertTrue(result.getResolvedException().getMessage().contains("User с id=10 не найден."));
        verify(userService, times(1)).update(any(User.class), anyLong());
    }

    @Test
    void deleteUserRequest() throws Exception {
        mvc.perform(delete(url + "/" + user.getId())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(userService, times(1)).delete(user.getId());
    }
}