package ru.yandex.practicum.filmorate;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.dao.MpaDao;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.time.Month;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
class FilmorateApplicationTests {
    UserDao userDao;
    FilmDao filmDao;
    MpaDao mpaDao;
    GenreDao genreDao;

    @Test
    public void testGetFilmAndUser() {
        Optional<Film> filmOptional = Optional.ofNullable(filmDao.getFilm(1L));

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("id", 1)
                );

        Optional<User> userOptional = Optional.ofNullable(userDao.getUser(1L));

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1L)
                );
    }

    @Test
    public void testGetMpa() {
        Optional<Mpa> mpaOptional = Optional.ofNullable(mpaDao.getById(1L));

        assertThat(mpaOptional)
                .isPresent()
                .hasValueSatisfying(mpa ->
                        assertThat(mpa).hasFieldOrPropertyWithValue("id", 1)
                );
    }

    @Test
    public void testGetGenre() {
        Optional<Genre> genreOptional = Optional.ofNullable(genreDao.getById(1L));

        assertThat(genreOptional)
                .isPresent()
                .hasValueSatisfying(genre ->
                        assertThat(genre).hasFieldOrPropertyWithValue("id", 1)
                );
    }

    @Test
    public void testCreateUser() {
        Optional<User> userOptional = Optional.ofNullable(userDao.create(
                User.builder()
                        .login("new_filmorate_user")
                        .name("new_user_name")
                        .birthday(LocalDate.of(1994, Month.APRIL, 7))
                        .email("something@icloud.com")
                        .build()
        ));
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(genre ->
                        assertThat(genre).hasFieldOrPropertyWithValue("login", "new_filmorate_user")
                );
    }

    @Test
    public void testCreateFilm() {
        Optional<Film> filmOptional = Optional.ofNullable(filmDao.create(
                Film.builder()
                        .name("New filmorate film")
                        .description("description")
                        .duration(90)
                        .releaseDate(LocalDate.of(1990, Month.AUGUST, 8))
                        .mpa(new Mpa(1, "testMPA"))
                        .build()
        ));
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("name", "New filmorate film")
                );
    }
}
