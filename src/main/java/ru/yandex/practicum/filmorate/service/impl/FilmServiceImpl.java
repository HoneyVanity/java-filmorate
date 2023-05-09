package ru.yandex.practicum.filmorate.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.guard.FilmGuard;
import ru.yandex.practicum.filmorate.guard.OptionsOfCheck;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class FilmServiceImpl implements FilmService {

    FilmStorage filmStorage;
    FilmGuard filmGuard;
    @NonFinal
    Map<Long, Film> films;

    public List<Film> getFilms() {
        return new ArrayList<>(filmStorage.findAll());
    }

    public Film create(Film film) {

        filmGuard.check(film.getId(), OptionsOfCheck.PRESENTS);
        return filmStorage.create(film);
    }

    public Film update(Film film) {
        filmGuard.check(film.getId(), OptionsOfCheck.EXISTS);
        return filmStorage.update(film);
    }

    public Film addLike(Long id, Long userId) {
        updateFilmsField();
        filmGuard.check(id, OptionsOfCheck.EXISTS);
        films.get(id).getLikes().add(userId);

        log.info("User with id {} added like to the film with name {} and id {}",
                userId,
                films.get(id).getName(), films.get(id).getId());
        return films.get(id);
    }

    public Film removeLike(Long filmId, Long userId) {
        updateFilmsField();
        filmGuard.check(filmId, OptionsOfCheck.EXISTS);
        if (!films.get(filmId).getLikes().contains(userId)) {
            throw new EntityNotFoundException("User", userId);
        }
        films.get(filmId).getLikes().remove(userId);
        log.info("User with id {} removed like to the film with name {} and id {}",
                userId,
                films.get(filmId).getName(), films.get(filmId).getId());
        return films.get(filmId);
    }

    public List<Film> showMostPopularFilms(Long count) {
        updateFilmsField();
        Comparator<Film> comparator =
                (f1, f2) -> f2.getLikes().size() - f1.getLikes().size();

        List<Film> mostPopularFilms = films.values().stream()
                .sorted(comparator)
                .limit(count)
                .collect(Collectors.toList());

        log.info("{} most popular films are: {}", count, mostPopularFilms);

        return mostPopularFilms;
    }

    public Film getFilm(Long id) {

        filmGuard.check(id, OptionsOfCheck.EXISTS);
        return filmStorage.getFilm(id);
    }

    private void updateFilmsField() {
        films = filmStorage.getFilms();
    }

}
