package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.EntityAlreadyExistsException;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import javax.validation.ValidationException;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

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
    public User getUser(@Valid @PathVariable Long id) {
        return userService.getUser(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public User addFriend(@Valid @PathVariable Long id, @PositiveOrZero @PathVariable Long friendId) {
        return userService.addFriend(friendId, id);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public User removeFromFriends(@PositiveOrZero @PathVariable Long friendId, @Valid @PathVariable Long id) {
        return userService.removeFromFriends(friendId, id);
    }

    @GetMapping("/{id}/friends")
    public List<User> getUserFriends(@PositiveOrZero @PathVariable Long id) {
        return userService.getUserFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> showFriendsInCommon(@PositiveOrZero @PathVariable Long otherId, @Valid @PathVariable Long id) {
        return userService.showFriendsInCommon(otherId, id);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleNullPointerExc(final EntityNotFoundException e) {
        return Map.of("error", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleNullPointerExc(final EntityAlreadyExistsException e) {
        return Map.of("error", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleNullPointerExc(final ValidationException e) {
        return Map.of("error", e.getMessage());
    }
}
