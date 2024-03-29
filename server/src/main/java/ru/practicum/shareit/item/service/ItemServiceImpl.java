package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import ru.practicum.shareit.booking.StatusBooking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.error.CommentNotAllowedException;
import ru.practicum.shareit.item.error.ItemNotFoundByUserException;
import ru.practicum.shareit.item.error.ItemNotFoundException;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.service.UserService;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {
    private final UserService userService;
    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    @Override
    @Transactional
    public Item add(final Item item, final long userId) {
        item.setOwner(userService.getById(userId));
        return itemRepository.save(item);
    }

    @Override
    public Item getById(final long id, final long userId) {
        Item item = itemRepository.findById(id).orElseThrow(() -> new ItemNotFoundException(id));
        fillBookingInfoByItem(item, userId);
        fillCommentByItem(item);
        return item;
    }

    @Override
    public List<Item> getAll(final long userId, final int from, final int size) {
        Sort sort = Sort.by("id").ascending();
        List<Item> itemList = itemRepository.findAllByOwnerId(userId, PageRequest.of(from / size, size, sort)).getContent();

        return itemList.stream()
                .map(item -> fillBookingInfoByItem(item, userId))
                .map(this::fillCommentByItem)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Item update(final Item item, final long id, final long userId) {
        Optional<Item> itemCheck = itemRepository.findById(id);
        Item itemUpdate = itemCheck.orElseThrow(() -> new ItemNotFoundException(id));
        itemCheck.map(i -> i.getOwner().getId())
                .filter(s -> s == userId)
                .orElseThrow(() -> new ItemNotFoundByUserException(id, userId));
        Optional.ofNullable(item.getName()).ifPresent(itemUpdate::setName);
        Optional.ofNullable(item.getDescription()).ifPresent(itemUpdate::setDescription);
        Optional.ofNullable(item.getAvailable()).ifPresent(itemUpdate::setAvailable);
        return itemRepository.save(itemUpdate);
    }

    @Override
    public List<Item> search(final String text, final int from, final int size) {
        if (!StringUtils.hasText(text)) {
            return Collections.emptyList();
        }
        return itemRepository
                .findAllByNameOrDescriptionContainingIgnoreCaseAndAvailableTrue(text, text, PageRequest.of(from / size, size))
                .getContent();
    }

    @Override
    @Transactional
    public Comment addComment(final Comment comment, final long author, final long itemId) {
        bookingRepository.findFirstByBookerIdAndItemIdAndStatusNotLikeAndEndBefore(author, itemId, StatusBooking.REJECTED, comment.getCreated())
                .orElseThrow(() -> new CommentNotAllowedException(author, itemId));
        comment.setItem(this.getById(itemId, author));
        comment.setAuthor(userService.getById(author));
        return commentRepository.save(comment);
    }

    private Item fillBookingInfoByItem(final Item item, final long userId) {
        Sort startAsc = Sort.by("start").ascending();
        Sort endDesc = Sort.by("end").descending();

        if (item.getOwner().getId() == userId) {
            item.setNextBooking(bookingRepository.findFirstByItemIdAndStartAfterAndStatus(item.getId(),
                    Timestamp.valueOf(LocalDateTime.now()), StatusBooking.APPROVED, startAsc));
            item.setLastBooking(bookingRepository.findFirstByItemIdAndStartBeforeAndStatus(item.getId(),
                    Timestamp.valueOf(LocalDateTime.now()), StatusBooking.APPROVED, endDesc));
        }
        return item;
    }

    private Item fillCommentByItem(final Item item) {
        item.setComments(new ArrayList<>(commentRepository.findAllByItemId(item.getId())));
        return item;
    }
}
