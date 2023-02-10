package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserStorage userStorage;

    public User update(User user) {
        return userStorage.update(user);
    }

    public User create(User user) {
        return userStorage.create(user);
    }

    public List<User> getUsers() {
        return userStorage.getUsers();
    }

    public User addFriend(Long friendId, Long id) {
        return userStorage.addFriend(friendId, id);
    }

    public User removeFromFriends(Long friendId, Long id) {
        return userStorage.removeFromFriends(friendId, id);
    }

    public List<User> showFriendsInCommon(Long otherId, Long id) {
        return userStorage.showFriendsInCommon(otherId, id);
    }

    public User getUser(Long id) {
        return userStorage.getUser(id);
    }

    public List<User> getUserFriends(Long id) {
        return userStorage.getUserFriends(id);
    }
}
