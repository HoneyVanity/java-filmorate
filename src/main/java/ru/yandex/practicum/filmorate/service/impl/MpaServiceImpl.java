package ru.yandex.practicum.filmorate.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.MpaDao;
import ru.yandex.practicum.filmorate.guard.MpaGuard;
import ru.yandex.practicum.filmorate.guard.OptionsOfCheck;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.Collection;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class MpaServiceImpl implements MpaService {

    MpaDao mpaDao;
    MpaGuard guard;

    @Override
    public Collection<Mpa> getMpa() {
        return mpaDao.getAll();
    }

    @Override
    public Mpa getById(Long id) {
        guard.check(id, OptionsOfCheck.EXISTS);
        return mpaDao.getById(id);
    }
}
