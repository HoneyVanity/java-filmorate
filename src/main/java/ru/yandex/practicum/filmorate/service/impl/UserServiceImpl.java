package ru.yandex.practicum.filmorate.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exception.FieldValidationException;
import ru.yandex.practicum.filmorate.guard.OptionsOfCheck;
import ru.yandex.practicum.filmorate.guard.UserGuard;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class UserServiceImpl implements UserService {

    UserStorage userStorage;
    UserGuard userGuard;
    @NonFinal
    Map<Long, User> users;

    public User update(User user) {

        userGuard.check(user.getId(), OptionsOfCheck.EXISTS);
        return userStorage.update(user);
    }

    public User create(User user) {

        userGuard.check(user.getId(), OptionsOfCheck.PRESENTS);
        return userStorage.create(user);
    }

    public List<User> getUsers() {
        return new ArrayList<>(userStorage.findAll());
    }

    public User addFriend(Long friendId, Long id) {
        getUsersMap();
        if (id.equals(friendId)) {
            throw new FieldValidationException("friendId", "friendId can not be equal to userId");
        }
        userGuard.check(id, OptionsOfCheck.EXISTS);
        userGuard.check(friendId, OptionsOfCheck.EXISTS);
        users.get(id).getFriendsList().add(friendId);
        users.get(friendId).getFriendsList().add(id);

        log.info("User with name {} and id {} added a friend with name {} and id {}",
                users.get(id).getName(), users.get(id).getId()
                , users.get(friendId).getName(), users.get(friendId).getId());
        return users.get(id);
    }

    public User removeFromFriends(Long friendId, Long id) {
        getUsersMap();
        userGuard.check(id, OptionsOfCheck.EXISTS);
        userGuard.check(friendId, OptionsOfCheck.EXISTS);
        if (!users.get(id).getFriendsList().contains(friendId)) {
            EntityNotFoundException exc = new EntityNotFoundException("User", friendId);
            log.warn("Exception generated with message {}", exc.getMessage());
            throw exc;
        }
        users.get(id).getFriendsList().remove(friendId);

        log.info("User with name {} and id {} removed a friend with name {} and id {}",
                users.get(id).getName(), users.get(id).getId()
                , users.get(friendId).getName(), users.get(friendId).getId());
        return users.get(id);
    }

    public List<User> showFriendsInCommon(Long otherId, Long id) {
        getUsersMap();
        userGuard.check(id, OptionsOfCheck.EXISTS);
        userGuard.check(otherId, OptionsOfCheck.EXISTS);
        Set<Long> otherUserFriends = new HashSet<>(users.get(otherId).getFriendsList());
        otherUserFriends.retainAll(users.get(id).getFriendsList());
        List<User> inCommon = otherUserFriends.stream()
                .map(users::get).collect(Collectors.toList());
        log.info("User with name {} and id {} and user with name {} and id {} have {} friends in common: {}",
                users.get(id).getName(), users.get(id).getId()
                , users.get(otherId).getName(), users.get(otherId).getId(),
                inCommon.size(), inCommon);

        return inCommon;
    }

    public User getUser(Long id) {

        userGuard.check(id, OptionsOfCheck.EXISTS);
        return userStorage.getUser(id);
    }

    private void getUsersMap() {
        users = userStorage.getUsers();
    }

    public List<User> getUserFriends(Long id) {

        userGuard.check(id, OptionsOfCheck.EXISTS);
        return userStorage.getUserFriends(id);
    }

}
