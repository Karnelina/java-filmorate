package ru.yandex.practicum.filmorate.storage.dbStorage.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.EventDao;
import ru.yandex.practicum.filmorate.model.Event;

import java.util.List;

@Repository
@Slf4j
public class EventDbStorage implements EventStorage {
    private final EventDao eventDao;

    @Autowired
    public EventDbStorage(EventDao eventDao) {
        this.eventDao = eventDao;
    }

    @Override
    public void addEvent(long userId, long entityId, String eventType, String eventOperation) {
        eventDao.addEvent(userId, entityId, eventType, eventOperation);
    }

    @Override
    public List<Event> getEvents(long userId) {
        return eventDao.getEvents(userId);
    }
}
