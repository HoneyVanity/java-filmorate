package ru.yandex.practicum.filmorate.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.guard.GenreGuard;
import ru.yandex.practicum.filmorate.guard.OptionsOfCheck;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.Collection;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class GenreServiceImpl implements GenreService {
    GenreDao genreStorage;
    GenreGuard guard;

    @Override
    public Genre getById(Long id) {

        guard.check(id, OptionsOfCheck.EXISTS);
        return genreStorage.getById(id);
    }

    @Override
    public Collection<Genre> getGenres() {

        return genreStorage.getAll();
    }
}
