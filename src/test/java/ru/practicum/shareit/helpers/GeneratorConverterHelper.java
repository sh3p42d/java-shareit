package ru.practicum.shareit.helpers;

import org.springframework.data.domain.Sort;
import ru.practicum.shareit.booking.StatusBooking;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.CommentMapper;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemBookingInfoDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemRequestInfoDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class GeneratorConverterHelper {
    protected static final String USER_REQUEST_HEADER = "X-Sharer-User-Id";
    protected final Sort startAsc = Sort.by("start").ascending();
    protected final Sort endDesc = Sort.by("end").descending();

    private final Timestamp start = generateTime();
    private final Timestamp end = Timestamp.valueOf(start.toLocalDateTime().plusDays(5));

    protected Booking generateBooking() {
        return Booking.builder()
                .id(ThreadLocalRandom.current().nextLong(1, 1000))
                .booker(generateUser())
                .item(generateItem())
                .start(start)
                .end(end)
                .status(StatusBooking.WAITING)
                .build();
    }

    protected BookingResponseDto bookingToBookingResponseDto(Booking booking) {
        return BookingResponseDto.builder()
                .itemId(booking.getItem().getId())
                .start(booking.getStart().toLocalDateTime())
                .end(booking.getEnd().toLocalDateTime())
                .build();
    }

    protected Booking generateBookingForRepository(User booker, Item item) {
        return new Booking(null, booker, item, start, end, StatusBooking.APPROVED);
    }

    protected Item generateItem() {
        return Item.builder()
                .id(ThreadLocalRandom.current().nextLong(1, 1000))
                .name(generateString())
                .description(generateString() + " " + generateString())
                .owner(generateUser())
                .available(true)
                .build();
    }

    protected ItemDto itemToItemDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .owner(item.getOwner().getId())
                .available(item.getAvailable())
                .lastBooking(item.getLastBooking() != null ?
                        ItemBookingInfoDto.builder()
                                .id(item.getLastBooking().getId())
                                .bookerId(item.getLastBooking().getBooker().getId())
                                .build() : null)
                .nextBooking(item.getNextBooking() != null ?
                        ItemBookingInfoDto.builder()
                                .id(item.getNextBooking().getId())
                                .bookerId(item.getNextBooking().getBooker().getId())
                                .build() : null)
                .comments(item.getComments().stream().map(CommentMapper::toCommentDto).collect(Collectors.toList()))
                .requestId(item.getRequest() != null ?
                        item.getRequest().getId() : null)
                .build();
    }

    protected Item generateItemForRepository(User owner) {
        return new Item(null, owner, generateString(), generateString(), true, null, null, null, null);
    }

    protected User generateUser() {
        return User.builder()
                .id(ThreadLocalRandom.current().nextLong(1, 1000))
                .email(generateString() + "@yandex.kz")
                .name(generateString())
                .build();
    }

    protected UserDto userToUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .build();
    }

    protected User generateUserForRepository() {
        return new User(null, generateString(), generateString() + "@yandex.ru");
    }

    protected Comment generateComment(Item item) {
        return Comment.builder()
                .id(ThreadLocalRandom.current().nextLong(1, 1000))
                .item(item)
                .author(generateUser())
                .text(generateString() + generateString() + generateString())
                .created(generateTime())
                .build();
    }

    protected CommentDto commentToCommentDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .authorName(comment.getAuthor().getName())
                .text(comment.getText())
                .created(comment.getCreated())
                .build();
    }

    protected ItemRequest generateItemRequest(Item item) {
        item.setRequest(ItemRequest.builder()
                .id(ThreadLocalRandom.current().nextLong(1, 1000))
                .description(generateString())
                .created(generateTime())
                .author(generateUser())
                .items(List.of(item))
                .build());
        return item.getRequest();
    }

    protected ItemRequestDto itemRequestToItemRequestDto(ItemRequest itemRequest) {
        return ItemRequestDto.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .created(itemRequest.getCreated().toLocalDateTime())
                .items(itemToItemRequestInfoDto(itemRequest.getItems()))
                .build();
    }

    protected ItemRequest generateItemRequestForRepository(User user, Item item) {
        return new ItemRequest(null, user, generateString(), generateTime(), List.of(item));
    }

    private List<ItemRequestInfoDto> itemToItemRequestInfoDto(List<Item> itemList) {
        return itemList.stream()
                .map(req -> ItemRequestInfoDto.builder()
                        .id(req.getId())
                        .name(req.getName())
                        .description(req.getDescription())
                        .available(req.getAvailable())
                        .requestId(req.getRequest().getId())
                        .build())
                .collect(Collectors.toList());
    }

    protected String generateString() {
        int leftLimit = 97;
        int rightLimit = 122;
        int targetStringLength = 10;
        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    protected Timestamp generateTime() {
        long offset = Timestamp.valueOf(LocalDateTime.now()).getTime();
        long end = Timestamp.valueOf(LocalDateTime.now().plusDays(3)).getTime();
        long diff = end - offset + 1;

        // пришлось сделать ограничение до целых секунд, иначе тесты падают из-за ошибок,
        // когда автоматически округляются миллисекунды, с x.110 до x.11, например
        return Timestamp.valueOf(
                new Timestamp(offset + (long) (Math.random() * diff))
                        .toLocalDateTime()
                        .truncatedTo(ChronoUnit.SECONDS)
        );
    }
}
