package pl.supereasy.sectors.core.listeners;

import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.guilds.Guild;

public class PistonListener implements Listener {

    private final SectorPlugin plugin;
    public PistonListener(SectorPlugin plugin){
        this.plugin = plugin;
    }


    @EventHandler
    public void onPistonExtendEvent(BlockPistonExtendEvent event){
        event.getBlocks().forEach(block -> {
            final Guild guild = plugin.getGuildManager().getGuild(block.getLocation());
            if(guild != null && guild.getGuildRegion().isCenterWithAdd(block.getLocation(),6)){
                event.setCancelled(true);
            }
        });
    }
    @EventHandler
    public void onPistonRetractEvent(BlockPistonRetractEvent event){
        event.getBlocks().forEach(block -> {
            final Guild guild = plugin.getGuildManager().getGuild(block.getLocation());
            if(guild != null && guild.getGuildRegion().isCenterWithAdd(block.getLocation(),6)){
                event.setCancelled(true);
            }
        });
        final Guild guild = plugin.getGuildManager().getGuild(event.getRetractLocation());
        if(guild != null && guild.getGuildRegion().isCenterWithAdd(event.getRetractLocation(),6)){
            event.setCancelled(true);
        }
    }
}
