package pl.supereasy.sectors.core.specialitems.manager;

import pl.supereasy.sectors.core.specialitems.api.SpecialItem;
import pl.supereasy.sectors.core.specialitems.types.SpecialItemType;

import java.util.HashMap;

public interface SpecialItemManager {

    void loadSpecialItems();


    HashMap<SpecialItemType, SpecialItem> getSpecialItems();
}
