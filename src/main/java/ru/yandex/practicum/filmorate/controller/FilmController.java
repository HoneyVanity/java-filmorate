package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.EntityAlreadyExistsException;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import javax.validation.ValidationException;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        return filmService.update(film);
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        return filmService.create(film);
    }

    @GetMapping
    public List<Film> getFilms() {
        return filmService.getFilms();
    }

    @GetMapping("/{id}")
    public Film getFilm(@Valid @PathVariable Long id) {
        return filmService.getFilm(id);
    }

    @GetMapping("/popular")
    public List<Film> showMostPopularFilms(@RequestParam(defaultValue = "10") Long count) {
        return filmService.showMostPopularFilms(count);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Film removeLike(@Valid @PathVariable Long id, @PositiveOrZero @PathVariable Long userId) {
        return filmService.removeLike(id, userId);
    }

    @PutMapping("/{id}/like/{userId}")
    public Film addLike(@Valid @PathVariable Long id, @PositiveOrZero @PathVariable Long userId) {
        return filmService.addLike(id, userId);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleNullPointerExc(final EntityNotFoundException e) {
        return Map.of("error", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleNullPointerExc(final EntityAlreadyExistsException e) {
        return Map.of("error", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleNullPointerExc(final ValidationException e) {
        return Map.of("error", e.getMessage());
    }
}
