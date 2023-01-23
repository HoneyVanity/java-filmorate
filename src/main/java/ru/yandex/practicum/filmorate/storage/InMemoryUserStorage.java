package ru.yandex.practicum.filmorate.storage;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.UserAlreadyExistsException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validator.Validator;

import java.util.*;

@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();
    Validator validator = new Validator();
    @Setter
    private int userId = 1;

    @Override
    public List<User> getUsers() {
        return new ArrayList<>(findAll());
    }

    public Collection<User> findAll() {
        return users.values();
    }

    @Override
    public User update(User user) {
        lookUpWhenUpdate(user);
        validator.validateWhiteSpaces(user.getLogin());
        users.put(user.getId(), checkAndReplaceNameField(user));
        log.info("User with name {} has been updated", user.getName());
        return user;
    }

    @Override
    public User create(User user) {
        lookUpWhenCreate(user);
        validator.validateWhiteSpaces(user.getLogin());
        user.setId(userId++);
        users.put(user.getId(), checkAndReplaceNameField(user));
        log.info("User with name {} has been created", user.getName());
        return user;
    }

    private User checkAndReplaceNameField(User user) {

        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        return user;
    }

    private void lookUpWhenCreate(User user) throws UserAlreadyExistsException {
        if (findAll().stream().anyMatch(u ->
                u.getName().equals(user.getName()) ||
                        u.getBirthday() == user.getBirthday())) {
            UserAlreadyExistsException exc = new UserAlreadyExistsException("User already exists");
            log.warn("Exception generated with message {}", exc.getMessage());
            throw exc;
        }
    }

    private void lookUpWhenUpdate(User user) throws UserNotFoundException {

        if (!users.containsKey(user.getId())) {
            UserNotFoundException exc = new UserNotFoundException("Add user first");
            log.warn("Exception generated with message {}", exc.getMessage());
            throw exc;
        }
    }
}
