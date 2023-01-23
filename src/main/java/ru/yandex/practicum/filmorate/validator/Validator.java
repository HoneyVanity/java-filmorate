package ru.yandex.practicum.filmorate.validator;

import lombok.extern.slf4j.Slf4j;

import javax.validation.ValidationException;
import java.time.LocalDate;

@Slf4j
public class Validator {

    private final static LocalDate FIRST_FILM_DATE =
            LocalDate.of(1895, 12, 28);

    public void validateYear(LocalDate date) {
        if (date.isBefore(FIRST_FILM_DATE)) {
            ValidationException exc = new ValidationException("Date should be after 28.12.1895");
            log.warn("Exception generated with message {}", exc.getMessage());
            throw exc;
        }
    }

    public void validateWhiteSpaces(String str) {
        if (str.contains(" ")) {
            ValidationException exc = new ValidationException("Must not include white spaces");
            log.warn("Exception generated with message {}", exc.getMessage());
            throw exc;
        }
    }

}
