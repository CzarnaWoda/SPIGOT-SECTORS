package pl.supereasy.sectors.core.kits.impl;

import org.bukkit.Material;
import pl.supereasy.sectors.core.kits.data.Kit;
import pl.supereasy.sectors.core.kits.managers.KitManager;

import java.util.HashMap;

public class KitManagerImpl implements KitManager {

    private final HashMap<String, Kit> kits;

    public KitManagerImpl(){
        this.kits = new HashMap<>();
    }
    @Override
    public Kit getKit(String id) {
        return kits.get(id);
    }

    @Override
    public Kit getKitByMaterial(Material material) {
        for(Kit kit : kits.values()){
            if(kit.getGuiItem().equals(material))
                return kit;
        }
        return null;
    }

    @Override
    public HashMap<String, Kit> getKits() {
        return kits;
    }
}
