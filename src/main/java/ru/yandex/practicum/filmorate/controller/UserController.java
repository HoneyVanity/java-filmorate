package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    UserStorage storage = new InMemoryUserStorage();

    @PutMapping
    public User updateUser(@RequestBody @Valid User user) {
        return storage.update(user);
    }

    @PostMapping
    public User createUser(@RequestBody @Valid User user) {
        return storage.create(user);
    }

    @GetMapping
    public List<User> getUsers() {
        return storage.getUsers();
    }
}
