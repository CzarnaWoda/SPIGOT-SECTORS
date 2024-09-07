package pl.supereasy.sectors.core.events.impl;

import org.bukkit.entity.Player;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.core.events.data.Event;
import pl.supereasy.sectors.core.events.enums.EventType;
import pl.supereasy.sectors.core.events.managers.EventManager;
import pl.supereasy.sectors.util.TimeUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventManagerImpl implements EventManager {

    private final Map<EventType, Event> globalEvents;
    private final SectorPlugin plugin;

    public EventManagerImpl(SectorPlugin plugin){
        this.globalEvents = new HashMap<>();
        this.plugin = plugin;

        if(plugin.getCoreConfig().EVENTSMANAGER_DROP_TIME > System.currentTimeMillis()){
            int seconds = (int) ((plugin.getCoreConfig().EVENTSMANAGER_DROP_TIME-System.currentTimeMillis())/1000);
            createGlobalEvent("CzarnaWoda", EventType.DROP, seconds + "s", plugin.getCoreConfig().EVENTSMANAGER_DROP_VALUE);
        }

        if(plugin.getCoreConfig().EVENTSMANAGER_EXP_TIME > System.currentTimeMillis()){
            int seconds = (int) ((plugin.getCoreConfig().EVENTSMANAGER_EXP_TIME-System.currentTimeMillis())/1000);
            createGlobalEvent("CzarnaWoda", EventType.EXP, seconds + "s", plugin.getCoreConfig().EVENTSMANAGER_EXP_VALUE);
        }

        if(plugin.getCoreConfig().EVENTSMANAGER_TURBODROP_TIME > System.currentTimeMillis()){
            int seconds = (int) ((plugin.getCoreConfig().EVENTSMANAGER_TURBODROP_TIME-System.currentTimeMillis())/1000);
            createGlobalEvent("CzarnaWoda", EventType.TURBODROP, seconds + "s", plugin.getCoreConfig().EVENTSMANAGER_TURBODROP_VALUE);
        }
        if(plugin.getCoreConfig().EVENTSMANAGER_TURBOEXP_TIME > System.currentTimeMillis()){
            int seconds = (int) ((plugin.getCoreConfig().EVENTSMANAGER_TURBOEXP_TIME-System.currentTimeMillis())/1000);
            createGlobalEvent("CzarnaWoda", EventType.TURBOEXP, seconds + "s", plugin.getCoreConfig().EVENTSMANAGER_TURBOEXP_VALUE);
        }
    }
    @Override
    public boolean createGlobalEvent(String admin, EventType type, String time, int amount) {
        globalEvents.computeIfAbsent(type, k -> new Event(admin, Long.parseLong(time), type, amount));
        return true;
    }

    @Override
    public Event getGlobalEvent(EventType type) {
        return this.globalEvents.get(type);
    }


    @Override
    public Map<EventType, Event> getGlobalEvents() {
        return globalEvents;
    }

    @Override
    public boolean removePlayerEvent(Player player, EventType type) {
        return false;
    }

    @Override
    public void removeGlobalEvent(EventType type) {
        this.globalEvents.remove(type);
    }

    @Override
    public boolean isOnEvent(EventType type) {
        final Event event = getGlobalEvent(type);
        return event != null && event.isEnable();
    }
}
