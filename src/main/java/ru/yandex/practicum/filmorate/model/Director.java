package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@Setter
@Getter
@Builder
@ToString
public class Director {

    private Integer id;
    @NotBlank
    private String name;
}