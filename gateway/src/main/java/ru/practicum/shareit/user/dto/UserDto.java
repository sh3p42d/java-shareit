package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.config.validator.Create;
import ru.practicum.shareit.config.validator.Update;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto implements Serializable {
    private Long id;
    private String name;
    @NotBlank(message = "Почта пользователя не может быть пустой", groups = {Create.class})
    @Email(message = "Почта не соответствует формату mail@domain.com", groups = {Create.class, Update.class})
    private String email;
}
