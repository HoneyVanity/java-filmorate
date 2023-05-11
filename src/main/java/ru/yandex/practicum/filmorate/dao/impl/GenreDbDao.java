package ru.yandex.practicum.filmorate.dao.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.dao.queries.GenreQueries;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

@Repository
@Qualifier
@Primary
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class GenreDbDao implements GenreDao {

    JdbcTemplate jdbcTemplate;

    @Override
    public Genre getById(long id) {
        try {
            return jdbcTemplate.queryForObject(GenreQueries.GET_BY_ID, this::mapRowToGenre, id);
        } catch (DataAccessException exception) {
            return null;
        }
    }

    @Override
    public Collection<Genre> getAll() {
        return jdbcTemplate.query(GenreQueries.GET_ALL, this::mapRowToGenre);
    }

    @Override
    public List<Genre> getByFilmId(int filmId) {
        try {
            return jdbcTemplate.query(GenreQueries.GET_BY_FILM_ID, this::mapRowToGenre, filmId);
        } catch (DataAccessException exception) {
            return null;
        }
    }

    private Genre mapRowToGenre(ResultSet resultSet, int rowNum) throws SQLException {
        return new Genre(resultSet.getInt("genre_id"), resultSet.getString("genre_name"));
    }
}
