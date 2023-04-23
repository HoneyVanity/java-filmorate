package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

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
    public Film getFilm(@PositiveOrZero @PathVariable Long id) {
        return filmService.getFilm(id);
    }

    @GetMapping("/popular")
    public List<Film> showMostPopularFilms(@RequestParam(defaultValue = "10") Long count) {
        return filmService.showMostPopularFilms(count);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Film removeLike(@PositiveOrZero @PathVariable Long id,
                           @PositiveOrZero @PathVariable Long userId) {
        return filmService.removeLike(id, userId);
    }

    @PutMapping("/{id}/like/{userId}")
    public Film addLike(@PositiveOrZero @PathVariable Long id,
                        @PositiveOrZero @PathVariable Long userId) {
        return filmService.addLike(id, userId);
    }
}
