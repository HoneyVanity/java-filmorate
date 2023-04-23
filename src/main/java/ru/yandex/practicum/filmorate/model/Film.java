package ru.yandex.practicum.filmorate.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Film {
    @PositiveOrZero Long id;
    @NotEmpty String name;
    @NotEmpty @Size(max = 200) String description;
    @Past LocalDate releaseDate;
    @DecimalMin("1") int duration;
    Mpa mpa;
    Set<Long> likes;
    List<Genre> genres;

}
