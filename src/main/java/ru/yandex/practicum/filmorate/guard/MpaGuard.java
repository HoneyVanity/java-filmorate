package ru.yandex.practicum.filmorate.guard;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.MpaDao;
import ru.yandex.practicum.filmorate.model.Mpa;

@Component
@RequiredArgsConstructor
public class MpaGuard extends Guard<Mpa> {
    private final MpaDao mpaDao;

    @Override
    protected String getGuardClass() {
        return Mpa.class.getName();
    }

    @Override
    protected Mpa checkMethod(Long id) {
        try {
            return mpaDao.getById(id);
        } catch (Exception e) {
            return null;
        }
    }
}
