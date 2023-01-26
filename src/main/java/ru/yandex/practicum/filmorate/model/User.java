package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
public class User {
    @PositiveOrZero
    private int id;
    @NonNull
    private String login;
    private String name;
    @Email
    private String email;
    @Past
    private LocalDate birthday;

    public User(@NonNull String login, String name, @Email String email, @Past LocalDate birthday) {
        this.login = login;
        this.name = name;
        this.email = email;
        this.birthday = birthday;
    }


}
