package ru.yandex.practicum.filmorate.dao.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.dao.queries.FilmQueries;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.validator.Validator;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
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
    public Collection<Film> findAll() {
        Collection<Film> films = jdbcTemplate.query(FilmQueries.GET_ALL, this::mapRowToFilmWithoutGenres);
        if (!films.isEmpty()) {
            List<Map<Long, Genre>> genres = getGenresWithFilmIdAsMapKey();
            films.forEach(film -> {
                        if (genres.stream().anyMatch(map -> map.containsKey(film.getId()))) {

                            film.setGenres(
                                    genres.stream()
                                            .map(value -> value.get(film.getId()))
                                            .collect(Collectors.toList()));
                        }
                    });
        }
        return films;
    }

    @Override
    public Film create(Film film) {
        validator.validateYear(film.getReleaseDate());
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("films")
                .usingGeneratedKeyColumns("film_id");
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("name", film.getName())
                .addValue("description", film.getDescription())
                .addValue("release_date", film.getReleaseDate())
                .addValue("duration", film.getDuration())
                .addValue("mpa_id", film.getMpa().getId());
        long filmId = simpleJdbcInsert.executeAndReturnKey(parameters).longValue();

        updateFilmGenres(film.getGenres(), filmId);

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
                .mpa(new Mpa(resultSet.getInt("mpa_id"), resultSet.getString("mpa_name")))
                .genres((genreDao.getByFilmId(resultSet.getInt("film_id"))))
                .build();
    }

    private Film mapRowToFilmWithoutGenres(ResultSet resultSet, int rowNum) throws SQLException {

        return Film.builder()
                .id(resultSet.getLong("film_id"))
                .name(resultSet.getString("name"))
                .description(resultSet.getString("description"))
                .releaseDate(resultSet.getDate("release_date").toLocalDate())
                .duration(resultSet.getInt("duration"))
                .mpa(new Mpa(resultSet.getInt("mpa_id"), resultSet.getString("mpa_name")))
                .genres(Collections.emptyList())
                .build();
    }

    private List<Map<Long, Genre>> getGenresWithFilmIdAsMapKey() {
        return jdbcTemplate.query(FilmQueries.GET_GENRES, this::mapRowToGenresWithFilmId);
    }

    private Map<Long, Genre> mapRowToGenresWithFilmId(ResultSet resultSet, int rowNum) throws SQLException {
        Long film_id = resultSet.getLong("film_id");
        int genre_id = resultSet.getInt("genre_id");
        String genre_name = resultSet.getString("genre_name");

        return Map.of(film_id, new Genre(genre_id, genre_name));
    }

    private void updateFilmGenres(List<Genre> genres, long filmId) {
        if (genres == null) {
            return;
        }

        List<Integer> genreUniqueIds = genres.stream()
                .map(Genre::getId)
                .distinct()
                .collect(Collectors.toList());

        jdbcTemplate.batchUpdate(
                FilmQueries.ADD_GENRE,
                new BatchPreparedStatementSetter() {
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        int genreId = genreUniqueIds.get(i);
                        ps.setLong(1, filmId);
                        ps.setLong(2, genreId);
                    }

                    public int getBatchSize() {
                        return genreUniqueIds.size();
                    }
                });
    }

    @Override
    public void addLike(long filmId, long userId) {
        jdbcTemplate.update(FilmQueries.ADD_LIKE, filmId, userId);
    }

    @Override
    public Collection<Film> getPopularFilms(long count) {
        try {
            return jdbcTemplate.query(FilmQueries.GET_POPULAR_FILMS, this::mapRowToFilm, count);

        } catch (DataAccessException exception) {
            return null;
        }
    }

    @Override
    public void removeLikeFromFilm(long filmId, long userId) {
        jdbcTemplate.update(FilmQueries.REMOVE_LIKE_FROM_FILM, filmId, userId);
    }
}


