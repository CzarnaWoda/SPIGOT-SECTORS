package pl.supereasy.sectors.core.events.enums;

public enum EventType {
    
    TURBODROP(1),TURBOEXP(2),DROP(3),EXP(4);

    private final int typeNumber;

    EventType(int i) {
        this.typeNumber = i;
    }

    public static EventType getByInt(int type){
        for(EventType types : values()){
            if(types.getTypeNumber() == type){
                return types;
            }
        }
        return null;
    }

    public int getTypeNumber() {
        return typeNumber;
    }
}
