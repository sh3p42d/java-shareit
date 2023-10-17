package ru.practicum.shareit.item;

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
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ItemControllerTest extends GeneratorConverterHelper {
    private final String url = "/items";

    @MockBean
    private ItemService itemService;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mvc;

    private Item item;
    private ItemDto itemDto;

    @BeforeEach
    void beforeEach() {
        item = generateItem();
        itemDto = itemToItemDto(item);

        reset(itemService);
    }

    @AfterEach
    void afterEach() {
        verifyNoMoreInteractions(itemService);
    }

    @Test
    void shouldAddItem() throws Exception {
        when(itemService.add(any(Item.class), anyLong())).thenReturn(item);

        mvc.perform(post(url)
                        .content(mapper.writeValueAsString(itemDto))
                        .header(USER_REQUEST_HEADER, item.getOwner().getId())
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(item.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(item.getName())))
                .andExpect(jsonPath("$.description", is(item.getDescription())))
                .andExpect(jsonPath("$.owner", is(item.getOwner().getId().intValue())))
                .andExpect(jsonPath("$.available", is(true)));

        verify(itemService, times(1)).add(any(Item.class), anyLong());
    }

    @Test
    void shouldGetItemById() throws Exception {
        when(itemService.getById(anyLong(), anyLong())).thenReturn(item);

        mvc.perform(get(url + "/" + item.getId())
                        .header(USER_REQUEST_HEADER, item.getOwner().getId())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(item.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(item.getName())))
                .andExpect(jsonPath("$.description", is(item.getDescription())))
                .andExpect(jsonPath("$.owner", is(item.getOwner().getId().intValue())))
                .andExpect(jsonPath("$.available", is(true)));

        verify(itemService, times(1)).getById(anyLong(), anyLong());
    }

    @Test
    void shouldGetAllItem() throws Exception {
        when(itemService.getAll(anyLong(), anyInt(), anyInt())).thenReturn(List.of(item));

        mvc.perform(get(url)
                        .param("from", "0")
                        .param("size", "10")
                        .header(USER_REQUEST_HEADER, item.getOwner().getId())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(item.getId()), Long.class))
                .andExpect(jsonPath("$[0].name", is(item.getName())))
                .andExpect(jsonPath("$[0].description", is(item.getDescription())))
                .andExpect(jsonPath("$[0].owner", is(item.getOwner().getId().intValue())))
                .andExpect(jsonPath("$[0].available", is(true)));

        verify(itemService, times(1)).getAll(anyLong(), anyInt(), anyInt());
    }

    @Test
    void shouldUpdateItem() throws Exception {
        when(itemService.update(any(Item.class), anyLong(), anyLong())).thenReturn(item);

        mvc.perform(patch(url + "/" + item.getId())
                        .content(mapper.writeValueAsString(itemDto))
                        .header(USER_REQUEST_HEADER, item.getOwner().getId())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(item.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(item.getName())))
                .andExpect(jsonPath("$.description", is(item.getDescription())))
                .andExpect(jsonPath("$.owner", is(item.getOwner().getId().intValue())))
                .andExpect(jsonPath("$.available", is(true)));

        verify(itemService, times(1)).update(any(Item.class), anyLong(), anyLong());
    }

    @Test
    void shouldSearchItem() throws Exception {
        when(itemService.search(anyString(), anyInt(), anyInt())).thenReturn(List.of(item));

        mvc.perform(get(url + "/search")
                        .param("from", "0")
                        .param("size", "10")
                        .param("text", "test")
                        .header(USER_REQUEST_HEADER, item.getOwner().getId())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(item.getId()), Long.class))
                .andExpect(jsonPath("$[0].name", is(item.getName())))
                .andExpect(jsonPath("$[0].description", is(item.getDescription())))
                .andExpect(jsonPath("$[0].owner", is(item.getOwner().getId().intValue())))
                .andExpect(jsonPath("$[0].available", is(true)));

        verify(itemService, times(1)).search(anyString(), anyInt(), anyInt());
    }

    @Test
    void shouldAddCommentToItem() throws Exception {
        final Comment comment = generateComment(item);

        final CommentDto commentDto = commentToCommentDto(comment);

        when(itemService.addComment(any(Comment.class), anyLong(), anyLong())).thenReturn(comment);

        mvc.perform(post(url + "/" + item.getId() + "/comment")
                        .content(mapper.writeValueAsString(commentDto))
                        .header(USER_REQUEST_HEADER, item.getOwner().getId())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(comment.getId()), Long.class))
                .andExpect(jsonPath("$.text", is(comment.getText())))
                .andExpect(jsonPath("$.authorName", is(comment.getAuthor().getName())))
                .andExpect(jsonPath("$.created", notNullValue()));

        verify(itemService, times(1)).addComment(any(Comment.class), anyLong(), anyLong());
    }
}
