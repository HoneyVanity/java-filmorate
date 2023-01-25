package ru.yandex.practicum.filmorate.storage;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.EntityAlreadyExistsException;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validator.Validator;

import java.util.*;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();
    private final Validator validator;
    @Setter
    private int userId = 1;

    @Autowired
    public InMemoryUserStorage(Validator validator) {
        this.validator = validator;
    }

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

    private void lookUpWhenCreate(User user) throws EntityAlreadyExistsException {
        if (findAll().stream().anyMatch(u ->
                u.getName().equals(user.getName()) &&
                        u.getBirthday() == user.getBirthday())) {
            EntityAlreadyExistsException exc = new EntityAlreadyExistsException("User already exists");
            log.warn("Exception generated with message {}", exc.getMessage());
            throw exc;
        }
    }

    private void lookUpWhenUpdate(User user) throws EntityNotFoundException {

        if (!users.containsKey(user.getId())) {
            EntityNotFoundException exc = new EntityNotFoundException("Add user first");
            log.warn("Exception generated with message {}", exc.getMessage());
            throw exc;
        }
    }
}
