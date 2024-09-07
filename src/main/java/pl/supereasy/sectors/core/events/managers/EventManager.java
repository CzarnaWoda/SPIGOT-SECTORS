package pl.supereasy.sectors.core.events.managers;

import org.bukkit.entity.Player;
import pl.supereasy.sectors.core.events.data.Event;
import pl.supereasy.sectors.core.events.enums.EventType;

import java.util.List;
import java.util.Map;

public interface EventManager {

    boolean createGlobalEvent(String admin, EventType type, String time, int amount);

    Event getGlobalEvent(EventType type);

    Map<EventType, Event> getGlobalEvents();

    boolean removePlayerEvent(Player player, EventType type);

    void removeGlobalEvent(EventType type);

    boolean isOnEvent(EventType type);
}
