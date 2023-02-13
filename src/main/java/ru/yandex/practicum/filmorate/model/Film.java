package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class Film {
    @PositiveOrZero
    private Long id;
    @NotEmpty
    private String name;
    @NotEmpty
    @Size(max = 200)
    private String description;
    @Past
    private LocalDate releaseDate;
    @DecimalMin("1")
    private int duration;
    private Set<Long> likes = new HashSet<>();

}
