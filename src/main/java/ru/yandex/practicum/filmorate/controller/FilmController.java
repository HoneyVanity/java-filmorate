package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/films")
public class FilmController {

    FilmStorage storage = new InMemoryFilmStorage();

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        return storage.update(film);
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        return storage.create(film);
    }

    @GetMapping
    public List<Film> getFilms() {
        return storage.getFilms();
    }
}
