package ru.practicum.shareit.booking;

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
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.helpers.GeneratorConverterHelper;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class BookingControllerTest extends GeneratorConverterHelper {
    private final String url = "/bookings";

    @MockBean
    private BookingService bookingService;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mvc;

    private Booking booking;
    private BookingResponseDto bookingResponseDto;

    @BeforeEach
    void beforeEach() {
        booking = generateBooking();
        bookingResponseDto = bookingToBookingResponseDto(booking);

        reset(bookingService);
    }

    @AfterEach
    void afterEach() {
        verifyNoMoreInteractions(bookingService);
    }

    @Test
    void shouldAddBooking() throws Exception {
        when(bookingService.add(any(Booking.class), anyLong())).thenReturn(booking);

        mvc.perform(post(url)
                        .content(mapper.writeValueAsString(bookingResponseDto))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                        .header(USER_REQUEST_HEADER, booking.getBooker().getId())
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(booking.getId()), Long.class))
                .andExpect(jsonPath("$.booker", notNullValue()))
                .andExpect(jsonPath("$.item", notNullValue()))
                .andExpect(jsonPath("$.start", is(booking.getStart().toLocalDateTime().toString())))
                .andExpect(jsonPath("$.end", is(booking.getEnd().toLocalDateTime().toString())))
                .andExpect(jsonPath("$.status", is(booking.getStatus().toString())));

        verify(bookingService, times(1)).add(any(Booking.class), anyLong());
    }

    @Test
    void shouldGetBookingById() throws Exception {
        when(bookingService.getById(anyLong(), anyLong())).thenReturn(booking);

        mvc.perform(get(url + "/" + booking.getId())
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                        .header(USER_REQUEST_HEADER, booking.getBooker().getId())
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(booking.getId()), Long.class))
                .andExpect(jsonPath("$.booker", notNullValue()))
                .andExpect(jsonPath("$.item", notNullValue()))
                .andExpect(jsonPath("$.start", is(booking.getStart().toLocalDateTime().toString())))
                .andExpect(jsonPath("$.end", is(booking.getEnd().toLocalDateTime().toString())))
                .andExpect(jsonPath("$.status", is(booking.getStatus().toString())));

        verify(bookingService, times(1)).getById(anyLong(), anyLong());
    }

    @Test
    void shouldGetAllBooking() throws Exception {
        when(bookingService.findAll(anyLong(), anyString(), anyInt(), anyInt()))
                .thenReturn(List.of(BookingMapper.toBookingDto(booking)));

        mvc.perform(get(url)
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                        .header(USER_REQUEST_HEADER, booking.getBooker().getId())
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(booking.getId()), Long.class))
                .andExpect(jsonPath("$[0].booker", notNullValue()))
                .andExpect(jsonPath("$[0].item", notNullValue()))
                .andExpect(jsonPath("$[0].start", is(booking.getStart().toLocalDateTime().toString())))
                .andExpect(jsonPath("$[0].end", is(booking.getEnd().toLocalDateTime().toString())))
                .andExpect(jsonPath("$[0].status", is(booking.getStatus().toString())))
                .andReturn();

        verify(bookingService, times(1)).findAll(anyLong(), anyString(), anyInt(), anyInt());
    }

    @Test
    void shouldGetAllOwnerBooking() throws Exception {
        when(bookingService.findAllOwner(anyLong(), anyString(), anyInt(), anyInt()))
                .thenReturn(List.of(BookingMapper.toBookingDto(booking)));

        mvc.perform(get(url + "/owner")
                        .param("from", "0")
                        .param("size", "10")
                        .param("state", "ALL")
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                        .header(USER_REQUEST_HEADER, booking.getBooker().getId())
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(booking.getId()), Long.class))
                .andExpect(jsonPath("$[0].booker", notNullValue()))
                .andExpect(jsonPath("$[0].item", notNullValue()))
                .andExpect(jsonPath("$[0].start", is(booking.getStart().toLocalDateTime().toString())))
                .andExpect(jsonPath("$[0].end", is(booking.getEnd().toLocalDateTime().toString())))
                .andExpect(jsonPath("$[0].status", is(booking.getStatus().toString())));

        verify(bookingService, times(1)).findAllOwner(anyLong(), anyString(), anyInt(), anyInt());
    }

    @Test
    void shouldUpdateBooking() throws Exception {
        when(bookingService.approved(anyLong(), anyLong(), anyBoolean())).thenReturn(booking);

        mvc.perform(patch(url + "/" + booking.getId())
                        .param("approved", "true")
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                        .header(USER_REQUEST_HEADER, booking.getBooker().getId())
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(booking.getId()), Long.class))
                .andExpect(jsonPath("$.booker", notNullValue()))
                .andExpect(jsonPath("$.item", notNullValue()))
                .andExpect(jsonPath("$.start", is(booking.getStart().toLocalDateTime().toString())))
                .andExpect(jsonPath("$.end", is(booking.getEnd().toLocalDateTime().toString())))
                .andExpect(jsonPath("$.status", is(booking.getStatus().toString())));

        verify(bookingService, times(1)).approved(anyLong(), anyLong(), anyBoolean());
    }
}
