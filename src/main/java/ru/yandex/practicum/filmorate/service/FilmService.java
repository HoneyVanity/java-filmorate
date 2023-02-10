package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FilmService {

    private final FilmStorage filmStorage;

    public List<Film> getFilms() {
        return filmStorage.getFilms();
    }

    public Film create(Film film) {
        return filmStorage.create(film);
    }

    public Film update(Film film) {
        return filmStorage.update(film);
    }

    public Film addLike(Long id, Long userId) {
        return filmStorage.addLike(id, userId);
    }

    public Film removeLike(Long id, Long userId) {
        return filmStorage.removeLike(id, userId);
    }

    public List<Film> showMostPopularFilms(Long count) {
        return filmStorage.showMostPopularFilms(count);
    }

    public Film getFilm(Long id) {
        return filmStorage.getFilm(id);
    }
}
