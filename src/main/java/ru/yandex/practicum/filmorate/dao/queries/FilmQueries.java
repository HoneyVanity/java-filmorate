package ru.yandex.practicum.filmorate.dao.queries;

public class FilmQueries {

    public static final String GET_ALL = "SELECT * " +
            "FROM films AS f " +
            "JOIN mpa AS m ON f.mpa_id = m.mpa_id";
    public static final String GET_BY_ID = "SELECT * " +
            "FROM films AS f " +
            "JOIN mpa AS m ON f.mpa_id = m.mpa_id " +
            "WHERE f.film_id = ?";

    public static final String UPDATE = "UPDATE films " +
            "SET " +
            "name = ?, " +
            "description = ?, " +
            "release_date = ?, " +
            "duration = ?, " +
            "mpa_id = ? " +
            "WHERE film_id = ?";

    public static final String GET_POPULAR_FILMS =
            "COUNT(l.film_id) AS likes " +
                    "FROM films AS f " +
                    "INNER JOIN mpa AS m ON f.mpa_id = m.mpa_id " +
                    "LEFT JOIN likes AS l ON f.film_id = l.film_id " +
                    "GROUP BY f.film_id, f.name, f.description, f.release_date, f.duration, l.user_id, m.mpa_id, m.mpa_name " +
                    "ORDER BY likes DESC, f.name LIMIT ?";

    public static final String DELETE_FILM_GENRES = "DELETE FROM film_genre WHERE film_id = ?";

    public static final String ADD_LIKE = "INSERT INTO likes VALUES(?, ?)";

    public static final String REMOVE_LIKE_FROM_FILM = "DELETE FROM likes WHERE film_id = ? AND user_id = ?";

    public static final String ADD_GENRE = "INSERT INTO film_genre VALUES(?, ?)";

}

