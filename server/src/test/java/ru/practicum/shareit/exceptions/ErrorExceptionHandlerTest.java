package ru.practicum.shareit.exceptions;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.error.*;
import ru.practicum.shareit.config.exceptions.ErrorExceptionHandler;
import ru.practicum.shareit.config.exceptions.ErrorResponse;
import ru.practicum.shareit.item.error.CommentNotAllowedException;
import ru.practicum.shareit.item.error.ItemNotFoundByUserException;
import ru.practicum.shareit.item.error.ItemNotFoundException;
import ru.practicum.shareit.request.error.ItemRequestNotFoundException;
import ru.practicum.shareit.user.error.UserNotFoundException;
import ru.practicum.shareit.user.error.UserRepeatEmailException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class ErrorExceptionHandlerTest {
    @Autowired
    ErrorExceptionHandler error;

    @Test
    void shouldUserRepeatEmailException() {
        ErrorResponse responseEntity = error.errorResponse(new UserRepeatEmailException("test"));
        assertNotNull(responseEntity.getError());
        assertNotNull(responseEntity.getMessage());
        assertNotNull(responseEntity);
    }

    @Test
    void shouldIllegalException() {
        List<ErrorResponse> responseEntity = List.of(
                error.errorResponse(new BookingStateNotAvailable(1L)),
                error.errorResponse(new ItemNotAllowedException(1L)),
                error.errorResponse(new CommentNotAllowedException(1L, 1L))
        );
        assertNotNull(responseEntity);
        assertEquals(3, responseEntity.size());
    }

    @Test
    void shouldEntityNotFoundException() {
        List<ErrorResponse> responseEntity = List.of(
                error.errorResponse(new ItemNotFoundByUserException(1L, 1L)),
                error.errorResponse(new UserNotFoundException(1L)),
                error.errorResponse(new ItemNotFoundException(1L)),
                error.errorResponse(new BookingNotFoundException(1L)),
                error.errorResponse(new BookingNotAllowedException(1L)),
                error.errorResponse(new ItemRequestNotFoundException(1L))
        );
        assertNotNull(responseEntity);
        assertEquals(6, responseEntity.size());
    }

    @Test
    void shouldThrowable() {
        ErrorResponse responseEntity = error.handleThrowable(new Throwable());
        assertNotNull(responseEntity);
    }

    @Test
    void shouldBookingWrongStateException() {
        ErrorResponse responseEntity = error.handleThrowable(new BookingWrongStateException("test"));
        assertNotNull(responseEntity);
    }
}
