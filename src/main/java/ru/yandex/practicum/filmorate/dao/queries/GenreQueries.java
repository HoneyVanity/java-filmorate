package ru.yandex.practicum.filmorate.dao.queries;

public class GenreQueries {

    public static final String GET_ALL = "SELECT * FROM genres ORDER BY genre_id";

    public static final String GET_BY_ID = "SELECT * FROM genres WHERE genre_id = ?";

    public static final String GET_BY_FILM_ID = "SELECT fg.genre_id AS id, g.genre_name AS name " +
            "FROM film_genre AS fg " +
            "JOIN genres AS g ON fg.genre_id = g.genre_id " +
            "WHERE fg.film_id = ? ";
}
