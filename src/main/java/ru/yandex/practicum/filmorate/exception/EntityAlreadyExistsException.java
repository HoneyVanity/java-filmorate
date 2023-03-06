package ru.yandex.practicum.filmorate.exception;

public class EntityAlreadyExistsException extends RuntimeException {
    public EntityAlreadyExistsException(String entity, Long id) {
        super(entity + " with id=" + id + " already exists");
    }
}
