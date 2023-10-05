package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.config.validator.ValidateDate;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@ValidateDate(message = "Дата окончания бронирования не может быть раньше даты начала бронирования.")
public class BookingResponseDto implements Serializable {
    private final long itemId;
    @NotNull
    @FutureOrPresent(message = "Дата начала бронирования не может быть в прошлом")
    private final LocalDateTime start;
    @NotNull
    @Future(message = "Дата конца бронирования не может быть в прошлом")
    private final LocalDateTime end;
}