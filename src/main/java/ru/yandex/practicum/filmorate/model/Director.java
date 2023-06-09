package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.Getter;
import lombok.Builder;
import lombok.ToString;
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