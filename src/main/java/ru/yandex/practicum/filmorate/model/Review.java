package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Data
@AllArgsConstructor
public class Review {

    private Long reviewId;
    @NonNull @NotBlank
    private String content;
    @NonNull
    private Boolean isPositive;
    @NonNull @Positive
    private Long userId;
    @NonNull @Positive
    private Long filmId;
    private Long useful;
}
