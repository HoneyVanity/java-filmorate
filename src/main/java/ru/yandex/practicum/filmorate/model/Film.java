package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
public class Film {
    @PositiveOrZero
    private int id;
    @NotEmpty
    private String name;
    @NotEmpty
    @Size(max = 200)
    private String description;
    @Past
    private LocalDate releaseDate;
    @DecimalMin("1")
    private int duration;

}
