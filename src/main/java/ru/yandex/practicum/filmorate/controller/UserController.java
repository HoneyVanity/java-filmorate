package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.impl.UserServiceImpl;

import javax.validation.Valid;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserServiceImpl userService;

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

    @GetMapping("/{id}")
    public User getUser(@PositiveOrZero @PathVariable Long id) {
        return userService.getUser(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public User addFriend(@PositiveOrZero @PathVariable Long id,
                          @PositiveOrZero @PathVariable Long friendId) {
        return userService.addFriend(friendId, id);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public User removeFromFriends(@PositiveOrZero @PathVariable Long friendId,
                                  @PositiveOrZero @PathVariable Long id) {
        return userService.removeFromFriends(friendId, id);
    }

    @GetMapping("/{id}/friends")
    public List<User> getUserFriends(@PositiveOrZero @PathVariable Long id) {
        return userService.getUserFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> showFriendsInCommon(@PositiveOrZero @PathVariable Long otherId,
                                          @PositiveOrZero @PathVariable Long id) {
        return userService.showFriendsInCommon(otherId, id);
    }

}
