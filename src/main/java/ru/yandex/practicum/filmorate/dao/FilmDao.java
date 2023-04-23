package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Map;

public interface FilmDao {

    Map<Long, Film> getFilms();

    Collection<Film> findAll();

    Film create(Film film);

    Film update(Film film);

    Film getFilm(Long id);

    void addLike(long filmId, long userId);

    void removeLikeFromFilm(long filmId, long userId);
}
