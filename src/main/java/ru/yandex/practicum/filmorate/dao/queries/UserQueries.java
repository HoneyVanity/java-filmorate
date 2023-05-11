package ru.yandex.practicum.filmorate.dao.queries;

public class UserQueries {

    public static final String GET_ALL = "SELECT * FROM users";

    public static final String GET_BY_ID = "SELECT * FROM users WHERE user_id = ?";

    public static final String UPDATE = "UPDATE users " +
            "SET " +
            "email = ?, " +
            "login = ?, " +
            "name = ?, " +
            "birthday = ? " +
            "WHERE user_id = ?";
    public static final String GET_FRIENDS = "SELECT " +
            "u.user_id, " +
            "u.email, " +
            "u.login, " +
            "u.name, " +
            "u.birthday " +
            "FROM friendship AS f " +
            "JOIN users AS u on u.user_id = f.friend_id " +
            "WHERE f.user_id = ?";

    public static final String GET_COMMON_FRIENDS = "SELECT " +
            "u.user_id, " +
            "u.email, " +
            "u.login, " +
            "u.name, " +
            "u.birthday " +
            "FROM friendship AS f " +
            "JOIN users AS u on u.user_id = f.friend_id " +
            "WHERE f.user_id = ? AND " +
            "f.friend_id IN (" +
            "SELECT friend_id " +
            "FROM friendship " +
            "WHERE user_id = ?)";

    public static final String DELETE_FRIEND = "DELETE " +
            "FROM friendship " +
            "WHERE " +
            "user_id = ? AND friend_id = ? ";
}
