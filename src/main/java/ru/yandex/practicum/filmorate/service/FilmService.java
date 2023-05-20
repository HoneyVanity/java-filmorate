package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmService {
    List<Film> getFilms();

    Film create(Film film);

    Film update(Film film);

    Film addLike(Long id, Long userId);

    Film removeLike(Long id, Long userId);

    List<Film> showMostPopularFilms(Long count);

    Film getFilm(Long id);

}
