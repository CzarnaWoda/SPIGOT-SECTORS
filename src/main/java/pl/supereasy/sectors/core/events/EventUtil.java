package pl.supereasy.sectors.core.events;

import pl.supereasy.sectors.core.events.enums.EventType;

import java.util.HashMap;
import java.util.Map;

public class EventUtil {

    private static final Map<EventType, String> nameMap = new HashMap<>();

    static {
        nameMap.put(EventType.TURBODROP, "TURBO DROP");
        nameMap.put(EventType.TURBOEXP, "TURBO EXP");
        nameMap.put(EventType.DROP,"DROP");
        nameMap.put(EventType.EXP, "EXP");
    }
    public static String getNameOfEvent(EventType type){
        return nameMap.get(type);
    }
}
