package ru.yandex.practicum.filmorate.model;

import lombok.*;
import org.hibernate.validator.constraints.Length;
import ru.yandex.practicum.filmorate.validation.After;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class Film {

    @Positive
    private Long id;

    @NotBlank
    private String name;

    @Length(max = 200)
    private String description;

    @After("1895-12-28")
    private LocalDate releaseDate;

    @Positive
    private int duration;

    private MpaRating mpa;

    private List<Long> likes = new ArrayList<>();

    private List<Genre> genres;

}