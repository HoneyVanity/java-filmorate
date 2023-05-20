package ru.yandex.practicum.filmorate;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class ValidationTest {
    Validator validator;
    ru.yandex.practicum.filmorate.validator.Validator customValidator;
    Film film;
    User user;

    @BeforeEach
    public void setUp() {
        user = User.builder()
                .id(1L)
                .name("")
                .login("login with white spaces")
                .birthday(LocalDate.of(1994, 7, 7))
                .email("wrong.com")
                .build();

        film = Film.builder()
                .id(1L)
                .name("Peripheral")
                .duration(90)
                .description("Flynne Fisher is a brilliant gamer who works a dead-end job to support her brother and ailing mother. " +
                        "When her brother enlists her help in an advanced game, " +
                        "Flynne sees something she shouldn't, bringing danger to the family's doorstep.")
                .build();

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        customValidator = new ru.yandex.practicum.filmorate.validator.Validator();
    }

    @Test
    @DisplayName("Checks if release date is after than 28 of December of 1895")
    public void checkForYearIsAfterThanEarliestAllowed() {

        film = Film.builder().releaseDate(LocalDate.of(1894, 12, 28))
                .description("short description")
                .build();
        Assertions.assertThrows(javax.validation.ValidationException.class,
                () -> customValidator.validateYear(film.getReleaseDate()));

    }

    @Test
    @DisplayName("Checks if name contains white spaces")
    public void checkForNameContainsWhiteSpaces() {
        Assertions.assertThrows(javax.validation.ValidationException.class,
                () -> customValidator.validateWhiteSpaces(user.getLogin()));
    }

    @Test
    @DisplayName("Film Validator Annotation violations")
    public void checkFilmViolations() {
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("User Validator Annotation violations")
    public void checkUserViolations() {
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

}
