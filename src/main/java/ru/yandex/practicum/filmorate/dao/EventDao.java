package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Event;

import java.util.List;

public interface EventDao {
    void addEvent(long userId, long entityId, String eventType, String eventOperation);

    List<Event> getEvents(long userId);
}
