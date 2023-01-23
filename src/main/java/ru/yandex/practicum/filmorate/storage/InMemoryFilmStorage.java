package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exception.FilmAlreadyExistsException;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validator.Validator;

import java.util.*;

@Slf4j
public class InMemoryFilmStorage implements FilmStorage {

    Map<Integer, Film> films = new HashMap<>();
    Validator validator = new Validator();
    @Setter
    private int filmId = 1;

    @Override
    public List<Film> getFilms() {
        return new ArrayList<>(findAll());
    }

    @Override
    public Collection<Film> findAll() {
        return films.values();
    }

    @Override
    public Film create(Film film) {
        lookUpWhenCreate(film);
        validator.validateYear(film.getReleaseDate());
        film.setId(filmId++);
        films.put(film.getId(), film);
        log.info("film {} has been created", film.getName());
        return film;
    }

    @Override
    public Film update(Film film) {

        lookUpWhenUpdate(film);
        validator.validateYear(film.getReleaseDate());
        films.put(film.getId(), film);
        log.info("film with name {} has been added", film.getName());

        return film;
    }

    private void lookUpWhenCreate(Film film) throws FilmAlreadyExistsException {
        if (findAll().stream().anyMatch(f ->
                f.getName().equals(film.getName()) ||
                        f.getReleaseDate() == film.getReleaseDate())) {
            FilmAlreadyExistsException exc = new FilmAlreadyExistsException("Film already exists");
            log.warn("Exception generated with message {}", exc.getMessage());
            throw exc;
        }
    }

    private void lookUpWhenUpdate(Film film) throws FilmNotFoundException {

        if (!films.containsKey(film.getId())) {
            FilmNotFoundException exc = new FilmNotFoundException("Add film first");
            log.warn("Exception generated with message {}", exc.getMessage());
            throw exc;
        }
    }
}
