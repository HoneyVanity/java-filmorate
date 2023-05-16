package ru.yandex.practicum.filmorate.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.EntityAlreadyExistsException;
import ru.yandex.practicum.filmorate.guard.FilmGuard;
import ru.yandex.practicum.filmorate.guard.OptionsOfCheck;
import ru.yandex.practicum.filmorate.guard.UserGuard;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.dao.FilmDao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class FilmServiceImpl implements FilmService {

    FilmDao filmDao;
    FilmGuard filmGuard;
    UserGuard userGuard;

    public List<Film> getFilms() {
        return new ArrayList<>(filmDao.findAll());
    }

    public Film create(Film film) {
        checkIfExistsByFields(film);
        filmGuard.check(film.getId(), OptionsOfCheck.PRESENTS);
        return filmDao.create(film);

    }

    public Film update(Film film) {
        filmGuard.check(film.getId(), OptionsOfCheck.EXISTS);
        return filmDao.update(film);
    }

    public Film addLike(Long id, Long userId) {
        filmGuard.check(id, OptionsOfCheck.EXISTS);
        filmDao.addLike(id, userId);

        log.info("User with id {} added like to the film with id {}",
                userId, id);
        return filmDao.getFilm(id);
    }

    public Film removeLike(Long id, Long userId) {
        filmGuard.check(id, OptionsOfCheck.EXISTS);
        userGuard.check(userId, OptionsOfCheck.EXISTS);
        filmDao.removeLikeFromFilm(id, userId);
        log.info("User with id {} removed like to the film with id {}",
                userId, id);
        return filmDao.getFilm(id);
    }

    public List<Film> showMostPopularFilms(Long count) {

        Collection<Film> mostPopularFilms = filmDao.getPopularFilms(count);
        log.info("most popular films are: {}", mostPopularFilms);

        return new ArrayList<>(mostPopularFilms);
    }

    public Film getFilm(Long id) {

        filmGuard.check(id, OptionsOfCheck.EXISTS);
        return filmDao.getFilm(id);
    }

    private void checkIfExistsByFields(Film film) {
        if (filmDao.findAll().stream().anyMatch(f ->
                f.getName().equals(film.getName()) &&
                        f.getReleaseDate() == film.getReleaseDate())) {
            throw new EntityAlreadyExistsException("Film", film.getId());
        }
    }
}

