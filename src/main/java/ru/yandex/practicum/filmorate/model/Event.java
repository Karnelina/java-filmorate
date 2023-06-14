package ru.yandex.practicum.filmorate.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class Event {
    private long timestamp;
    private long userId;
    private String eventType;
    private String operation;
    private long eventId;
    private long entityId;
}
