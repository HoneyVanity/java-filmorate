package ru.yandex.practicum.filmorate.dao.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.dao.queries.FilmQueries;
import ru.yandex.practicum.filmorate.exception.EntityAlreadyExistsException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.validator.Validator;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Qualifier
@Repository
@RequiredArgsConstructor
@Primary
@Slf4j
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class FilmDbDao implements FilmDao {
    JdbcTemplate jdbcTemplate;
    GenreDao genreDao;
    Validator validator;

    @Override
    public Map<Long, Film> getFilms() {
        return findAll().stream()
                .collect(Collectors.toMap(Film::getId, Function.identity()));
    }

    @Override
    public Collection<Film> findAll() {
        return jdbcTemplate.query(FilmQueries.GET_ALL, this::mapRowToFilm);
    }

    @Override
    public Film create(Film film) {
        checkIfExistsByFields(film);
        validator.validateYear(film.getReleaseDate());
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("film")
                .usingGeneratedKeyColumns("film_id");

        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("name", film.getName())
                .addValue("description", film.getDescription())
                .addValue("release_date", film.getReleaseDate())
                .addValue("duration", film.getDuration())
                .addValue("mpa_id", film.getMpa().getId());
        long filmId = simpleJdbcInsert.executeAndReturnKey(parameters).longValue();

        updateFilmGenres(film.getGenres(), filmId);
        updateFilmLikes(film.getLikes(), filmId);
        log.info("film {} has been created", film.getName());
        return getFilm(filmId);
    }


    @Override
    public Film update(Film film) {
        validator.validateYear(film.getReleaseDate());
        jdbcTemplate.update(
                FilmQueries.UPDATE,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId()
        );

        jdbcTemplate.update(FilmQueries.DELETE_FILM_GENRES, film.getId());
        updateFilmGenres(film.getGenres(), film.getId());
        updateFilmLikes(film.getLikes(), film.getId());

        log.info("film with name {} has been added", film.getName());
        return getFilm(film.getId());
    }

    @Override
    public Film getFilm(Long id) {
        try {
            return jdbcTemplate.queryForObject(FilmQueries.GET_BY_ID, this::mapRowToFilm, id);
        } catch (DataAccessException exception) {
            return null;
        }
    }

    private Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {
        return Film.builder()
                .id(resultSet.getLong("film_id"))
                .name(resultSet.getString("name"))
                .description(resultSet.getString("description"))
                .releaseDate(resultSet.getDate("release_date").toLocalDate())
                .duration(resultSet.getInt("duration"))
                .mpa(new Mpa(resultSet.getInt("mpa_id"), resultSet.getString("mpa.name")))
                .likes(Collections.singleton(resultSet.getLong("film_id")))
                .genres(genreDao.getByFilmId(resultSet.getInt("film_id")))
                .build();
    }

    private void updateFilmGenres(List<Genre> genres, long filmId) {
        if (genres == null) {
            return;
        }
        genres.forEach(x -> jdbcTemplate.update(FilmQueries.ADD_GENRE, filmId, x));
    }

    private void updateFilmLikes(Set<Long> likes, long filmId) {

        if (likes == null) {
            return;
        }
        likes.forEach(x -> jdbcTemplate.update(FilmQueries.LIKE_FILM, filmId, x));
    }

    @Override
    public void addLike(long filmId, long userId) {
        jdbcTemplate.update(FilmQueries.LIKE_FILM, filmId, userId);
    }

    @Override
    public void removeLikeFromFilm(long filmId, long userId) {
        jdbcTemplate.update(FilmQueries.DELETE_LIKE_FROM_FILM, filmId, userId);
    }

    private void checkIfExistsByFields(Film film) throws EntityAlreadyExistsException {
        if (findAll().stream().anyMatch(f ->
                f.getName().equals(film.getName()) &&
                        f.getReleaseDate() == film.getReleaseDate())) {
            throw new EntityAlreadyExistsException("Film", film.getId());
        }
    }
}
