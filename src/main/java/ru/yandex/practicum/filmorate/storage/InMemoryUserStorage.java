package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.EntityAlreadyExistsException;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validator.Validator;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();
    private final Validator validator;
    @Setter
    private Long userId = 1L;

    @Override
    public List<User> getUsers() {
        return new ArrayList<>(findAll());
    }

    public Collection<User> findAll() {
        return users.values();
    }

    @Override
    public User update(User user) {
        checkIfPresent(user.getId());
        validator.validateWhiteSpaces(user.getLogin());
        users.put(user.getId(), checkAndReplaceNameField(user));
        log.info("User with name {} has been updated", user.getName());
        return users.get(user.getId());
    }

    @Override
    public User create(User user) {
        checkIfExists(user);
        validator.validateWhiteSpaces(user.getLogin());
        user.setId(userId++);
        users.put(user.getId(), checkAndReplaceNameField(user));
        log.info("User with name {} and id {} has been created", user.getName(), user.getId());
        return users.get(user.getId());
    }

    @Override
    public User addFriend(Long friendId, Long id) {
        checkIfPresent(id);
        checkIfPresent(friendId);
        users.get(id).getFriendsList().add(friendId);
        users.get(friendId).getFriendsList().add(id);
        System.out.println("user: " + users.get(id).getId() + ""
                + users.get(id).getFriendsList().size());
        System.out.println(users.get(id).getFriendsList());
        return users.get(id);
    }

    @Override
    public User removeFromFriends(Long friendId, Long id) {
        checkIfPresent(id);
        if (!users.get(id).getFriendsList().contains(friendId)) {
            EntityNotFoundException exc = new EntityNotFoundException("Cannot remove friend");
            log.warn("Exception generated with message {}", exc.getMessage());
            throw exc;
        }
        users.get(id).getFriendsList().remove(friendId);
        return users.get(id);
    }

    @Override
    public List<User> showFriendsInCommon(Long otherId, Long id) {
        Set<Long> otherUserFriends = new HashSet<>(users.get(otherId).getFriendsList());
        otherUserFriends.retainAll(users.get(id).getFriendsList());
        return otherUserFriends.stream()
                .map(users::get).collect(Collectors.toList());
    }

    @Override
    public User getUser(Long id) {
        checkIfPresent(id);
        return users.get(id);
    }

    @Override
    public List<User> getUserFriends(Long id) {
        checkIfPresent(id);
        return users.get(id).getFriendsList()
                .stream()
                .map(users::get)
                .collect(Collectors.toList());
    }

    private User checkAndReplaceNameField(User user) {

        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        return user;
    }

    private void checkIfExists(User user) throws EntityAlreadyExistsException {
        if (findAll().stream().anyMatch(u ->
                u.getName().equals(user.getName()) &&
                        u.getBirthday() == user.getBirthday())) {
            EntityAlreadyExistsException exc = new EntityAlreadyExistsException("User already exists");
            log.warn("Exception generated with message {}", exc.getMessage());
            throw exc;
        }
    }

    private void checkIfPresent(Long id) throws EntityNotFoundException {

        if (!users.containsKey(id)) {
            EntityNotFoundException exc = new EntityNotFoundException("Add user first");
            log.warn("Exception generated with message {}", exc.getMessage());
            throw exc;
        }
    }
}
