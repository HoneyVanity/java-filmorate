package ru.yandex.practicum.filmorate.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.EntityAlreadyExistsException;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exception.FieldValidationException;
import ru.yandex.practicum.filmorate.guard.OptionsOfCheck;
import ru.yandex.practicum.filmorate.guard.UserGuard;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.dao.UserDao;

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class UserServiceImpl implements UserService {

    UserDao userStorage;
    UserGuard userGuard;

    public User update(User user) {

        userGuard.check(user.getId(), OptionsOfCheck.EXISTS);
        return userStorage.update(user);
    }

    private boolean emailIsBusy(String email) {
        return userStorage.findAll().stream()
                .anyMatch(x -> x.getEmail().equals(email));
    }

    public User create(User user) {

        userGuard.check(user.getId(), OptionsOfCheck.PRESENTS);

        if (userStorage.findAll().stream().anyMatch(u ->
                u.getName().equals(user.getName()) &&
                        u.getBirthday() == user.getBirthday())) {
            throw new EntityAlreadyExistsException("User", user.getId());
        }

        if (emailIsBusy(user.getEmail())) {
            throw new FieldValidationException("email", "User with this email is already exists");
        }

        return userStorage.create(user);
    }

    public List<User> getUsers() {
        return new ArrayList<>(userStorage.findAll());
    }

    public User addFriend(Long friendId, Long id) {
        if (id.equals(friendId)) {
            throw new FieldValidationException("friendId", "friendId can not be equal to userId");
        }
        userGuard.check(id, OptionsOfCheck.EXISTS);
        userGuard.check(friendId, OptionsOfCheck.EXISTS);

        userStorage.addFriend(id, friendId);

        log.info("User with id {} added a friend with and id {}",
                id, friendId);
        return userStorage.getUser(id);
    }

    public User removeFromFriends(Long friendId, Long id) {
        userGuard.check(id, OptionsOfCheck.EXISTS);
        userGuard.check(friendId, OptionsOfCheck.EXISTS);
        if (userStorage.getUsers().isEmpty()) {
            EntityNotFoundException exc = new EntityNotFoundException("User", friendId);
            log.warn("Exception generated with message {}", exc.getMessage());
            throw exc;
        }
        userStorage.removeFromFriends(id, friendId);

        log.info("User with id {} removed a friend with id {}",
                id, friendId);
        return userStorage.getUser(id);
    }

    public List<User> showFriendsInCommon(Long otherId, Long id) {
        userGuard.check(id, OptionsOfCheck.EXISTS);
        userGuard.check(otherId, OptionsOfCheck.EXISTS);
        List<User> inCommon = userStorage.showFriendsInCommon(id, otherId);
        log.info("User with id {} and user with id {} have {} friends in common: {}",
                id, otherId,
                inCommon.size(), inCommon);

        return inCommon;
    }

    public User getUser(Long id) {

        userGuard.check(id, OptionsOfCheck.EXISTS);
        return userStorage.getUser(id);
    }

    public List<User> getUserFriends(Long id) {

        userGuard.check(id, OptionsOfCheck.EXISTS);
        return userStorage.getUserFriends(id);
    }
}
