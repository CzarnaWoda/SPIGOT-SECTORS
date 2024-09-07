package pl.supereasy.sectors.core.tablist;

public enum TabListType {
    KILLS(1),EATKOX(2),BREAKSTONE(3),POINTS(4);

    public int index;

    TabListType(int i) {
        index = i;
    }
    public static TabListType getByIndex(int index){
        for(TabListType t : values()){
            if(t.index == index){
                return t;
            }
        }
        return null;
    }
}
