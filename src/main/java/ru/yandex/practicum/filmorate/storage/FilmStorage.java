package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;

public interface FilmStorage {

    List<Film> getFilms();

    Collection<Film> findAll();

    Film create(Film film);

    Film update(Film film);
}
