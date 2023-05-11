package ru.yandex.practicum.filmorate.dao.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.MpaDao;
import ru.yandex.practicum.filmorate.dao.queries.MpaQueries;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

@Repository
@RequiredArgsConstructor
@Qualifier
@Slf4j
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class MpaDbDao implements MpaDao {

  JdbcTemplate jdbcTemplate;
    @Override
    public Collection<Mpa> getAll() {
        return jdbcTemplate.query(MpaQueries.GET_ALL, this::mapRowToRate);
    }

    @Override
    public Mpa getById(Long id) {
        try {
            return jdbcTemplate.queryForObject(MpaQueries.GET_BY_ID, this::mapRowToRate, id);
        } catch (DataAccessException exception) {
            return null;
        }
    }

    private Mpa mapRowToRate(ResultSet resultSet, int rowNum) throws SQLException {
        return new Mpa(resultSet.getInt("mpa_id"), resultSet.getString("mpa_name"));
    }
}
