package ru.yandex.practicum.filmorate.storage.dbStorage.event;

import ru.yandex.practicum.filmorate.model.Event;

import java.util.List;

public interface EventStorage {
    void addEvent(long userId, long entityId, String eventType, String eventOperation);

    List<Event> getEvents(long userId);
}
