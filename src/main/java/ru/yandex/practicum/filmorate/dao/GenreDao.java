package ru.yandex.practicum.filmorate.dao;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.List;

public interface GenreDao {
    Genre getById(long id);

    Collection<Genre> getAll();

    List<Genre> getByFilmId(int filmId);
}
