package pl.supereasy.sectors.core.kits.managers;

import org.bukkit.Material;
import pl.supereasy.sectors.core.kits.data.Kit;

import java.util.HashMap;

public interface KitManager {

    Kit getKit(String id);

    Kit getKitByMaterial(Material material);

    HashMap<String,Kit> getKits();
}
