package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserService {

    User update(User user);

    User create(User user);

    List<User> getUsers();

    User addFriend(Long friendId, Long id);

    User removeFromFriends(Long friendId, Long id);

    List<User> showFriendsInCommon(Long otherId, Long id);

    User getUser(Long id);

    List<User> getUserFriends(Long id);
}
