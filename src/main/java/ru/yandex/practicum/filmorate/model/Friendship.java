package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.PositiveOrZero;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class Friendship {

    @PositiveOrZero
    private Long id;

    @PositiveOrZero
    private Long userId;

    @PositiveOrZero
    private Long friendId;

    private Boolean status;

}