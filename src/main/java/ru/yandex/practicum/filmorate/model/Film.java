package ru.yandex.practicum.filmorate.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Film {
    @PositiveOrZero Long id;
    @NotEmpty String name;
    @NotEmpty @Size(max = 200) String description;
    @Past LocalDate releaseDate;
    @DecimalMin("1") int duration;
    Set<Long> likes = new HashSet<>();

}
