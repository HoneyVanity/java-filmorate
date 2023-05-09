package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exception.EntityAlreadyExistsException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface UserStorage {
    Map<Long, User> getUsers();

    User update(User user);

    Collection<User> findAll();

    User create(User user) throws EntityAlreadyExistsException;

    User getUser(Long id);

    List<User> getUserFriends(Long id);
}
