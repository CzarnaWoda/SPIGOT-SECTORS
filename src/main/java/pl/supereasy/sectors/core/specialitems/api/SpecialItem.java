package pl.supereasy.sectors.core.specialitems.api;

import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.util.ItemBuilder;

public abstract class SpecialItem {

    private final ItemBuilder itemBuilder;
    private final SectorPlugin plugin;

    public SpecialItem(final ItemBuilder itemBuilder,SectorPlugin plugin){
        this.itemBuilder = itemBuilder;
        this.plugin = plugin;
    }

    public abstract void openAction(PlayerInteractEvent  e);

    public abstract ItemStack getItemWithOption(Object option);


    public ItemBuilder getItemBuilder() {
        return itemBuilder;
    }

    public SectorPlugin getPlugin() {
        return plugin;
    }
}
