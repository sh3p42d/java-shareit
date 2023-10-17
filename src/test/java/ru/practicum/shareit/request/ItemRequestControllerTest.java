package ru.practicum.shareit.request;

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
import ru.practicum.shareit.helpers.GeneratorConverterHelper;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ItemRequestControllerTest extends GeneratorConverterHelper {
    private final String url = "/requests";

    @MockBean
    private ItemRequestService itemRequestService;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mvc;

    private ItemRequest itemRequest;
    private ItemRequestDto itemRequestDto;

    @BeforeEach
    void beforeEach() {
        itemRequest = generateItemRequest(generateItem());
        itemRequestDto = itemRequestToItemRequestDto(itemRequest);

        reset(itemRequestService);
    }

    @AfterEach
    void afterEach() {
        verifyNoMoreInteractions(itemRequestService);
    }

    @Test
    void shouldAddItemRequest() throws Exception {
        when(itemRequestService.add(any(ItemRequest.class), anyLong())).thenReturn(itemRequest);

        mvc.perform(post(url)
                        .content(mapper.writeValueAsString(itemRequestDto))
                        .header(USER_REQUEST_HEADER, itemRequest.getAuthor().getId())
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemRequest.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(itemRequest.getDescription())))
                .andExpect(jsonPath("$.created", is(itemRequest.getCreated().toLocalDateTime().toString())))
                .andExpect(jsonPath("$.items", hasSize(1)));

        verify(itemRequestService, times(1)).add(any(ItemRequest.class), anyLong());
    }

    @Test
    void shouldGetItemRequestById() throws Exception {
        when(itemRequestService.getById(anyLong(), anyLong())).thenReturn(itemRequest);

        mvc.perform(get(url + "/" + itemRequest.getId())
                        .header(USER_REQUEST_HEADER, itemRequest.getAuthor().getId())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemRequest.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(itemRequest.getDescription())))
                .andExpect(jsonPath("$.created", is(itemRequest.getCreated().toLocalDateTime().toString())))
                .andExpect(jsonPath("$.items", hasSize(1)));

        verify(itemRequestService, times(1)).getById(anyLong(), anyLong());
    }

    @Test
    void shouldGetAllItemRequest() throws Exception {
        when(itemRequestService.getAll(anyLong())).thenReturn(List.of(itemRequest));

        mvc.perform(get(url)
                        .header(USER_REQUEST_HEADER, itemRequest.getAuthor().getId())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(itemRequest.getId()), Long.class))
                .andExpect(jsonPath("$[0].description", is(itemRequest.getDescription())))
                .andExpect(jsonPath("$[0].created", is(itemRequest.getCreated().toLocalDateTime().toString())))
                .andExpect(jsonPath("$[0].items", hasSize(1)));

        verify(itemRequestService, times(1)).getAll(anyLong());
    }

    @Test
    void shouldGetAllItemRequestWithPages() throws Exception {
        when(itemRequestService.getPage(anyInt(), anyInt(), anyLong())).thenReturn(List.of(itemRequest));

        mvc.perform(get(url + "/all")
                        .param("from", "0")
                        .param("size", "10")
                        .header(USER_REQUEST_HEADER, itemRequest.getAuthor().getId())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(itemRequest.getId()), Long.class))
                .andExpect(jsonPath("$[0].description", is(itemRequest.getDescription())))
                .andExpect(jsonPath("$[0].created", is(itemRequest.getCreated().toLocalDateTime().toString())))
                .andExpect(jsonPath("$[0].items", hasSize(1)));

        verify(itemRequestService, times(1)).getPage(anyInt(), anyInt(), anyLong());
    }
}
