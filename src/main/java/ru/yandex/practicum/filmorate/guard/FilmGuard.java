package ru.yandex.practicum.filmorate.guard;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

@Component
@RequiredArgsConstructor
public class FilmGuard extends Guard<Film> {
    private final FilmStorage filmStorage;

    @Override
    protected String getGuardClass() {
        return Film.class.getName();
    }

    @Override
    protected Film checkMethod(Long id) {
        if (filmStorage.findAll().stream().anyMatch(f -> f.getId() == id)) {
            return filmStorage.getFilm(id);
        }
        return null;
    }
}
