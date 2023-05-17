package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.exception.EntityAlreadyExistsException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface UserDao {
    Map<Long, User> getUsers();

    User update(User user);

    Collection<User> findAll();

    User create(User user) throws EntityAlreadyExistsException;

    User getUser(Long id);

    List<User> getUserFriends(Long id);

    List<User> showFriendsInCommon(long userId, long otherUserId);

    void addFriend(long userId, long friendId);

    void removeFromFriends(long userId, long friendId);
}
