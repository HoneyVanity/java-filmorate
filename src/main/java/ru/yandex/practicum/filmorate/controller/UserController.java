package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PutMapping
    public User updateUser(@RequestBody @Valid User user) {
        return userService.update(user);
    }

    @PostMapping
    public User createUser(@RequestBody @Valid User user) {
        return userService.create(user);
    }

    @GetMapping
    public List<User> getUsers() {
        return userService.getUsers();
    }
}
