package ru.yandex.practicum.filmorate.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.guard.FilmGuard;
import ru.yandex.practicum.filmorate.guard.OptionsOfCheck;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.dao.FilmDao;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class FilmServiceImpl implements FilmService {

    FilmDao filmStorage;
    FilmGuard filmGuard;

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
        filmGuard.check(id, OptionsOfCheck.EXISTS);
        filmStorage.addLike(id, userId);

        log.info("User with id {} added like to the film with id {}",
                userId, id);
        return filmStorage.getFilm(id);
    }

    public Film removeLike(Long id, Long userId) {
        filmGuard.check(id, OptionsOfCheck.EXISTS);
        if (filmStorage.getFilm(id).getLikes().contains(userId)) {
            throw new EntityNotFoundException("User", userId);
        }
        filmStorage.removeLikeFromFilm(id, userId);
        log.info("User with id {} removed like to the film with id {}",
                userId, id);
        return filmStorage.getFilm(id);
    }

    public List<Film> showMostPopularFilms(Long count) {
        Comparator<Film> comparator =
                (f1, f2) -> f2.getLikes().size() - f1.getLikes().size();

        List<Film> mostPopularFilms = filmStorage.findAll()
                .stream()
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

}
