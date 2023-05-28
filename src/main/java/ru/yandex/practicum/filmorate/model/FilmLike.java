package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.Positive;
import java.util.HashMap;
import java.util.Map;

import static ru.yandex.practicum.filmorate.constant.FilmLikeConstant.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class FilmLike {

    @Positive
    private long id;

    @Positive
    private long userId;

    @Positive
    private long filmId;
    public Map<String, Object> toMap() {
        Map<String, Object> values = new HashMap<>();
        values.put(LIKE_ID, id);
        values.put(USER_ID, userId);
        values.put(FILM_ID, filmId);

        return values;
    }
}