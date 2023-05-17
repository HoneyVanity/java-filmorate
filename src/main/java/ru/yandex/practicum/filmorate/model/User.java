package ru.yandex.practicum.filmorate.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class User {
    @NonFinal
    @PositiveOrZero long id;
    @NonNull String login;
    @NonFinal
    String name;
    @Email String email;
    @Past LocalDate birthday;
    @NonFinal
    Set<Long> friendsList;
}
