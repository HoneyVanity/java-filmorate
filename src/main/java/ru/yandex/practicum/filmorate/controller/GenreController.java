package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/genres")
public class GenreController {
    private final GenreService genreService;

    @GetMapping("/{id}")
    public Genre getById(@PositiveOrZero @PathVariable Long id) {

        return genreService.getById(id);
    }

    @GetMapping
    public Collection<Genre> getGenres() {

        return genreService.getGenres();
    }
}
