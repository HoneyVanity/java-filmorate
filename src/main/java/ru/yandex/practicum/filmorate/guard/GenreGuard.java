package ru.yandex.practicum.filmorate.guard;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.model.Genre;

@Component
@RequiredArgsConstructor
public class GenreGuard extends Guard<Genre> {
    private final GenreDao genreDao;

    @Override
    protected String getGuardClass() {
        return Guard.class.getName();
    }

    @Override
    protected Genre checkMethod(Long id) {
        try {
            return genreDao.getById(id);
        } catch (Exception e) {
            return null;
        }
    }
}
