package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.EntityAlreadyExistsException;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validator.Validator;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Long, Film> films = new HashMap<>();
    private final Validator validator;
    @Setter
    private Long filmId = 1L;

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
        checkIfAlreadyExists(film);
        validator.validateYear(film.getReleaseDate());
        film.setId(filmId++);
        films.put(film.getId(), film);
        log.info("film {} has been created", film.getName());
        return film;
    }

    @Override
    public Film update(Film film) {

        checkIfPresent(film.getId());
        validator.validateYear(film.getReleaseDate());
        films.put(film.getId(), film);
        log.info("film with name {} has been added", film.getName());

        return film;
    }

    @Override
    public Film addLike(Long id, Long userId) {
        checkIfPresent(id);
        films.get(id).getLikes().add(userId);
        return films.get(id);
    }

    @Override
    public Film removeLike(Long id, Long userId) {
        checkIfPresent(id);
        if (!films.get(id).getLikes().contains(userId)) {
            EntityNotFoundException exc = new EntityNotFoundException("Add film first");
            log.warn("Exception generated with message {}", exc.getMessage());
            throw exc;
        }
        films.get(id).getLikes().remove(userId);

        return films.get(id);
    }

    @Override
    public List<Film> showMostPopularFilms(Long count) {

        Comparator<Film> comparator =
                (f1, f2) -> f2.getLikes().size() - f1.getLikes().size();
        return films.values().stream()
                .sorted(comparator)
                .limit(count)
                .collect(Collectors.toList());
    }

    @Override
    public Film getFilm(Long id) {
        checkIfPresent(id);
        return films.get(id);
    }

    private void checkIfAlreadyExists(Film film) throws EntityAlreadyExistsException {
        if (findAll().stream().anyMatch(f ->
                f.getName().equals(film.getName()) &&
                        f.getReleaseDate() == film.getReleaseDate())) {
            EntityAlreadyExistsException exc = new EntityAlreadyExistsException("Film already exists");
            log.warn("Exception generated with message {}", exc.getMessage());
            throw exc;
        }
    }

    private void checkIfPresent(Long id) throws EntityNotFoundException {

        if (!films.containsKey(id)) {
            EntityNotFoundException exc = new EntityNotFoundException("Add film first");
            log.warn("Exception generated with message {}", exc.getMessage());
            throw exc;
        }
    }
}
