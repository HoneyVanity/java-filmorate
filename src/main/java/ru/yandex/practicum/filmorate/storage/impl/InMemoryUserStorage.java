package ru.yandex.practicum.filmorate.storage.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.EntityAlreadyExistsException;
import ru.yandex.practicum.filmorate.exception.FieldValidationException;
import ru.yandex.practicum.filmorate.guard.OptionsOfCheck;
import ru.yandex.practicum.filmorate.guard.UserGuard;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.validator.Validator;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class InMemoryUserStorage implements UserStorage {
    Map<Long, User> users = new HashMap<>();
    Validator validator;
    @Setter @NonFinal
    Long userId = 1L;

    @Override
    public Map<Long, User> getUsers() {
        return users;
    }

    public Collection<User> findAll() {
        return users.values();
    }

    @Override
    public User update(User user) {
        if (emailIsBusy(user.getEmail())) {
            throw new FieldValidationException("email", "User with this email is already exists");
        }
        validator.validateWhiteSpaces(user.getLogin());
        users.put(user.getId(), replaceNameFieldIfNeeded(user));
        log.info("User with name {} has been updated", user.getName());
        return users.get(user.getId());
    }

    @Override
    public User create(User user) {

        if (findAll().stream().anyMatch(u ->
                u.getName().equals(user.getName()) &&
                        u.getBirthday() == user.getBirthday())) {
            throw new EntityAlreadyExistsException("User", user.getId());
        }

        if (emailIsBusy(user.getEmail())) {
            throw new FieldValidationException("email", "User with this email is already exists");
        }

        validator.validateWhiteSpaces(user.getLogin());
        user.setId(userId++);
        users.put(user.getId(), replaceNameFieldIfNeeded(user));
        log.info("User with name {} and id {} has been created", user.getName(), user.getId());
        return users.get(user.getId());
    }

    @Override
    public User getUser(Long id) {

        log.info("Returned User with name {} and id {}", users.get(id).getName(), id);
        return users.get(id);
    }

    @Override
    public List<User> getUserFriends(Long id) {

        List<User> userFriends = users.get(id).getFriendsList()
                .stream()
                .map(users::get)
                .collect(Collectors.toList());
        log.info("Returned Friends of User with name {} and id {}", users.get(id).getName(), id);
        log.info("Friends listed: " + userFriends);
        return userFriends;
    }

    private User replaceNameFieldIfNeeded(User user) {

        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        return user;
    }

    private boolean emailIsBusy(String email) {
        return findAll()
                .stream()
                .anyMatch(x -> x.getEmail().equals(email));
    }

}
