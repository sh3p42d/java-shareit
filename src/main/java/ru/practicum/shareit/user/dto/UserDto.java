package ru.practicum.shareit.user.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@Builder
public class UserDto {
    private Long id;
    private String name;
    @NotBlank(message = "Почта пользователя не может быть пустой")
    @Email(message = "Почта не соответствует формату mail@domain.com")
    private String email;
}
