package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exception.EntityAlreadyExistsException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;

public interface UserStorage {
    List<User> getUsers();

    User update(User user);

    Collection<User> findAll();

    User create(User user) throws EntityAlreadyExistsException;

    User addFriend(Long friendId, Long id);
    User removeFromFriends(Long friendId, Long id);

    List<User> showFriendsInCommon(Long otherId, Long id);

    User getUser(Long id);

    List<User> getUserFriends(Long id);
}
