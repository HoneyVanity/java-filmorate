package ru.yandex.practicum.filmorate.guard;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.dao.UserDao;

@Component
@RequiredArgsConstructor
public class UserGuard extends Guard<User> {

    private final UserDao userStorage;

    @Override
    protected String getGuardClass() {
        return User.class.getName();
    }

    @Override
    protected User checkMethod(Long id) {
        if (userStorage.findAll().stream().anyMatch(u -> u.getId() == id)) {
            return userStorage.getUser(id);
        }
        return null;
    }
}
