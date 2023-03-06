package ru.yandex.practicum.filmorate.storage.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.EntityAlreadyExistsException;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.validator.Validator;

import java.util.*;

@Component
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class InMemoryFilmStorage implements FilmStorage {
    Map<Long, Film> films = new HashMap<>();

    Validator validator;
    @Setter @NonFinal
    Long filmId = 1L;

    @Override
    public Map<Long, Film> getFilms() {
        return films;
    }

    @Override
    public Collection<Film> findAll() {
        return films.values();
    }

    @Override
    public Film create(Film film) {

        checkIfExistsByFields(film);
        validator.validateYear(film.getReleaseDate());
        film.setId(filmId++);
        films.put(film.getId(), film);
        log.info("film {} has been created", film.getName());
        return film;
    }

    @Override
    public Film update(Film film) {

        validator.validateYear(film.getReleaseDate());
        films.put(film.getId(), film);
        log.info("film with name {} has been added", film.getName());
        return film;
    }

    @Override
    public Film getFilm(Long id) {

        return films.get(id);
    }

    private void checkIfExistsByFields(Film film) throws EntityAlreadyExistsException {
        if (findAll().stream().anyMatch(f ->
                f.getName().equals(film.getName()) &&
                        f.getReleaseDate() == film.getReleaseDate())) {
            throw new EntityAlreadyExistsException("Film", film.getId());
        }
    }
}
