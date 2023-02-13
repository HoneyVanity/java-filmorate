package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {
    @PositiveOrZero
    private long id;
    @NonNull
    private final String login;
    private String name;
    @Email
    private final String email;
    @Past
    private final LocalDate birthday;
    private Set<Long> friendsList = new HashSet<>();
}
