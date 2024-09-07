package pl.supereasy.sectors.core.drop.managers;

import org.bukkit.Material;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.core.drop.data.Drop;
import pl.supereasy.sectors.core.drop.data.DropData;
import pl.supereasy.sectors.core.drop.drops.CancelDropData;
import pl.supereasy.sectors.core.drop.drops.NormalDropData;
import pl.supereasy.sectors.core.drop.drops.RandomDropData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DropManager {

    private final List<Drop> blockdrops;

    private final HashMap<Material,DropData> drops;

    private final HashMap<Material, Integer> exps;

    private final SectorPlugin plugin;

    public DropManager(SectorPlugin plugin){
        this.plugin = plugin;
        drops = new HashMap<>();
        blockdrops = new ArrayList<>();
        exps = new HashMap<>();

    }

    public void setup(){
        for(String material : plugin.getDropConfig().getConfig().getStringList("drops.block.cancel-drops")){
            drops.put(Material.getMaterial(material),new CancelDropData());
        }
        final RandomDropData data = new RandomDropData(plugin);
        for(Drop d : blockdrops){
            drops.put(d.getFrom(), data);
        }
        for(String key : plugin.getDropConfig().getConfigurationSection("drops.block.exp-drops").getKeys(false)){
            exps.put(Material.getMaterial(key),plugin.getConfig().getInt("drops.block.exp-drops." + key,1));
        }
        for (String s : plugin.getDropConfig().getConfig().getStringList("cancel-drops")) {
            drops.put(Material.getMaterial(s), new CancelDropData());
        }
    }
    public DropData getDropData(Material material){
        DropData dropData = new NormalDropData();
        if(drops.containsKey(material)){
            dropData = drops.get(material);
        }
        return dropData;
    }
    public int getExp(Material material){
        int exp = 0;
        if(exps.containsKey(material)){
            exp = exps.get(material);
        }
        return exp;
    }
    public void registerDrop(Drop drop){
        blockdrops.add(drop);
    }

    public List<Drop> getBlockdrops() {
        return blockdrops;
    }

    public HashMap<Material, Integer> getExps() {
        return exps;
    }
}
