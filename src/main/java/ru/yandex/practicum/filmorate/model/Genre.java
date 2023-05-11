package ru.yandex.practicum.filmorate.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Value;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class Genre {
    int id;
    String name;
}
