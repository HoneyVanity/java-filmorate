package ru.yandex.practicum.filmorate.guard;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.dao.FilmDao;

@Component
@RequiredArgsConstructor
public class FilmGuard extends Guard<Film> {
    private final FilmDao filmDao;

    @Override
    protected String getGuardClass() {
        return Film.class.getName();
    }

    @Override
    protected Film checkMethod(Long id) {
        try {
            return filmDao.getFilm(id);
        } catch (Exception e) {
            return null;
        }
    }
}
