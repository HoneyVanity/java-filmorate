package ru.yandex.practicum.filmorate.guard;

import lombok.RequiredArgsConstructor;
import ru.yandex.practicum.filmorate.exception.EntityAlreadyExistsException;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;

@RequiredArgsConstructor
public abstract class Guard<T> {
    protected abstract String getGuardClass();

    protected abstract T checkMethod(Long id);

    public void check(Long id, OptionsOfCheck option) {
        T entity = checkMethod(id);
        switch (option) {
            case EXISTS:
                if (entity == null) {
                    throw new EntityNotFoundException(getGuardClass(), id);
                }
                break;
            case PRESENTS:
                if (entity != null) {
                    throw new EntityAlreadyExistsException(getGuardClass(), id);
                }
                break;
            default:
                break;
        }
    }
}
