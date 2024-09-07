package pl.supereasy.sectors.core.events.data;

import pl.supereasy.sectors.core.events.enums.EventType;

public class Event {

    private final String admin;
    private final long eventTime;
    private final EventType type;
    private final int amount;

    public Event(String admin, long eventTime, EventType type, int amount){
        this.admin = admin;
        this.eventTime = eventTime;
        this.type = type;
        this.amount = amount;
    }

    public boolean isEnable(){
        return this.eventTime > System.currentTimeMillis();
    }

    public EventType getType() {
        return type;
    }

    public long getEventTime() {
        return eventTime;
    }

    public String getAdmin() {
        return admin;
    }

    public int getAmount() {
        return amount;
    }
}
